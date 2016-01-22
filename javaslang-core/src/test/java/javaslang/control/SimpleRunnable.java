package javaslang.control;

class SimpleRunnable implements Runnable {

    public boolean executed;

    @Override
    public void run() {
        this.executed = true;
    }
}
