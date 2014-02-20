package javaslang.util;

import java.util.Timer;
import java.util.TimerTask;

public final class Timers {
	
	private Timers() {
		throw new AssertionError(Timers.class.getName() + " cannot be instantiated.");
	}
	
	public static Timer schedule(Runnable task, long delay) {
		final Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				task.run();
			}
		}, delay);
		return timer;
	}

}
