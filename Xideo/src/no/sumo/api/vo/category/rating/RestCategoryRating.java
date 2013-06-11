package no.sumo.api.vo.category.rating;

import java.util.Date;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created with IntelliJ IDEA. User: ogi Date: 18.06.12 Time: 16:08 To change
 * this template use File | Settings | File Templates.
 */
@Root( name = "categoryRating" )
public class RestCategoryRating extends RestObject {
	@Attribute( required = false )
	private Long id;

	@Element( required = false )
	private Long categoryId;

	@Element( name = "category", required = false )
	private RestObject categoryUri;

	@Element( name = "userId" )
	private Long memberId;

	@Element( name = "rating" )

	private Long rating;
	private String crumb;
	private Date registered;

	public RestCategoryRating() {
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

	public RestObject getCategoryUri() {
		if( categoryUri == null ) {
			categoryUri = new RestObject();
		}
		return categoryUri;
	}

	public void setCategoryUri( RestObject categoryUri ) {
		this.categoryUri = categoryUri;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId( Long memberId ) {
		this.memberId = memberId;
	}

	public Long getRating() {
		return rating;
	}

	public void setRating( Long rating ) {
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
