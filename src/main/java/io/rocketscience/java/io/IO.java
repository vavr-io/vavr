package io.rocketscience.java.io;

import io.rocketscience.java.util.Failure;
import io.rocketscience.java.util.Success;
import io.rocketscience.java.util.Try;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

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
	public static Try<byte[]> toBytes(InputStream in) {
		try (InputStream source = in) {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final byte[] buf = new byte[4096];
			int read;
			while ((read = source.read(buf)) != -1) {
				out.write(buf, 0, read);
			}
			return new Success<>(out.toByteArray());
		} catch (IOException x) {
			return new Failure<>(x);
		}
	}
	
	/**
	 * Reads a system reosurce and returns the content as byte[].
	 * 
	 * @param resource A system resource path.
	 * @return The content of resource.
	 */
	public static Try<byte[]> resourceToBytes(String resource) {
		final InputStream in = ClassLoader.getSystemResourceAsStream(resource);
		return toBytes(in);
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
	public static Try<String> toString(InputStream in, Charset charset) {
		return toBytes(in).map(bytes -> new String(bytes, charset));
	}

	/**
	 * Reads a system reosurce and returns the content as String using a specific Charset to encode the bytes.<br>
	 * 
	 * @param resource A system resource path.
	 * @param charset Charset used to convert stream of bytes to String.
	 * @return The content of resource.
	 */
	public static Try<String> resourceToString(String resource, Charset charset) {
		final InputStream in = ClassLoader.getSystemResourceAsStream(resource);
		return toString(in, charset);
	}
	
}
