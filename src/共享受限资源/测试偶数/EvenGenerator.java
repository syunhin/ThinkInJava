package 共享受限资源.测试偶数;

import 共享受限资源.消费者任务.EvenChecker;
import 共享受限资源.消费者任务.IntGenerator;

public class EvenGenerator extends IntGenerator {
    private int currentEvenValue = 0;

    @Override
    public int next() {
        ++currentEvenValue;//问题点
//        Thread.yield();//如果想更快的发现问题
        ++currentEvenValue;
        return currentEvenValue;
    }

    public static void main(String[] args) {
        EvenChecker.test(new EvenGenerator());
    }
}
