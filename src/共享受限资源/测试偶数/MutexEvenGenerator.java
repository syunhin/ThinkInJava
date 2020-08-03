package 共享受限资源.测试偶数;

import 共享受限资源.消费者任务.IntGenerator;

public class MutexEvenGenerator extends IntGenerator {
    @Override
    public int next() {
        return 0;
    }
}
