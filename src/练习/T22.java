package 练习;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 忙等待
 */
public class T22 {
    public static void main(String[] args) {
        /*Flag flag = new Flag();

        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new V1(flag));
        exec.execute(new V2(flag));

        exec.shutdown();*/

        Flag flag = new Flag();

        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(new V3(flag));
        exec.execute(new V4(flag));

        exec.shutdown();
    }
}

class Flag {
    volatile boolean flag = false;//如果不使用volatile关键词，则在V1修改了，但是在V2不可视
}

class V1 implements Runnable {
    Flag flag;

    public V1(Flag flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        try {
            System.out.println("v1 start...");
            TimeUnit.SECONDS.sleep(5);
            System.out.println("v1 wakeup");
            flag.flag = true;
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class V2 implements Runnable {
    Flag flag;

    public V2(Flag flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        System.out.println("v2 start...");
        while (!flag.flag) { //此处为  while阻塞

        }
        System.out.println("v2 true");
        flag.flag = false;
        System.out.println("v2 false");
    }
}

class V3 implements Runnable {
    Flag flag;

    public V3(Flag flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        try {
            System.out.println("v3 start...");
            TimeUnit.SECONDS.sleep(5);
            System.out.println("v3 wakeup");
            synchronized (flag) {
                flag.flag = true;
                System.out.println("v3 flag.notifyAll()");
                flag.notifyAll();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class V4 implements Runnable {
    Flag flag;

    public V4(Flag flag) {
        this.flag = flag;
    }

    @Override
    public void run() {
        System.out.println("V4 start...");
        synchronized (flag) {
            while (!flag.flag) {
                try {
                    System.out.println("V4 flag.wait()");
                    flag.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("V4 interrupt");
                }
            }
        }
        System.out.println("V4 true");
        flag.flag = false;
        System.out.println("V4 false");
    }
}