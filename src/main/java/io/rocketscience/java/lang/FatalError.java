package io.rocketscience.java.lang;

/**
 * An unchecked wrapper for fatal throwables.
 */
public class FatalError extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public FatalError(Throwable t) {
		super(t);
	}

}
