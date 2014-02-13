package io.rocketscience.javax.xml;

import static javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.function.Function;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.w3c.dom.ls.LSResourceResolver;
import org.xml.sax.InputSource;

/**
 * Validate xml based on a specific schema, where included schemas are searched in classpath resource
 * <code>schema/calypsoml/</code>.
 */
public class XmlValidator {

	private final Validator validator;

	// TODO: do we need a charset provider? should the XmlResourceResolver look for xml encoding="..." instead?
	public XmlValidator(InputStream schema, Function<String, byte[]> resourceLoader,
			Function<byte[], Charset> charsetProvider) throws Exception {
		final SchemaFactory factory = SchemaFactory.newInstance(W3C_XML_SCHEMA_NS_URI);
		final LSResourceResolver resourceResolver = new XmlResourceResolver(resourceLoader, charsetProvider);
		factory.setResourceResolver(resourceResolver);
		validator = factory.newSchema(getSource(schema)).newValidator();
	}

	public void validate(InputStream xml) throws Exception {
		validator.validate(getSource(xml));
	}

	/**
	 * Returns a SAXSource based on an InputStream.
	 * 
	 * @param string
	 * @return
	 * @see #getResult()
	 */
	private Source getSource(InputStream in) {
		return new SAXSource(new InputSource(in));
	}

}
