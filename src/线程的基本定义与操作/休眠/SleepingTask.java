package 线程的基本定义与操作.休眠;

import 线程的基本定义与操作.定义任务.LiftOff;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class SleepingTask extends LiftOff {

    @Override
    public void run() {
        try {
            while (countDown-- > 0) {
                System.out.println(status());
                //old-style:
                //Thread.sleep(100);
                //java se5/6 style
                int time = new Random().nextInt(10)+1;
                System.out.println(getId()+" sleep "+time+"s");
                TimeUnit.MILLISECONDS.sleep(time*1000);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++) {
            exec.execute(new SleepingTask());
        }
        exec.shutdown();
    }
}
