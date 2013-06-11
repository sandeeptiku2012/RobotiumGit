package no.sumo.api.vo.asset.property;

import no.sumo.api.contracts.IAssetProperty;
import no.sumo.api.contracts.ProgramPropertyType;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "property" )
@Default( DefaultType.FIELD )
public class RestAssetProperty extends RestObject implements IAssetProperty {
	@Attribute
	private Long id;

	@Transient
	private Long assetId;

	@Element( name = "asset" )
	private RestObject assetUri;

	private ProgramPropertyType property;
	private String value;

	public RestAssetProperty() {
	}

	public RestObject getAssetUri() {
		if( assetUri == null ) {
			assetUri = new RestObject();
		}
		return assetUri;
	}

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId( Long assetId ) {
		this.assetId = assetId;
	}

	public ProgramPropertyType getProperty() {
		return property;
	}

	public void setProperty( ProgramPropertyType property ) {
		this.property = property;
	}

	public String getValue() {
		return value;
	}

	public void setValue( String value ) {
		this.value = value;
	}
}
