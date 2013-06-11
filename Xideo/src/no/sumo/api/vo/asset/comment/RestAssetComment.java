package no.sumo.api.vo.asset.comment;

import java.util.Date;

import no.sumo.api.contracts.IAssetComment;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "comment" )
@Default( DefaultType.FIELD )
public class RestAssetComment extends RestObject implements IAssetComment {
	@Attribute
	private Long id;

	@Transient
	private Long assetId;
	@Transient
	private Long memberId;

	@Element( name = "asset" )
	private RestObject assetUri;

	private String nickname;
	private String comment;
	private String crumb;
	private Date registered;

	public RestAssetComment() {
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

	public String getComment() {
		return comment;
	}

	public void setComment( String comment ) {
		this.comment = comment;
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
