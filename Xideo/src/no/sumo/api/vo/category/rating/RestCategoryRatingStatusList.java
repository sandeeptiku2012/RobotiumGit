package no.sumo.api.vo.category.rating;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created with IntelliJ IDEA. User: ogi Date: 29.06.12 Time: 11:34 To change
 * this template use File | Settings | File Templates.
 */
@Root( name = "categoryRatings" )
public class RestCategoryRatingStatusList extends RestObject {
	@Element( name = "categoryRating" )
	List<RestCategoryRatingStatus> statusList = new ArrayList<RestCategoryRatingStatus>();

	public void addRatingStatus( RestCategoryRatingStatus status ) {
		statusList.add( status );
	}

	public List<RestCategoryRatingStatus> getStatusList() {
		return statusList;
	}

	public void setStatusList( List<RestCategoryRatingStatus> statusList ) {
		this.statusList = statusList;
	}
}
