package io.rocketscience.javax.xml;

import static io.rocketscience.java.lang.Lang.require;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import org.w3c.dom.ls.LSInput;
import org.w3c.dom.ls.LSResourceResolver;

public class XmlResourceResolver implements LSResourceResolver {

	private static final String SLASH = "/";

	private final Set<String> visited = new HashSet<String>();
	private final Map<String, String> ns2path = new HashMap<String, String>();
	private final Function<String, byte[]> resourceProvider;
	private final Function<byte[], Charset> charsetProvider;

	public XmlResourceResolver(Function<String, byte[]> resourceProvider, Function<byte[], Charset> charsetProvider) {
		this.resourceProvider = resourceProvider;
		this.charsetProvider = charsetProvider;
	}

	@Override
	public LSInput resolveResource(String type, String namespaceURI, String publicId, String systemId, String baseURI) {
		require(systemId != null, "systemId is null"); // TODO: use publicId instead of systemId? when is which defined?
		final String path = getPath(ns2path, namespaceURI, systemId);
		final String file = extractFile(systemId);
		final String resource = path + SLASH + file;
		if (visited.contains(resource)) {
			return null;
		} else {
			visited.add(resource);
			final byte[] data = resourceProvider.apply(resource);
			final Charset charset = charsetProvider.apply(data);
			return new XmlInput(data, charset, publicId, systemId, baseURI);
		}
	}

	private String getPath(Map<String, String> ns2path2, String namespaceURI, String systemId) {
		if (ns2path.containsKey(namespaceURI)) {
			return ns2path.get(namespaceURI);
		} else {
			final String path = extractPath(systemId);
			ns2path.put(namespaceURI, path);
			return path;
		}
	}

	private String extractPath(String systemId) {
		final int index = systemId.lastIndexOf(SLASH);
		return (index != -1) ? systemId.substring(0, index) : "";
	}

	private String extractFile(String systemId) {
		final int index = systemId.lastIndexOf(SLASH);
		return (index != -1) ? systemId.substring(index + 1) : systemId;
	}

}
