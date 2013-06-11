package no.sumo.api.vo.favorite;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "favorites" )
@Default( DefaultType.FIELD )
public class RestFavoriteList extends RestObject {

	@Element( name = "favorite" )
	private List<RestFavorite> favorites = new ArrayList<RestFavorite>();

	public RestFavoriteList() {
	}

	public RestFavoriteList( List<RestFavorite> favorites ) {
		this.favorites = favorites;
	}

	public void addFavorite( RestFavorite favorite ) {

		if( favorites == null ) {
			favorites = new ArrayList<RestFavorite>();
		}

		favorites.add( favorite );
	}

	public List<RestFavorite> getFavorites() {
		return favorites;
	}

	public void setFavorites( List<RestFavorite> favorites ) {
		this.favorites = favorites;
	}

}
