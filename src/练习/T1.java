package 练习;

public class T1 {
    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable1()).start();
        }
    }
}

class Runnable1 implements Runnable {
    public static int count = 1;

    private final int num = count++;

    public Runnable1() {
        System.out.println("init Runnable1-" + num);
    }

    @Override
    public void run() {
        System.out.println("run Runnable1-" + num + " open");
        System.out.println("run Runnable1-" + num + " part-1");
        Thread.yield();

        System.out.println("run Runnable1-" + num + " part-2");
        Thread.yield();

        System.out.println("run Runnable1-" + num + " part-3");
        Thread.yield();

        System.out.println("run Runnable1-" + num + " close");
    }
}
