package javaslang.lang;

/**
 * Use {@link Cause#of(Throwable)} to create instances of {@link Fatal} and {@link NonFatal}.
 */
public class Fatal extends Cause {

	private static final long serialVersionUID = 1L;

	Fatal(Throwable cause) {
		super(cause);
	}

	@Override
	public boolean isFatal() {
		return true;
	}

}
