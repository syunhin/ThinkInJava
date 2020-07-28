package 用户界面响应;

import java.io.IOException;

class UnResponsiveUI {
    private volatile double d = 1;

    public UnResponsiveUI() throws IOException {
        while (d > 0) {
            d = d + (Math.PI + Math.E) / d;
        }
        System.in.read();
    }
}

public class ResponsiveUI extends Thread {
    private static volatile double d = 1;

    public ResponsiveUI() {
        this.setDaemon(false);
        start();
    }

    @Override
    public void run() {
        while (true) {
            d = d + (Math.E + Math.PI) / d;
        }
    }

    public static void main(String[] args) throws IOException {
        //new UnResponsiveUI();
        new ResponsiveUI();
        System.in.read();
        System.out.println(d);
    }
}
