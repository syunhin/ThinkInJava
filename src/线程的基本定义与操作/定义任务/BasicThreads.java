package 线程的基本定义与操作.定义任务;

public class BasicThreads {
    public static void main(String[] args) {
        Thread t = new Thread(new LiftOff());
        t.start();
        System.out.println("waiting for Liftoff");
    }
}
