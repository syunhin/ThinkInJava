package 终结任务.中断任务;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

//无法阻塞的类型-I/O和等待synchronized锁
public class Interrupting {
    private static ExecutorService exec = Executors.newCachedThreadPool();
    static void test(Runnable r) throws InterruptedException{
        Future<?> f = exec.submit(r);
        TimeUnit.MILLISECONDS.sleep(100);
        System.out.println("interrupting "+r.getClass().getName());
        f.cancel(true);
        System.out.println("interrupt sent to "+r.getClass().getName());
    }

    public static void main(String[] args) throws InterruptedException {
        test(new SleepBlocked());
        test(new IOBlocked(System.in));
        test(new SynchronizedBlocked());
        TimeUnit.SECONDS.sleep(3);
        System.out.println("aborting with system.exit(0)");
        System.exit(0);
    }
}

class SleepBlocked implements Runnable{
    @Override
    public void run() {
        try{
            TimeUnit.SECONDS.sleep(100);
        }catch (InterruptedException e){
            System.out.println("InterruptedException");
        }
        System.out.println("Exiting sleepBlocked.run()");
    }
}

class IOBlocked implements Runnable{
    private InputStream in;

    public IOBlocked(InputStream in) {
        this.in = in;
    }

    @Override
    public void run() {
        try{
            System.out.println("Waiting for read(): ");
            in.read();
        }catch (IOException e){
            if (Thread.currentThread().isInterrupted()){
                System.out.println("Interrupted from blocked I/O");
            }else{
                throw new RuntimeException(e);
            }
        }
        System.out.println("Exiting IOBlocked.run()");
    }
}

class SynchronizedBlocked implements Runnable{
    public synchronized void f(){
        while (true){
            Thread.yield();
        }
    }
    public SynchronizedBlocked(){
        new Thread(){
            @Override
            public void run() {
                f();
            }
        }.start();
    }

    @Override
    public void run() {
        System.out.println("trying to call f();");
        f();
        System.out.println("exiting synchronizedBlocked.run()");
    }
}