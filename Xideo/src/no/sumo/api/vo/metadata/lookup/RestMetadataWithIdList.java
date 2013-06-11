package no.sumo.api.vo.metadata.lookup;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Root;

@Root( name = "metadataLookupResultList" )
public class RestMetadataWithIdList {

	private List<RestMetadataWithId> metadataWithId;

	public RestMetadataWithIdList( List<RestMetadataWithId> metadataWithId ) {
		this.metadataWithId = metadataWithId;
	}

	public RestMetadataWithIdList() {
		metadataWithId = new ArrayList<RestMetadataWithId>();
	}

	public List<RestMetadataWithId> getMetadataWithId() {
		return metadataWithId;
	}

	public void setMetadataWithId( List<RestMetadataWithId> metadataWithId ) {
		this.metadataWithId = metadataWithId;
	}
}
