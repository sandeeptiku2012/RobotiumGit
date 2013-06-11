package no.sumo.api.vo.asset.item;

import java.text.DecimalFormat;
import java.util.Date;

import no.sumo.api.contracts.IAssetItem;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "item" )
@Default( DefaultType.FIELD )
public class RestAssetItem extends RestObject implements IAssetItem {
	@Attribute
	Long id;

	@Transient
	private Long assetId;

	private Date dateBegin;
	private Date dateEnd;

	@Element( name = "asset" )
	private RestObject assetUri;

	private String picture;
	private String text;
	private Double timeBegin;
	private Double timeEnd;
	private String title;
	private String categoryName;
	private Long categoryId;

	public RestAssetItem() {
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

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( Long categoryId ) {
		this.categoryId = categoryId;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture( String picture ) {
		this.picture = picture;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId( Long assetId ) {
		this.assetId = assetId;
	}

	public String getText() {
		return text;
	}

	public void setText( String text ) {
		this.text = text;
	}

	public Double getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin( Double timeBegin ) {
		this.timeBegin = timeBegin;
	}

	public Double getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd( Double timeEnd ) {
		this.timeEnd = timeEnd;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateBegin( Date dateBegin ) {
		this.dateBegin = dateBegin;
	}

	public void setDateEnd( Date dateEnd ) {
		this.dateEnd = dateEnd;
	}

	@Element( name = "duration" )
	public Double getDuration() {
		if( getTimeBegin() != null && getTimeEnd() != null ) {
			Double diff = getTimeEnd() - getTimeBegin();
			DecimalFormat format = new DecimalFormat( "#.##" );
			String formatted = format.format( diff );

			return Double.valueOf( formatted.replace( ',', '.' ) );
		}

		return null;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName( String categoryName ) {
		this.categoryName = categoryName;
	}
}
