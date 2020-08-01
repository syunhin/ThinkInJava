package 线程的基本定义与操作.编码的变体;

import java.util.concurrent.TimeUnit;

public class SelfManaged implements Runnable {
    private int countDown = 5;
    private Thread t = new Thread(this);
    private String name;

    public SelfManaged() {
        t.start();
        try {
            TimeUnit.SECONDS.sleep(2);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
        name = "test";
    }

    @Override
    public String toString() {
        return Thread.currentThread().getName() + "(" + countDown +
                ")";
    }

    @Override
    public void run() {
        System.out.println(name);
        while (true) {
            System.out.println(this);
            if (--countDown == 0) {
                return;
            }
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new SelfManaged();
        }
    }
}
