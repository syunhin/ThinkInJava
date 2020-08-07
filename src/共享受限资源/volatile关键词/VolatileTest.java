package 共享受限资源.volatile关键词;

/**
 * volatile保证不了原子性
 */
public class VolatileTest{
    public volatile int inc = 0;

    public void increase() {
        inc++;
    }

    public static void main(String[] args) {
        final VolatileTest test = new VolatileTest();
        for(int i=0;i<10;i++){
            new Thread(){
                {setDaemon(true);}
                public void run() {
                    for(int j=0;j<1000;j++)
                        test.increase();
                    System.out.println(Thread.currentThread()+" end");
                };
            }.start();
        }

        while(Thread.activeCount()>2)  //保证前面的线程都执行完
            Thread.yield();
        System.out.println(test.inc);
    }
}
