package no.sumo.api.vo.contentpanel;

import no.sumo.api.contracts.IContentPanelElement;
import no.sumo.api.vo.RestObject;
import no.sumo.api.vo.asset.RestSearchAsset;
import no.sumo.api.vo.category.RestSearchCategory;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author Tor Erik (tea@vimond.com)
 */
@Root( name = "contentPanelElement" )
public class RestContentPanelElement extends RestObject implements IContentPanelElement {
	@Attribute( name = "id" )
	private Long id;

	@Element( name = "panelName", required = false )
	private String panelName;

	@Element( name = "panelType", required = false )
	private String panelType;

	@Element( name = "sortIndex", required = false )
	private Integer sortIndex;

	@Element( name = "contentType", required = false )
	private String contentType;

	@Element( name = "contentKey", required = false )
	private String contentKey;

	@Element( name = "itemsCount", required = false )
	private Integer itemsCount;

	@Element( name = "title", required = false )
	private String title;

	@Element( name = "text", required = false )
	private String text;

	private String url;

	@Element( name = "imageUrl", required = false )
	private String imageUrl;

	private String imageSize;

	@Element( name = "imagePackId", required = false )
	private String imagePackId;

	@Element( name = "category", required = false )
	private RestSearchCategory category;

	@Element( name = "asset", required = false )
	private RestSearchAsset asset;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public RestSearchAsset getAsset() {
		return asset;
	}

	public void setAsset( RestSearchAsset asset ) {
		this.asset = asset;
	}

	public RestSearchCategory getCategory() {
		return category;
	}

	public void setCategory( RestSearchCategory category ) {
		this.category = category;
	}

	public String getPanelName() {
		return panelName;
	}

	public void setPanelName( String panelName ) {
		this.panelName = panelName;
	}

	public String getPanelType() {
		return panelType;
	}

	public void setPanelType( String panelType ) {
		this.panelType = panelType;
	}

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex( Integer sortIndex ) {
		this.sortIndex = sortIndex;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType( String contentType ) {
		this.contentType = contentType;
	}

	public String getContentKey() {
		return contentKey;
	}

	public void setContentKey( String contentKey ) {
		this.contentKey = contentKey;
	}

	public Integer getItemsCount() {
		return itemsCount;
	}

	public void setItemsCount( Integer elementCount ) {
		itemsCount = elementCount;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText( String text ) {
		this.text = text;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl( String url ) {
		this.url = url;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl( String imageUrl ) {
		this.imageUrl = imageUrl;
	}

	public String getImageSize() {
		return imageSize;
	}

	public void setImageSize( String imageSize ) {
		this.imageSize = imageSize;
	}

	public String getImagePackId() {
		return imagePackId;
	}

	public void setImagePackId( String imagePackId ) {
		this.imagePackId = imagePackId;
	}
}
