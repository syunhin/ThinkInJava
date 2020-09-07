package 死锁.哲学家就餐问题;

/**
 * 筷子类（一根）
 */
public class Chopstick {
    private boolean taken = false;//可用标志
    public synchronized void take() throws InterruptedException{
        while (taken)
            wait();
        taken = true;
    }

    public synchronized void drop(){
        taken = false;
        notifyAll();
    }
}
