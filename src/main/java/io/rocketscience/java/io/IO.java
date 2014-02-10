package io.rocketscience.java.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Optional;

public final class IO {
	
	/** It is encouraged here to usage a unified charset. */
	public static final Charset UTF8 = Charset.forName("UTF-8");

	private IO() {
		throw new AssertionError(IO.class.getName() + " cannot be instantiated.");
	}

	/**
	 * Converts a byte array to an InputStream.
	 * 
	 * @param bytes A byte arrays
	 * @return An InputStream of bytes.
	 */
	public static InputStream toInputStream(byte[] bytes) {
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * Converts a String to an InputStream using a specific Charset to decode the bytes.<br>
	 * <br>
	 * There will be no <code>toInputStream(String)</code> method in order to enforce correctness of charset encoding.
	 * 
	 * @param s A String
	 * @param charset A Charset
	 * @return An InputStream of s using charset.
	 */
	public static InputStream toInputStream(String s, Charset charset) {
		return new ByteArrayInputStream(s.getBytes(charset));
	}

	/**
	 * Convert an InputStream to byte[].
	 * 
	 * @param in Input
	 * @return An Optional containing the bytes of in or empty if the input could not be read.
	 */
	// TODO: return Either<byte[], String> instead of Optional
	public static Optional<byte[]> toBytes(InputStream in) {
		try (InputStream source = in) {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final byte[] buf = new byte[4096];
			int read;
			while ((read = source.read(buf)) != -1) {
				out.write(buf, 0, read);
			}
			return Optional.of(out.toByteArray());
		} catch (IOException e) {
			return Optional.empty();
		}
	}

	/**
	 * Convert an InputStream to String using a specific Charset to encode the bytes.<br>
	 * <br>
	 * There will be no <code>toString(InputStream)</code> method in order to enforce correctness of charset encoding.
	 * 
	 * @param in Input
	 * @param charset Charset used to convert stream of bytes to String.
	 * @return An Optional of encoded String, empty if the input could not be read.
	 */
	// TODO: return Either<String, String> instead of Optional
	public static Optional<String> toString(InputStream in, Charset charset) {
		return toBytes(in).map(bytes -> new String(bytes, charset));
	}

}
