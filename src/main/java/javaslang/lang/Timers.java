package javaslang.lang;

import java.util.Timer;
import java.util.TimerTask;

public final class Timers {

	private Timers() {
		throw new AssertionError(Timers.class.getName() + " cannot be instantiated.");
	}

	/**
	 * Syntactic sugar, allows to call
	 * 
	 * <pre>
	 * <code>
	 * final Timer timer = Timers.schedule(() -&gt; println("hi"), 1000)
	 * </code>
	 * </pre>
	 * 
	 * instead of
	 * 
	 * <pre>
	 * <code>
	 * final Timer timer = new Timer();
	 * timer.schedule(new TimerTask() {
	 *     &#64;Override
	 *     public void run() {
	 *         println("hi");
	 *     }
	 * }, 1000);
	 * </code>
	 * </pre>
	 * 
	 * @param task A Runnable
	 * @param delay A delay in milliseconds
	 * @return A Timer
	 */
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
