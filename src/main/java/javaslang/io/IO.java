/**    / \____  _    ______   _____ / \____   ____  _____
 *    /  \__  \/ \  / \__  \ /  __//  \__  \ /    \/ __  \   Javaslang
 *  _/  // _\  \  \/  / _\  \\_  \/  // _\  \  /\  \__/  /   Copyright 2014 Daniel Dietrich
 * /___/ \_____/\____/\_____/____/\___\_____/_/  \_/____/    Licensed under the Apache License, Version 2.0
 */
package javaslang.io;

import static javaslang.Lang.requireNotInstantiable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import javaslang.exception.Failure;
import javaslang.exception.Success;
import javaslang.exception.Try;

/**
 * The IO class provides convenient methods for loading system resources and converting data between
 * <code>InputStream</code>, <code>byte[]</code> and <code>String</code>.
 * <p>
 * When converting <code>bytes</code> to <code>String</code>, the charset has to be provided explicitly. Most
 * encoding-related errors occur, because the default charset is used. This may produce unwanted side-effects when
 * relying on a specific encoding. Please consider this as a good example, where <em>coding by convention</em> does
 * <strong>not</strong> apply.
 * <p>
 * Also it is strongly encouraged to use {@link IO#UTF8} as charset for encoding Strings, therefore this is the only
 * charset constant defined in the <code>IO</code> class. Working with charsets works best if the encoding is used
 * consistently throughout the whole code base of the underlying project. E.g. Maven projects set the encoding in the
 * pom.xml similar to this:
 * 
 * <pre>
 * <code>
 * &lt;properties&gt;
 *     &lt;project.build.sourceEncoding&gt;UTF-8&lt;/project.build.sourceEncoding&gt;
 * &lt;/properties&gt;
 * </code>
 * </pre>
 * 
 * This sounds as a matter of course but many projects are missing the encoding.
 */
public final class IO {

	/** It is encouraged here to use a unified charset. */
	public static final Charset UTF8 = Charset.forName("UTF-8");

	/**
	 * This class is not intended to be instantiated.
	 */
	private IO() {
		requireNotInstantiable();
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
	 * Converts a String to an InputStream using a specific Charset to decode the bytes.
	 * <p>
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
	 * Convert an InputStream to String using a specific Charset to encode the bytes.
	 * <p>
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
	 * Reads a system reosurce and returns the content as byte[].
	 * 
	 * @param resource A system resource path.
	 * @return The content of resource.
	 */
	public static Try<byte[]> loadResource(String resource) {
		final InputStream in = ClassLoader.getSystemResourceAsStream(resource);
		return toBytes(in);
	}

	/**
	 * Reads a system reosurce and returns the content as String using a specific Charset to encode the bytes.
	 * 
	 * @param resource A system resource path.
	 * @param charset Charset used to convert stream of bytes to String.
	 * @return The content of resource.
	 */
	public static Try<String> loadResource(String resource, Charset charset) {
		final InputStream in = ClassLoader.getSystemResourceAsStream(resource);
		return toString(in, charset);
	}

}
