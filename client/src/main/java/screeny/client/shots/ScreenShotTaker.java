package screeny.client.shots;

import java.io.File;
import java.util.concurrent.CountDownLatch;

public abstract class ScreenShotTaker {
    private final CountDownLatch countDownLatch = new CountDownLatch( 1 );

    public void startAndWait() {
        this.start();
        try {
            this.countDownLatch.await();
        } catch ( InterruptedException e ) {
            // NOP
        }
    }

    public void done() {
        this.countDownLatch.countDown();
    }

    public abstract void start();

    public abstract File getImgageFile();
}
