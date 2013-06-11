package no.sumo.api.vo.asset;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root( name = "assetType" )
@Default( DefaultType.FIELD )
public class RestAssetType extends RestObject {

	private Long id;
	private String name;
	private boolean published;

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

	public boolean isPublished() {
		return published;
	}

	public void setPublished( boolean published ) {
		this.published = published;
	}
}
