package no.sumo.api.vo.metadata;

import java.util.HashMap;
import java.util.Map;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Root;
import org.simpleframework.xml.convert.Convert;


/**
 * <p>
 * Generates xml with dynamic tag names. The values is backed by a
 * {@link Multimap}.
 * <p>
 * <p>
 * Generates the following xml output:
 * 
 * <pre>
 * {@code
 * <metadata>
 *     <title xml:lang="nb-no">Best uten ball</title>
 *     <title xml:lang="nn-no">Best utan ball</title>
 *     <title xml:lang="en-gb">Best without ball</title>
 * </metadata>
 * }
 * </pre>
 * 
 * </p>
 */
@Root( name = "metadata", strict = false )
@Convert( RestMetadataConverter.class )
public class RestMetadata extends RestObject {
	private final Map<String, String> properties = new HashMap<String, String>();

	public String get( String key ) {
		return properties.get( key );
	}

	public void put( String key, String value ) {
		properties.put( key, value );
	}

	public Map<String, String> getProperties() {
		return properties;
	}
}