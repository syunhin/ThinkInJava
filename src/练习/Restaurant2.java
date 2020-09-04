package 练习;


//class Meal {
//    private final int orderNum;
//    public Meal(int orderNum) { this.orderNum = orderNum; }
//    public String toString() { return "Meal " + orderNum; }
//}
//
//class WaitPerson27 implements Runnable {
//    private Restaurant2 restaurant;
//    protected Lock lock = new ReentrantLock();
//    protected Condition condition = lock.newCondition();
//    public WaitPerson27(Restaurant2 r) { restaurant = r; }
//    public void run() {
//        try {
//            while(!Thread.interrupted()) {
//                lock.lock();
//                try {
//                    while(restaurant.meal == null)
//                        condition.await();
//                } finally {
//                    lock.unlock();
//                }
//                System.out.println("waitPerson got " + restaurant.meal);
//                restaurant.chef.lock.lock();
//                try {
//                    restaurant.meal = null;
//                    restaurant.chef.condition.signalAll();
//                } finally {
//                    restaurant.chef.lock.unlock();
//                }
//            }
//        } catch(InterruptedException e) {
//            System.out.println("WaitPerson27 interrupted");
//        }
//    }
//}
//
//class Chef27 implements Runnable {
//    private Restaurant2 restaurant;
//    private int count = 0;
//    protected Lock lock = new ReentrantLock();
//    protected Condition condition = lock.newCondition();
//    public Chef27(Restaurant2 r) { restaurant = r; }
//    public void run() {
//        try {
//            while(!Thread.interrupted()) {
//                lock.lock();
//                try {
//                    while(restaurant.meal != null)
//                        condition.await();
//                } finally {
//                    lock.unlock();
//                }
//                if(++count == 10) {
//                    System.out.println("Out of food, closing");
//                    restaurant.exec.shutdownNow();
//                    return;
//                }
//                System.out.print("Order up! ");
//                restaurant.waitPerson.lock.lock();
//                try {
//                    restaurant.meal = new Meal(count);
//                    restaurant.waitPerson.condition.signalAll();
//                } finally {
//                    restaurant.waitPerson.lock.unlock();
//                }
//                TimeUnit.MILLISECONDS.sleep(100);
//            }
//        } catch(InterruptedException e) {
//            System.out.println("chef interrupted");
//        }
//    }
//}
//
//public class Restaurant2 {
//    Meal meal;
//    ExecutorService exec = Executors.newCachedThreadPool();
//    WaitPerson27 waitPerson = new WaitPerson27(this);
//    Chef27 chef = new Chef27(this);
//    public Restaurant2() {
//        exec.execute(chef);
//        exec.execute(waitPerson);
//    }
//    public static void main(String[] args) {
//        new Restaurant2();
//    }
//}

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Restaurant2 {
    Meal meal;
    ExecutorService exec = Executors.newCachedThreadPool();
    WaitPerson waitPerson = new WaitPerson(this);
    Chef chef = new Chef(this);

    public Restaurant2() {
        exec.execute(chef);
        exec.execute(waitPerson);
    }

    public static void main(String[] args) {
        Restaurant2 r = new Restaurant2();
    }
}

class Chef implements Runnable {
    private Restaurant2 restaurant;
    private int count = 0;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public Chef(Restaurant2 restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                lock.lock();
                try {
                    while (restaurant.meal != null) {
                        condition.await();
                    }
                } finally {
                    lock.unlock();
                }

                if (++count == 10) {
                    System.out.println("out of food, closing");
                    restaurant.exec.shutdownNow();
                    return;
                }
                System.out.print("order up! ");

                restaurant.waitPerson.lock.lock();
                try {
                    restaurant.meal = new Meal(count);
                    restaurant.waitPerson.condition.signalAll();
                } finally {
                    restaurant.waitPerson.lock.unlock();
                }
                TimeUnit.MILLISECONDS.sleep(100);
            }
        } catch (InterruptedException e) {
            System.out.println("chef interrupted");
        }
    }
}

class WaitPerson implements Runnable {
    private Restaurant2 restaurant;
    Lock lock = new ReentrantLock();
    Condition condition = lock.newCondition();

    public WaitPerson(Restaurant2 r) {
        restaurant = r;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                lock.lock();
                try {
                    while (restaurant.meal == null) {
                        condition.await();
                    }
                } finally {
                    lock.unlock();
                }

                System.out.println("Waitperson got " + restaurant.meal);

                restaurant.chef.lock.lock();
                try {
                    restaurant.meal = null;
                    restaurant.chef.condition.signalAll();
                } finally {
                    restaurant.chef.lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            System.out.println("WaitPerson interrupted");
        }
    }
}

class Meal {
    private final int orderNum;

    public Meal(int orderNum) {
        this.orderNum = orderNum;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "orderNum=" + orderNum +
                '}';
    }
}

