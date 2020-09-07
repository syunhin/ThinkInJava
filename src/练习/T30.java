package 练习;

import java.util.Random;
import java.util.concurrent.*;

public class T30 {
    public static void main(String[] args) throws InterruptedException {
        BlockingQueue<Character> charQueue = new LinkedBlockingDeque<Character>();
        Sender sender = new Sender(charQueue);
        Receiver receiver = new Receiver(charQueue);

        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(sender);
        exec.execute(receiver);

        TimeUnit.SECONDS.sleep(4);
        exec.shutdownNow();
    }
}

class Receiver implements Runnable {
    private BlockingQueue<Character> charQueue;

    public Receiver(BlockingQueue<Character> charQueue) {
        this.charQueue = charQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.println("read: " + charQueue.take() + ", ");
            }
        } catch (InterruptedException e) {
            System.out.println(e + " Receiver read exception");
        }
    }
}

class Sender implements Runnable {
    private Random rand = new Random(47);
    private BlockingQueue<Character> charQueue;

    public Sender(BlockingQueue<Character> charQueue) {
        this.charQueue = charQueue;
    }

    @Override
    public void run() {
        try {
            while (true) {
                for (char c = 'A'; c <= 'z'; c++) {
                    charQueue.put(c);
                    TimeUnit.MILLISECONDS.sleep(rand.nextInt(500));
                }
            }
        } catch (InterruptedException e) {
            System.out.println(e + " Sender sleep interrupted");
        }
    }
}