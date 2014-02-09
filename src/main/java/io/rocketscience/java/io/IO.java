package io.rocketscience.java.io;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Optional;

public final class IO {

	private IO() {
        throw new AssertionError(IO.class.getName() + " cannot be instantiated.");
    }
	
	public static Optional<byte[]> toBytes(InputStream source) {
		try(InputStream in = source) {
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			final byte[] buf = new byte[4096];
			int read;
			while((read = in.read(buf)) != -1) {
				out.write(buf, 0, read);
			}
			return Optional.of(out.toByteArray());
		} catch (IOException e) {
			return Optional.empty();
		} 
	}

	public static Optional<String> toString(InputStream in, Charset charset) {
		return toBytes(in).map(bytes -> new String(bytes, charset));
	}

}
