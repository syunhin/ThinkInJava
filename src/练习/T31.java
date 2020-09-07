package 练习;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.*;

public class T31 {
    public static void main(String[] args) throws InterruptedException, IOException {
        int ponder = 0;

        BlockingQueue<Chopstick> queue = new LinkedBlockingQueue<>();
        for (int i = 0; i < 6; i++) {//若筷子数与人数相等则会死锁
            Chopstick chopstick = new Chopstick(i);
            queue.put(chopstick);
        }

        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            exec.execute(new Philosopher(queue,i,ponder));
        }

        System.in.read();
        exec.shutdownNow();
    }
}

class Chopstick {
    private int num;

    public Chopstick(int num) {
        this.num = num;
    }

    @Override
    public String toString() {
        return "Chopstick{" +
                "num=" + num +
                '}';
    }
}

class ChopstickQueue extends LinkedBlockingQueue<Chopstick>{}

class Philosopher implements Runnable{
    private BlockingQueue<Chopstick> queue;
    private final int id;
    private final int ponderFactor;
    private Random rand = new Random(47);

    public Philosopher(BlockingQueue<Chopstick> queue, int id, int ponderFactor) {
        this.queue = queue;
        this.id = id;
        this.ponderFactor = ponderFactor;
    }

    private void pause() throws InterruptedException {//暂停
        if (ponderFactor == 0) return;
        TimeUnit.MILLISECONDS.sleep(rand.nextInt(ponderFactor * 250));
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                System.out.println(this + " " + "thinking");
                pause();

                //饿了
                System.out.println(this + " " + "grabbing right");
                Chopstick right = queue.take();
                System.out.println(this + " " + "grab right: "+right);

                System.out.println(this + " " + "grabbing left");
                Chopstick left = queue.take();
                System.out.println(this + " " + "grab left: "+left);

                System.out.println(this + " " + "eating");
                pause();
                queue.put(right);
                queue.put(left);
            }
        } catch (InterruptedException e) {
            System.out.println(this + " " + "exiting via interrupt");
        }
    }

    @Override
    public String toString() {
        return "Philosopher{" +
                "id=" + id +
                '}';
    }
}