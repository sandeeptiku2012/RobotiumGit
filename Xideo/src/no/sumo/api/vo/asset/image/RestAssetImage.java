package no.sumo.api.vo.asset.image;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "image" )
@Default( DefaultType.FIELD )
public class RestAssetImage {
	@Element( name = "url", required = false )
	private String url;

	@Attribute( required = false )
	private Integer width;

	@Attribute( required = false )
	private Integer height;

	@Attribute( required = false )
	private String type;

	public void setWidth( Integer width ) {
		this.width = width;
	}

	public Integer getWidth() {
		return width;
	}

	public void setUrl( String url ) {
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setHeight( Integer height ) {
		this.height = height;
	}

	public Integer getHeight() {
		return height;
	}

	public void setType( String type ) {
		this.type = type;
	}

	public String getType() {
		return type;
	}
}
