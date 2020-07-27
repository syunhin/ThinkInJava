package Executor;

import 定义任务.LiftOff;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FixedThreadPool {
    public static void main(String[] args) {
        ExecutorService exec = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 50; i++) {
            exec.execute(new LiftOff());

        }
        exec.shutdown();
    }
    //候娟娟她看着不爽，把库删了
}
