package no.sumo.api.vo.asset;

import java.util.Date;

import no.sumo.api.contracts.IPlatformPublishInfo;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "publishing" )
@Default( DefaultType.FIELD )
public class RestPlatformPublishInfo extends RestObject implements IPlatformPublishInfo {
	@Transient
	private Long id;
	@Transient
	private Integer platformId;
	@Transient
	private Long assetId;

	@Element( name = "platform" )
	private String platformName;
	@Element( name = "live" )
	private Boolean livePublished;
	@Element( name = "onDemand" )
	private Boolean onDemandPublished;

	public static final Date DATE_EMPTY = new Date( 0L );

	@Element( name = "expire", required = true )
	private Date expire = null;

	@Element( name = "publish", required = true )
	private Date publish = null;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId( Integer platformId ) {
		this.platformId = platformId;
	}

	public Boolean getLivePublished() {
		return livePublished;
	}

	public void setLivePublished( Boolean livePublished ) {
		this.livePublished = livePublished;
	}

	public Boolean getOnDemandPublished() {
		return onDemandPublished;
	}

	public void setOnDemandPublished( Boolean onDemandPublished ) {
		this.onDemandPublished = onDemandPublished;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire( Date expire ) {
		this.expire = expire;
	}

	public Date getPublish() {
		return publish;
	}

	public void setPublish( Date publish ) {
		this.publish = publish;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId( Long assetId ) {
		this.assetId = assetId;
	}

	public String getPlatformName() {
		return platformName;
	}

	public void setPlatformName( String platformName ) {
		this.platformName = platformName;
	}
}
