package 线程的基本定义与操作.优先级;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimplPriorites implements Runnable {
    private int countDown = 5;
    private volatile double d;//No optimization
    private int priority;

    public SimplPriorites(int priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return Thread.currentThread() + ": " + countDown;
    }

    @Override
    public void run() {
        Thread.currentThread().setPriority(priority);
        while (true) {
            //中断操作
            for (int i = 1; i < 100000; i++) {
                d += (Math.PI + Math.E) / (double) i;
                if (i % 1000 == 0)
                    Thread.yield();
            }
            System.out.println(this);
            if (--countDown == 0) return;
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            exec.execute(new SimplPriorites(Thread.MIN_PRIORITY));
        }

        exec.execute(new SimplPriorites(Thread.MAX_PRIORITY));
        exec.shutdown();
    }
}
