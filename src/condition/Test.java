package condition;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Test {
    public static void main(String[] args) throws InterruptedException {
        Test1 test1 = new Test1();
        new Thread(test1).start();

        TimeUnit.SECONDS.sleep(5);
        test1.lock.lock();
        try {
            test1.condition.signal();
        } finally {
            test1.lock.unlock();
        }
    }
}

class Test1 implements Runnable {
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    @Override
    public void run() {
        lock.lock();
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.SECOND, 3);

            boolean a = condition.awaitUntil(cal.getTime());
            System.out.println(a);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }
}

