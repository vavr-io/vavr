package io.rocketscience.javax.xml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

public final class Xml {

	private Xml() {
		throw new AssertionError(Xml.class.getName() + " cannot be instantiated.");
	}
	
	/**
	 * Convenience method for chaining XmlFilters. Effectively returns the lambda <code>xml -> Xml.sort(xml)</code>.
	 * 
	 * @return A XmlFilter which sorts xml.
	 */
	public static XmlFilter getSorter() {
		return xml -> sort(xml);
	}
	
	public static byte[] sort(byte[] xml) {
		return null; // TODO
	}
	
	/*
	 * TODO: parse() and format() xsd datetime.
	 * 
	 * DateTimeFormatterBuilder formatterBuilder = new DateTimeFormatterBuilder();
	 * formatterBuilder.append(DateTimeFormatter.ISO_LOCAL_DATE_TIME) .appendLiteral("-") .appendZoneOrOffsetId();
	 * DateTimeFormatter formatter = formatterBuilder.toFormatter();
	 * System.out.println(formatter.format(ZonedDateTime.now()));
	 */

	public static void transform(InputStream in, InputStream xslt, OutputStream out) {
		try(InputStream source = in; InputStream transformation = xslt; OutputStream target = out) {
			final Source xmlSource = new StreamSource(source);
			final Source xsltSource = new StreamSource(transformation);
			final Result outputTarget = new StreamResult(target);
			final TransformerFactory transformerFactory = TransformerFactory.newInstance();
			final Transformer transformer = transformerFactory.newTransformer(xsltSource);
			transformer.transform(xmlSource, outputTarget);
		} catch(TransformerException x) {
			throw new RuntimeException("error transforming xml", x);
		} catch (IOException x) {
			// error closing resources. out should contain result anyway.
		}
	}
	
	// TODO: public static class SorterBuilder { ... }
	
	// TODO: public static class ValidationBuilder { ... }
	

	/**
	 * Implementation of a filter function which mutates xml elements and attributes of a given xml document.
	 */
	public static class XsltBuilder {

		private final StringBuilder builder = new StringBuilder();

		public XsltBuilder(String charset) {
			builder.append(
					"<xsl:stylesheet version=\"1.0\" xmlns:xsl=\"http://www.w3.org/1999/XSL/Transform\" xmlns:calypso=\"http://www.calypso.com/xml\">\n")//
					.append("  <xsl:output encoding=\"" + charset + "\"/>\n")//
					.append("  <xsl:template match=\"node()|@*\">\n")//
					.append("    <xsl:copy>\n")//
					.append("      <xsl:apply-templates select=\"node()|@*\"/>\n")//
					.append("    </xsl:copy>\n")//
					.append("  </xsl:template>\n");
		}

		public XmlFilter build() {
			final String transformation = builder.toString() + "</xsl:stylesheet>";
			return xml -> {
				final InputStream in = new ByteArrayInputStream(xml);
				final InputStream xslt = new ByteArrayInputStream(transformation.getBytes());
				final ByteArrayOutputStream out = new ByteArrayOutputStream();
				Xml.transform(in, xslt, out);
				return out.toByteArray();
			};
		}

		public XsltBuilder setAttribute(String xpath, String name, String value) {
			builder.append("  <xsl:template match=\"" + xpath + "/@" + name + "\">\n")//
					.append("    <xsl:attribute name=\"" + name + "\">" + value + "</xsl:attribute>\n")//
					.append("  </xsl:template>\n");
			return this;
		}

		public XsltBuilder removeAttribute(String xpath, String name) {
			builder.append("  <xsl:template match=\"" + xpath + "/@" + name + "\"/>\n");
			return this;
		}

		// TODO: match all text() children or just first (if possible with xpath)?
		public XsltBuilder setElementText(String xpath, String value) {
			builder.append("  <xsl:template match=\"" + xpath + "/text()\">\n")//
					.append("    <xsl:text>" + value + "</xsl:text>\n")//
					.append("  </xsl:template>\n");
			return this;
		}

		public XsltBuilder setElement(String xpath, String value) {
			builder.append("  <xsl:template match=\"" + xpath + "\">\n")//
					.append("     " + value + "\n")//
					.append("  </xsl:template>\n");
			return this;
		}

		public XsltBuilder removeElement(String xpath) {
			builder.append("  <xsl:template match=\"" + xpath + "\"/>\n");
			return this;
		}
	}
	
}
