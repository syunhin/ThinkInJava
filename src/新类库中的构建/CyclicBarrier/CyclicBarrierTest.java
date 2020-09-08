package 新类库中的构建.CyclicBarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 简单测试类
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        int size = 10;
        final CyclicBarrier barrier = new CyclicBarrier(size, new Runnable() {
            @Override
            public void run() {
                System.out.println("===============");
            }
        });

        ExecutorService exec = Executors.newCachedThreadPool();
        for (int i = 0; i < size; i++) {
            final int a = i;
            exec.execute(new Runnable() {
                private int id = a;
                private CyclicBarrier barrier2;
                {
                    barrier2 = barrier;
                }
                @Override
                public void run() {
                    System.out.print(id);
                    try {
                        barrier2.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (BrokenBarrierException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}

