package no.sumo.api.vo.metadata;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

public class RestMetadataConverter implements Converter<RestMetadata> {

	@Override
	public RestMetadata read( InputNode node ) throws Exception {
		RestMetadata result = new RestMetadata();
		InputNode child = node.getNext();
		while( child != null ) {
			result.put( child.getName(), child.getValue() );
			child = node.getNext();
		}
		return result;
	}

	@Override
	public void write( OutputNode outputNode, RestMetadata object ) throws Exception {
		throw new UnsupportedOperationException();
	}
}
