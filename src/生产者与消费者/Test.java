package 生产者与消费者;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Test {
    public static void main(String[] args) {
        A a = new A();
        ExecutorService exec = Executors.newCachedThreadPool();
        exec.execute(a);
        exec.shutdownNow();
        a.cancel();
    }
}

class A implements Runnable{
    boolean flag = true;

    public void cancel(){
        flag = false;
    }

    @Override
    public void run() {
        try {
            System.out.println("a start...");
            while (flag) {

            }
            System.out.println("to sleep()");
            TimeUnit.SECONDS.sleep(3);
        }catch (InterruptedException e){
            System.out.println("interrupt");
        }
    }
}
