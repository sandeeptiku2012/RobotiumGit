package no.sumo.api.vo.platform;

import no.sumo.api.contracts.ConversionFactory;
import no.sumo.api.contracts.IPlatform;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

/**
 * Provides a REST value object for platform information.
 */
@Root( name = "platform" )
@Default( DefaultType.FIELD )
public class RestPlatform implements IPlatform {
	private Integer id;
	private String name;
	private String apiName;
	private String sectionName;

	public RestPlatform() {

	}

	public RestPlatform( String name ) {
		this( 0, name );
	}

	public RestPlatform( Integer platformId, String name ) {
		setId( platformId );
		setApiName( name );
		setName( name );
	}

	public Integer getId() {
		return id;
	}

	public void setId( Integer id ) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName( String apiName ) {
		this.apiName = apiName;

	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName( String sectionName ) {
		this.sectionName = sectionName;
	}

	public ApiPlatform asApiPlatform() {
		ApiPlatform platform = new ApiPlatform();

		ConversionFactory.getPlatformConverter().transfer( this, platform );

		return platform;
	}

	/**
	 * Returns the name of the platform
	 */
	@Override
	public String toString() {
		return getApiName();
	}
}
