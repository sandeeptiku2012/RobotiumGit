package no.sumo.api.vo;

import org.simpleframework.xml.Attribute;

public class RestObject implements IRestObject {
	@Attribute( name = "uri", required = false )
	private String uri;

	@Override
	public String getUri() {
		if( uri == null ) {
			uri = "";
		}
		return uri;
	}
}
