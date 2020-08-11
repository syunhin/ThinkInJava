package 终结任务.检查中断;

import java.util.concurrent.TimeUnit;

/*
    > **用法**：
> 1. 判断非阻塞时中断时，使用interrupted()；
> 2. 判断阻塞时中断时，通过捕获异常即可；
 */
public class InterruptingIdiom {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Blocked3());
        t.start();
        TimeUnit.MILLISECONDS.sleep(500);
        t.interrupt();
    }
}
class Blocked3 implements Runnable{
    private volatile double d = 0.0;

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                //点1
                NeedsCleanup n1 = new NeedsCleanup(1);
                //定义n1后立即开始try-finally。 以确保正确清理n1
                try {
                    System.out.println("sleeping");
                    TimeUnit.SECONDS.sleep(1);

                    //点2
                    NeedsCleanup n2 = new NeedsCleanup(2);
                    //确保正确清理n2
                    try {
                        System.out.println("Calculating");//计算
                        for (int i = 0; i < 25000000; i++) {
                            d = d + (Math.PI + Math.E) / d;
                        }
                        System.out.println("Finished time-consuming operation");//完成耗时的操作
                    } finally {
                        n2.cleanup();
                    }
                } finally {
                    n1.cleanup();
                }
            }
            System.out.println("Exiting via while() test");
        }catch (InterruptedException e){
            System.out.println("Exiting via interruptedException");
        }
    }
}

class NeedsCleanup{
    private final int id;

    public NeedsCleanup(int id) {
        this.id = id;
        System.out.println("NeedsCleanup "+id);
    }

    public void cleanup(){
        System.out.println("Cleaning up "+id);
    }
}


