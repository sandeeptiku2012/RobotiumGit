package no.sumo.api.vo.asset.relations;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root( name = "assetRelationType" )
@Default( DefaultType.FIELD )
public class RestAssetRelationType extends RestObject {

	private Long id;
	private String name;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}
}
