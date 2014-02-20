package javaslang.lang;

/**
 * Use {@link Cause#of(Throwable)} to create instances of {@link Fatal} and {@link NonFatal}.
 */
public class NonFatal extends Cause {

	private static final long serialVersionUID = 1L;

	NonFatal(Throwable cause) {
		super(cause);
	}

	@Override
	public boolean isFatal() {
		return false;
	}

}
