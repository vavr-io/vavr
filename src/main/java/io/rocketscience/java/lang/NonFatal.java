package io.rocketscience.java.lang;

/**
 * Use {@link Thrown#of(Throwable)} to create instances of {@link Fatal} and {@link NonFatal}.
 */
public class NonFatal extends Thrown {

	private static final long serialVersionUID = 1L;

	private final Throwable t;

	NonFatal(Throwable t) {
		this.t = t;
	}

	@Override
	public Throwable get() {
		return t;
	}

	@Override
	public boolean isFatal() {
		return false;
	}

}
