package no.sumo.api.vo.asset;

import java.util.Date;
import java.util.List;

import no.sumo.api.contracts.ConversionFactory;
import no.sumo.api.contracts.IAsset;
import no.sumo.api.contracts.IPlatformPublishInfo;
import no.sumo.api.vo.RestObject;
import no.sumo.api.vo.asset.image.RestAssetImageList;
import no.sumo.api.vo.metadata.RestMetadata;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "asset" )
@Default( value = DefaultType.FIELD, required = false )
public class RestAsset extends RestObject implements IAsset {
	@Attribute( name = "id", required = false )
	private Long id;

	@Attribute( name = "categoryId", required = true )
	private Long categoryId;

	@Attribute( name = "channelId", required = false )
	private Long channelId;

	@Element( name = "views", required = false )
	private Long viewCount;

	@Element( name = "voteAverage", required = false )
	private Long voteAverage;

	@Element( name = "voteAverageDouble", required = false )
	private Double voteAverageDouble;

	@Element( name = "voteCount", required = false )
	private Long voteCount;

	@Element( name = "categoryPath", required = false )
	private String categoryPath;

	@Transient
	private Boolean isItemsPublished;

	@Transient
	private RestObject itemsUri;

	@Transient
	private RestObject commentsUri;

	@Transient
	private RestObject ratingsUri;

	@Transient
	private Date encodingStart;

	@Transient
	private Date encodingEnd;

	@Element( name = "aspect16x9", required = false )
	private Boolean isAspect16x9;

	@Element( name = "drmProtected", required = false )
	private Boolean drmProtected;

	@Element( name = "live", required = false )
	private Boolean isLive;

	@Element( name = "category", required = false )
	private RestObject categoryUri;

	@Element( name = "products", required = false )
	private RestObject productsUri;

	@Element( name = "playback", required = false )
	private RestObject playbackUri;

	@Element( name = "metadata", required = false )
	private RestMetadata metadata;

	/**
	 * Should not be exposed in the API
	 */
	@Deprecated
	@Transient
	private List<RestPlatformPublishInfo> publishInfo;

	@Element( name = "onDemandTimeBegin", required = false )
	private Double onDemandTimeBegin;

	@Element( name = "onDemandTimeEnd", required = false )
	private Double onDemandTimeEnd;

	@Element( name = "imageUrl", required = false )
	private String imageUrl;

	@Element( required = false )
	private RestAssetImageList imageVersions;

	@Element( name = "title", required = true )
	private String title;

	@Element( name = "liveBroadcastTime", required = false )
	private Date liveBroadcastTime;

	@Element( name = "expireDate", required = false )
	private Date expiryTime;

	private String description;

	@Element( name = "duration", required = false )
	private Long duration;

	@Element( name = "autoEncode", required = false )
	private Boolean autoEncode;

	private String keywords;

	private String subtitle;

	public RestAsset() {
	}

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Date getEncodingStart() {
		return encodingStart;
	}

	public void setEncodingStart( Date encodingStart ) {
		this.encodingStart = encodingStart;
	}

	public Date getEncodingEnd() {
		return encodingEnd;
	}

	public void setEncodingEnd( Date encodingEnd ) {
		this.encodingEnd = encodingEnd;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration( Long duration ) {
		this.duration = duration;
	}

	public Boolean getAutoEncode() {
		return autoEncode;
	}

	public void setAutoEncode( Boolean autoEncode ) {
		this.autoEncode = autoEncode;
	}

	public Boolean isAspect16x9() {
		if( isAspect16x9 == null ) {
			isAspect16x9 = false;
		}

		return isAspect16x9;
	}

	public void setAspect16x9( Boolean isAspect16x9 ) {
		this.isAspect16x9 = isAspect16x9;
	}

	public Boolean isDrmProtected() {
		return drmProtected;
	}

	public void setDrmProtected( Boolean drmProtected ) {
		this.drmProtected = drmProtected;
	}

	public Boolean isLive() {
		if( isLive == null ) {
			isLive = false;
		}

		return isLive;
	}

	public void setLive( Boolean isLive ) {
		this.isLive = isLive;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl( String imageUrl ) {
		this.imageUrl = imageUrl;
	}

	public RestAssetImageList getImageVersions() {
		return imageVersions;
	}

	public void setImageVersions( RestAssetImageList imageVersions ) {
		this.imageVersions = imageVersions;
	}

	public Date getLiveBroadcastTime() {
		return liveBroadcastTime;
	}

	public void setLiveBroadcastTime( Date liveBroadcastTime ) {
		this.liveBroadcastTime = liveBroadcastTime;
	}

	public Double getOnDemandTimeBegin() {
		if( onDemandTimeBegin == null ) {
			onDemandTimeEnd = 0.0D;
		}

		return onDemandTimeBegin;
	}

	public void setOnDemandTimeBegin( Double onDemandTimeBegin ) {
		this.onDemandTimeBegin = onDemandTimeBegin;
	}

	public Double getOnDemandTimeEnd() {
		if( onDemandTimeEnd == null ) {
			onDemandTimeEnd = 0.0D;
		}

		return onDemandTimeEnd;
	}

	public void setOnDemandTimeEnd( Double onDemandTimeEnd ) {
		this.onDemandTimeEnd = onDemandTimeEnd;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( Long categoryId ) {
		this.categoryId = categoryId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public void setChannelId( Long channelId ) {
		this.channelId = channelId;
	}

	public Date getExpiryTime() {
		return expiryTime;
	}

	public void setExpiryTime( Date expiryTime ) {
		this.expiryTime = expiryTime;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords( String keywords ) {
		this.keywords = keywords;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public void setSubtitle( String subtitle ) {
		this.subtitle = subtitle;
	}

	public RestObject getCategoryUri() {
		if( categoryUri == null ) {
			categoryUri = new RestObject();
		}

		return categoryUri;
	}

	@SuppressWarnings( "unused" )
	//	@Element( name = "items", required = false )
	@Deprecated
	private RestObject getItemsUriJAXB() {
		if( isItemsPublished() == null || !isItemsPublished() ) {
			return null;
		}

		return getItemsUri();
	}

	public RestObject getItemsUri() {
		if( itemsUri == null ) {
			itemsUri = new RestObject();
		}

		return itemsUri;
	}

	public RestObject getCommentsUri() {
		if( commentsUri == null ) {
			commentsUri = new RestObject();
		}

		return commentsUri;
	}

	public RestObject getRatingsUri() {
		if( ratingsUri == null ) {
			ratingsUri = new RestObject();
		}

		return ratingsUri;
	}

	public RestObject getProductUri() {
		if( productsUri == null ) {
			productsUri = new RestObject();
		}

		return productsUri;
	}

	public RestObject getPlaybackUri() {
		if( playbackUri == null ) {
			playbackUri = new RestObject();
		}

		return playbackUri;
	}

	public List<IPlatformPublishInfo> getPlatformPublishInfo() {
		return ConversionFactory.getProgramPublishedConverter().transfer( publishInfo );
	}

	/**
	 * Should not be exposed in the API
	 */
	public void setPlatformPublishInfo( List<IPlatformPublishInfo> publishInfo ) {
		this.publishInfo = ConversionFactory.getProgramPublishedConverter().transfer( publishInfo, RestPlatformPublishInfo.class );
	}

	public Boolean isItemsPublished() {
		return isItemsPublished;
	}

	public void setIsItemsPublished( Boolean isIndexed ) {
		isItemsPublished = isIndexed;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount( Long viewCount ) {
		this.viewCount = viewCount;
	}

	public RestMetadata getMetadata() {
		if( metadata == null ) {
			metadata = new RestMetadata();
		}

		return metadata;
	}

	public void setMetadata( RestMetadata metadata ) {
		this.metadata = metadata;
	}

	public Long getVoteAverage() {
		return voteAverage;
	}

	public void setVoteAverage( Long voteAverage ) {
		this.voteAverage = voteAverage;
	}

	public Double getVoteAverageDouble() {
		return voteAverageDouble;
	}

	public void setVoteAverageDouble( Double voteAverageDouble ) {
		this.voteAverageDouble = voteAverageDouble;
	}

	public Long getVoteCount() {
		return voteCount;
	}

	public void setVoteCount( Long voteCount ) {
		this.voteCount = voteCount;
	}

	public String getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath( String categoryPath ) {
		this.categoryPath = categoryPath;
	}

	public Long getProgramViewCount() {
		return viewCount;
	}

	public void setProgramViewCount( Long viewCount ) {
		this.viewCount = viewCount;
	}
}
