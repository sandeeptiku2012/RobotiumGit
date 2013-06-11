package no.sumo.api.vo.metadata.lookup;

import no.sumo.api.vo.metadata.RestMetadata;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root( name = "metadataWithId" )
@Default( DefaultType.FIELD )
public class RestMetadataWithId extends RestMetadata {

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}
}
