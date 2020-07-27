package 定义任务;

public class LiftOff implements Runnable {
    protected int countDown = 10;
    private static int tackCount = 0;
    private final int id = tackCount++;

    public LiftOff() {
    }

    public LiftOff(int countDown) {
        this.countDown = countDown;
    }

    public String status() {
        return "#" + id + "(" + (countDown > 0 ? countDown : "Liftoff!") + "),";
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        while (countDown-- > 0) {
            System.out.println(status());
            Thread.yield();
            /*
             Thread.yield()的调用是对线程调度器（java线程机制的一部分，可以将CPU从一个线程转移给另一个线程）的建议，
             它声明：”我已经执行完声明周期中最重要的部分了，此刻正是切换给其他任务执行一段时间的大好时机。“
             这完全是选择性的，但是这里使用它是因为它会在这些示例中产生更加有趣的输出：你更有可能会看到任务换进换出的证据。
             */
        }
    }
}
