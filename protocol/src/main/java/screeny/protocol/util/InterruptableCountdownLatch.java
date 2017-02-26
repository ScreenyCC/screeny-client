package screeny.protocol.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class InterruptableCountdownLatch {
    private final CountDownLatch countDownLatch;
    private int count;
    private boolean interrupted;

    public InterruptableCountdownLatch( int count ) {
        this.count = count;
        this.countDownLatch = new CountDownLatch( count );
    }

    public boolean await( long time, TimeUnit timeUnit ) throws InterruptedException {
        boolean result = this.countDownLatch.await( time, timeUnit );
        if ( this.interrupted ) {
            throw new InterruptedException( "Countdownlatch interrupted" );
        }
        return result;
    }

    public void countDown() {
        this.count--;
        this.countDownLatch.countDown();
    }

    public void interrupt() {
        this.interrupted = true;
        for ( int i = 0; i < this.count; i++ ) {
            this.countDown();
        }
    }

}
