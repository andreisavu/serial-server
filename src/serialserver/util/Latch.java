
package serialserver.util;

/**
 * Simple latch around a counter
 * 
 */
public class Latch {

    private final Object sync = new Object();
    private int count;

    public Latch(int count) {
        synchronized(sync) {
            this.count = count;
        }
    }

    public void awaitZero() {
        synchronized(sync) {
            while(count > 0) {
                try {
                    sync.wait();
                } catch (InterruptedException ex) {
                }
            }
        }
    }

    public void decrement() {
        synchronized(sync) {
            if(--count <= 0) {
                sync.notifyAll();
            }
        }
    }

}
