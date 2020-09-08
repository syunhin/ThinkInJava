package 新类库中的构建.CyclicBarrier;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class HorseRace {
    static final int FINISH_LINE = 75;
    private List<Horse> horses = new ArrayList<>();
    private ExecutorService exec = Executors.newCachedThreadPool();
    private CyclicBarrier barrier;

    public HorseRace(int nHorses, final int pause) {
        System.out.println("nHorses: " + nHorses + " ,pause: " + pause);
        barrier = new CyclicBarrier(nHorses, new Runnable() {
            @Override
            public void run() {
                StringBuilder s = new StringBuilder();
                for (int i = 0; i < FINISH_LINE; i++) {
                    s.append("=");//赛道上的栅栏
                }
                System.out.println(s);
                for (Horse horse:horses){
                    System.out.println(horse.tracks());
                }
                for (Horse horse : horses) {
                    if (horse.getStrides() > FINISH_LINE) {
                        System.out.println(horse + "won!");
                        exec.shutdownNow();
                        return;
                    }

                    try {
                        TimeUnit.MILLISECONDS.sleep(pause);
                    } catch (InterruptedException e) {
                        System.out.println("barrier-action sleep interrupted");
                    }
                }
            }
        });
        Horse.barrier = barrier;
        for (int i = 0; i < nHorses; i++) {
            Horse horse = new Horse();
            horses.add(horse);
            exec.execute(horse);
        }
    }

    public static void main(String[] args) {
        int nHorses = 7;
        int pause = 10;

        new HorseRace(nHorses, pause);
    }

}

class Horse implements Runnable {
    private static int counter = 0;
    private final int id = counter++;

    private int strides = 0;
    private static Random rand = new Random(47);

    public static CyclicBarrier barrier;

    @Override
    public void run() {
        try {
            while ((!Thread.interrupted())) {
                synchronized (this) {
                    strides += rand.nextInt(3);//生成 0、1或者2
                }
                barrier.await();
            }
        } catch (InterruptedException e) {

        } catch (BrokenBarrierException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "Horse{" +
                "id=" + id +
                ", strides=" + strides +
                '}';
    }

    public String tracks() {
        StringBuilder s = new StringBuilder();
        for (int i = 0; i < getStrides(); i++) {
            s.append("*");
        }
        s.append(id);
        return s.toString();
    }

    public int getStrides() {
        return strides;
    }
}
