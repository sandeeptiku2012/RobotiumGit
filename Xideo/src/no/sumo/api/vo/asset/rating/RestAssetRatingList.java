package no.sumo.api.vo.asset.rating;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "ratings" )
public class RestAssetRatingList {
	private String uri;
	private List<RestAssetRating> ratings = new ArrayList<RestAssetRating>();

	public RestAssetRatingList() {
	}

	public RestAssetRatingList( List<RestAssetRating> ratings ) {
		this.ratings = ratings;
	}

	@Attribute
	public String getUri() {
		return uri;
	}

	public void setUri( String uri ) {
		this.uri = uri;
	}

	@Element( name = "rating" )
	public List<RestAssetRating> getRatings() {
		return ratings;
	}
}
