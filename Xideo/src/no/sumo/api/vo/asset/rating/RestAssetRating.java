package no.sumo.api.vo.asset.rating;

import java.util.Date;

import no.sumo.api.contracts.IAssetRating;
import no.sumo.api.entity.sumo.enums.ProgramRatingType;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "rating" )
public class RestAssetRating extends RestObject implements IAssetRating {
	@Attribute( required = false )
	private Long id;

	@Transient
	private Long assetId;

	@Element( name = "userId", required = false )
	private Long memberId;

	@Element( name = "asset", required = false )
	private RestObject assetUri;

	private String nickname;

	@Element( name = "rating", required = false )
	private ProgramRatingType rating;

	@Element( name = "crumb", required = false )
	private String crumb;
	private Date registered;

	public RestAssetRating() {
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

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId( Long assetId ) {
		this.assetId = assetId;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId( Long memberId ) {
		this.memberId = memberId;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname( String nickname ) {
		this.nickname = nickname;
	}

	public ProgramRatingType getRating() {
		return rating;
	}

	public void setRating( ProgramRatingType rating ) {
		this.rating = rating;
	}

	public String getCrumb() {
		return crumb;
	}

	public void setCrumb( String crumb ) {
		this.crumb = crumb;
	}

	public Date getRegistered() {
		return registered;
	}

	public void setRegistered( Date registered ) {
		this.registered = registered;
	}
}
