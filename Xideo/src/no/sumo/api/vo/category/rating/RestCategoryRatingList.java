package no.sumo.api.vo.category.rating;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created with IntelliJ IDEA. User: ogi Date: 18.06.12 Time: 16:09 To change
 * this template use File | Settings | File Templates.
 */
@Root( name = "categoryRatings" )
public class RestCategoryRatingList extends RestObject {
	@Element( name = "categoryRating" )
	private List<RestCategoryRating> ratings = new ArrayList<RestCategoryRating>();

	public void addRating( RestCategoryRating rating ) {
		ratings.add( rating );
	}

	public List<RestCategoryRating> getRatings() {
		return ratings;
	}

	public void setRatings( List<RestCategoryRating> ratings ) {
		this.ratings = ratings;
	}
}
