package no.sumo.api.vo.favorite;

import java.util.Date;

import no.sumo.api.contracts.IFavorite;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "favorite" )
public class RestFavorite extends RestObject implements IFavorite {

	@Attribute
	private Long id;

	@Element( name = "memberId" )
	private Long memberId;

	@Element( name = "category" )
	private RestObject categoryUri = new RestObject();

	@Element( name = "treeId" )
	private Long treeId;

	@Element( name = "created" )
	private Date created;

	public Date getCreated() {
		return this.created;
	}

	public Long getId() {
		return this.id;
	}

	public Long getMemberId() {
		return this.memberId;
	}

	public Long getTreeId() {
		return this.treeId;
	}

	public void setCreated( Date created ) {
		this.created = created;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public void setMemberId( Long memberId ) {
		this.memberId = memberId;
	}

	public void setTreeId( Long treeId ) {
		this.treeId = treeId;

	}

	public RestObject getCategoryUri() {
		return categoryUri;
	}
}
