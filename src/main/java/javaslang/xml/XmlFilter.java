package javaslang.xml;

/**
 * Definition of API to mutate xml.
 */
@FunctionalInterface
public interface XmlFilter {

	/**
	 * Apply filter(s) to an array of byte containing xml.
	 * 
	 * @param xml Xml data
	 * @return Filtered xml data
	 */
	byte[] apply(byte[] xml);
	
	/**
	 * Chaining filters.
	 * 
	 * @param next Next filter
	 * @return A new XmlFilter combining this and next.
	 */
	default XmlFilter andThen(XmlFilter next) {
		return xml -> next.apply(this.apply(xml));
	}
	
}
