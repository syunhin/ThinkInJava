package 线程的基本定义与操作.加入一个线程;

public class Joining {
    public static void main(String[] args) {
        Sleeper sleepy = new Sleeper("Sleepy", 1500),
                grumpy = new Sleeper("Grumpy", 1500);
        Joiner dopey = new Joiner("Dopey", sleepy),
                doc = new Joiner("Doc", grumpy);

        grumpy.interrupt();
    }
}

class Sleeper extends Thread {
    private int duration;

    public Sleeper(String name, int sleepTime) {
        super(name);
        duration = sleepTime;
        start();
    }

    @Override
    public void run() {
        try {
            sleep(duration);

            int count = 1;
            while(true){
                sleep(1000);//循环
                System.out.print( getName() + " doing, ");
                count++;
                if(count == 5){
                    System.out.println();
                    return;
                }
            }
        } catch (InterruptedException e) {
            System.out.println(getName() + " was interrupted.isInterrupted(): " + isInterrupted());
            return;
        }
        //System.out.println(getName() + " has awakened");
    }
}

class Joiner extends Thread {
    private Sleeper sleeper;

    public Joiner(String name, Sleeper sleeper) {
        super(name);
        this.sleeper = sleeper;
        start();
    }

    @Override
    public void run() {
        try {
            sleeper.join();//如果不指定值，则会一直等待
        } catch (InterruptedException e) {
            System.out.println("Interrupted");
        }
        System.out.println(getName() + " join completed");
    }
}
