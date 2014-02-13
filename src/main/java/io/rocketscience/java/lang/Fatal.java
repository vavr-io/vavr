package io.rocketscience.java.lang;

/**
 * Use {@link Thrown#of(Throwable)} to create instances of {@link Fatal} and {@link NonFatal}.
 */
public class Fatal extends Thrown {

	private static final long serialVersionUID = 1L;

	final Throwable t;

	Fatal(Throwable t) {
		this.t = t;
	}

	@Override
	public Throwable get() {
		return t;
	}

	@Override
	public boolean isFatal() {
		return true;
	}

}
