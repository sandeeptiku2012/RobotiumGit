package no.sumo.api.vo.asset.property;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "properties" )
public class RestAssetPropertyList {
	private String uri;
	private List<RestAssetProperty> properties = new ArrayList<RestAssetProperty>();

	public RestAssetPropertyList() {
	}

	public RestAssetPropertyList( List<RestAssetProperty> properties ) {
		this.properties = properties;
	}

	@Attribute
	public String getUri() {
		return uri;
	}

	public void setUri( String uri ) {
		this.uri = uri;
	}

	@Element( name = "property" )
	public List<RestAssetProperty> getProperties() {
		return properties;
	}
}
