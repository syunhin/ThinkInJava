package 死锁.哲学家就餐问题;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 哲学家就餐问题-死锁版本
 */
public class DeadlockingDiningPhilosophers {
    public static void main(String[] args) throws IOException {
        int ponder = 0;
        int size = 5;

        ExecutorService exec = Executors.newCachedThreadPool();
        Chopstick[] sticks = new Chopstick[size];

        for (int i = 0; i < size; i++) {
            sticks[i] = new Chopstick();
        }

        for (int i = 0; i < size; i++) {
            exec.execute(new Philosopher(sticks[i], sticks[(i + 1) % size], i, ponder));
        }

        System.out.println("Press 'Enter' to quit");
        System.in.read();

        exec.shutdownNow();
    }
}
