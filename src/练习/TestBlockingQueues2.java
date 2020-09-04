package 练习;

import 线程的基本定义与操作.定义任务.LiftOff;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.*;

public class TestBlockingQueues2 {
    static void getkey() {
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void getkey(String message) {
        System.out.println(message);
        getkey();
    }

    static void test(String msg, BlockingQueue<LiftOff> queue) {
        System.out.println(msg);
        LiftOffRunner runner = new LiftOffRunner(queue);
        LiftOffAdder adder = new LiftOffAdder(runner);
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(runner);
        exec.execute(adder);
        getkey("Press 'Enter' (" + msg + ")");
        exec.shutdownNow();
        System.out.println("Finished " + msg + " test");
    }

    public static void main(String[] args) {
        test("LinkedBlockingQueue", new LinkedBlockingDeque<LiftOff>());
        test("ArrayBlockingQueue", new ArrayBlockingQueue<LiftOff>(3));//大小限制
        test("SynchronousQueue", new SynchronousQueue<LiftOff>());//大小为1

    }
}

class LiftOffRunner implements Runnable {
    private BlockingQueue<LiftOff> rockets;

    public LiftOffRunner(BlockingQueue<LiftOff> rockets) {
        this.rockets = rockets;
    }

    public void add(LiftOff lo) {
        try {
            rockets.put(lo);
        } catch (InterruptedException e) {
            System.out.println("Interrupt during put()");
        }
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                LiftOff rocket = rockets.take();
                rocket.run();//执行此任务
            }
        } catch (InterruptedException e) {
            System.out.println("waking from take()");
        }
        System.out.println("Exiting LiftOffRunner");
    }
}

class LiftOffAdder implements Runnable {
    private LiftOffRunner runner;

    public LiftOffAdder(LiftOffRunner runner) {
        this.runner = runner;
    }

    public void run() {
        for (int i = 0; i < 5; i++)
            runner.add(new LiftOff(5));
    }
}
