package no.sumo.api.vo.asset.item;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "items" )
public class RestAssetItemList {
	private String uri;
	private List<RestAssetItem> items = new ArrayList<RestAssetItem>();
	private String numberOfHits;

	public RestAssetItemList() {
	}

	public RestAssetItemList( List<RestAssetItem> items ) {
		this.items = items;
	}

	@Attribute
	public String getUri() {
		return uri;
	}

	public void setUri( String uri ) {
		this.uri = uri;
	}

	@Element( name = "item" )
	public List<RestAssetItem> getItems() {
		return items;
	}

	@Element( name = "numberOfHits" )
	public String getNumberOfHits() {
		return numberOfHits;
	}

	public void setNumberOfHits( String numberOfHits ) {
		this.numberOfHits = numberOfHits;
	}

}
