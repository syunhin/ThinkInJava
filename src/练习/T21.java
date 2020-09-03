package 练习;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 练习21 ：
 * 创建两个Runnable：
 * 其中一个的run()方法启动并调用wait()，
 * 而第二个类应该捕获第一个Runnable对象的引用，其run()方法应该在一定的秒数之后，为第一个任务调用notifyAll()，从而使得第一个任务可以显示一条信息。
 * 使用Executor来测试你的类。
 */

public class T21 {
    public static void main(String[] args) {
        R1 r1 = new R1();
        R2 r2 = new R2(r1);

        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(r1);
        exec.execute(r2);
        exec.shutdown();//如果没有这句，则线程池一直会在等待

    }
}

class R1 implements Runnable {

    @Override
    public void run() {
        try {
            synchronized (this) {
                System.out.println("r1.wait()");
                wait();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("R1 interrupted");
        }
        System.out.println("R1 awake up 完");
    }
}

class R2 implements Runnable {
    private R1 r1;

    public R2(R1 r1) {
        this.r1 = r1;
    }

    @Override
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(5);
            synchronized (r1) {
                System.out.println("r1.notifyAll() 前");
                r1.notifyAll();
                System.out.println("r1.notifyAll() 后");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("R2 完");
    }
}
