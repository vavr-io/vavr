package javaslang.xml;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.w3c.dom.ls.LSInput;

public class XmlInput implements LSInput {

	private final byte[] data;
	private final String charset;
	private final String publicId;
	private final String systemId;
	private final String baseURI;

	XmlInput(byte[] data, Charset charset, String publicId, String systemId, String baseURI) {
		this.data = data;
		this.charset = charset.name();
		this.publicId = publicId;
		this.systemId = systemId;
		this.baseURI = baseURI;
	}

	@Override
	public Reader getCharacterStream() {
		try {
			return new StringReader(new String(data, charset));
		} catch(UnsupportedEncodingException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public void setCharacterStream(Reader characterStream) {
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getByteStream() {
		return new ByteArrayInputStream(data);
	}

	@Override
	public void setByteStream(InputStream byteStream) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getStringData() {
		try {
			return new String(data, charset);
		} catch(UnsupportedEncodingException x) {
			throw new RuntimeException(x);
		}
	}

	@Override
	public void setStringData(String stringData) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getSystemId() {
		return systemId;
	}

	@Override
	public void setSystemId(String systemId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getPublicId() {
		return publicId;
	}

	@Override
	public void setPublicId(String publicId) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getBaseURI() {
		return baseURI;
	}

	@Override
	public void setBaseURI(String baseURI) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getEncoding() {
		return charset;
	}

	@Override
	public void setEncoding(String encoding) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getCertifiedText() {
		return true;
	}

	@Override
	public void setCertifiedText(boolean certifiedText) {
		throw new UnsupportedOperationException();
	}

}
