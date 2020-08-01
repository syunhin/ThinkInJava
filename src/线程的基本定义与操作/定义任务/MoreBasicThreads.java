package 线程的基本定义与操作.定义任务;

public class MoreBasicThreads {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            new Thread(new LiftOff()).start();
        }
        System.out.println("waiting for LiftOff");
    }
}
