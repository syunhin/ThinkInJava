package 线程的基本定义与操作.捕获异常;

public class UnCatchException {
    public static void main(String[] args) {
        try {
            new Runnable() {
                private Thread t;

                {
                    t = new Thread(this);
                    t.start();
                }

                @Override
                public void run() {
                    throw new RuntimeException("运行时异常");
                }
            };
        } catch (RuntimeException e) {
            System.out.println("捕获了运行时异常");
        }
    }
}
