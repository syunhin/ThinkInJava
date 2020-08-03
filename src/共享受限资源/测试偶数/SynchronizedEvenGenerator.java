package 共享受限资源.测试偶数;

import 共享受限资源.消费者任务.EvenChecker;
import 共享受限资源.消费者任务.IntGenerator;

public class SynchronizedEvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;

    @Override
    public synchronized int next() {
        ++currentEvenValue;
        Thread.yield();
        ++currentEvenValue;
        return currentEvenValue;
    }

    public static void main(String[] args) {
        EvenChecker.test(new EvenGenerator());
    }

}
