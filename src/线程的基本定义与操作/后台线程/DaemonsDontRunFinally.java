package 线程的基本定义与操作.后台线程;

import java.util.concurrent.TimeUnit;

public class DaemonsDontRunFinally {
    public static void main(String[] args) {
        Thread t = new Thread(new ADaemon());
        t.setDaemon(true);
        t.start();
    }
}

class ADaemon implements Runnable {
    @Override
    public void run() {
        try {
            System.out.println("starting ADaemon");
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("this should always run?");
        }
    }
}
