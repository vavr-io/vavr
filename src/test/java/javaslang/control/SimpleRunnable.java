package javaslang.control;

public class SimpleRunnable implements Runnable {
    public boolean executed;

    @Override
    public void run() {
        this.executed=true;
    }
}
