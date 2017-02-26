package screeny.client.audio;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class AudioPlayer extends Thread implements LineListener {
    private InputStream audioFileStream;

    public AudioPlayer( InputStream audioFileStream ) {
        this.audioFileStream = audioFileStream;
    }

    public void start() {
        super.start();
    }

    @Override
    public void run() {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream( audioFileStream );
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream( bufferedInputStream );
            AudioFormat audioFormat = audioInputStream.getFormat();

            DataLine.Info info = new DataLine.Info( Clip.class, audioFormat );

            Clip clip = ( Clip ) AudioSystem.getLine( info );
            clip.addLineListener( this );
            clip.open( audioInputStream );
            clip.start();

            synchronized ( this ) {
                this.wait();
            }

            clip.close();
            bufferedInputStream.close();
            audioFileStream.close();
            audioInputStream.close();
        } catch ( Exception exc ) {
            exc.printStackTrace();
        }
    }

    @Override
    public void update( LineEvent event ) {
        if ( event.getType().equals( LineEvent.Type.STOP ) ) {
            synchronized ( this ) {
                this.notify();
            }
        }
    }
}
