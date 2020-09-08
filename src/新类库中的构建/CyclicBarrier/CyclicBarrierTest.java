package 新类库中的构建.CyclicBarrier;

import java.util.concurrent.*;

/**
 * 简单测试类
 */
public class CyclicBarrierTest {
    public static void main(String[] args) throws InterruptedException {
        int size = 10;
        final ExecutorService exec = Executors.newCachedThreadPool();
        final CyclicBarrier barrier = new CyclicBarrier(size, new Runnable() {

            private int count = 0;

            @Override
            public void run() {
                System.out.println("\n==============="+count++);
                if(count == 5){
                    exec.shutdownNow();
                }
            }
        });

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
                    try {
                        while(!Thread.interrupted()) {
                            System.out.print(id);
                            barrier2.await();
                        }
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

