package no.sumo.api.vo.asset.playback;

import java.util.Date;

import no.sumo.api.contracts.IPlaybackLogData;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root( name = "playback" )
@Default( DefaultType.FIELD )
public class RestPlaybackLogData extends RestObject implements IPlaybackLogData {
	private Long assetId;
	private boolean isLive;
	private Long memberId;
	private Long memberAccessId;
	private Long fileId;
	private Long categoryId;
	private Integer platformId;
	private String ipAddress;
	private Date expires;

	public RestPlaybackLogData() {
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId( Long assetId ) {
		this.assetId = assetId;
	}

	public boolean isLive() {
		return isLive;
	}

	public void setLive( boolean isLive ) {
		this.isLive = isLive;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId( Long memberId ) {
		this.memberId = memberId;
	}

	public Long getMemberAccessId() {
		return memberAccessId;
	}

	public void setMemberAccessId( Long memberAccessId ) {
		this.memberAccessId = memberAccessId;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId( Long fileId ) {
		this.fileId = fileId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( Long categoryId ) {
		this.categoryId = categoryId;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId( Integer platformId ) {
		this.platformId = platformId;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress( String ipAddress ) {
		this.ipAddress = ipAddress;
	}

	public Date getExpiry() {
		return expires;
	}

	public void setExpiry( Date expires ) {
		this.expires = expires;
	}
}
