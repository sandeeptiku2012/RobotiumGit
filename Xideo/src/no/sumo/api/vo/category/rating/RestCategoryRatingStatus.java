package no.sumo.api.vo.category.rating;

import java.util.Date;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

/**
 * Created with IntelliJ IDEA. User: ogi Date: 29.06.12 Time: 11:34 To change
 * this template use File | Settings | File Templates.
 */
@Root( name = "categoryRatingStatus" )
public class RestCategoryRatingStatus extends RestObject {
	@Transient
	private Long id;

	@Attribute
	private Long categoryId;

	@Attribute
	private Long rating;

	private Integer voteCount;

	private Date lastUpdate;

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

	public Long getRating() {
		return rating;
	}

	public void setRating( Long rating ) {
		this.rating = rating;
	}

	public Integer getVoteCount() {
		return voteCount;
	}

	public void setVoteCount( Integer voteCount ) {
		this.voteCount = voteCount;
	}

	public Date getLastUpdate() {
		return lastUpdate;
	}

	public void setLastUpdate( Date lastUpdate ) {
		this.lastUpdate = lastUpdate;
	}
}
