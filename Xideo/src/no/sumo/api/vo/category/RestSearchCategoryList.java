package no.sumo.api.vo.category;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * Vimond Media Solutions AS
 * <p/>
 * User: dirceuvjr, Date: 03/07/12, Time: 13:02
 */
@Root( name = "categories" )
public class RestSearchCategoryList {

	@Element( name = "numberOfHits", required = false )
	private String hits;

	@ElementList( entry = "category", inline = true, required = false )
	private List<RestSearchCategory> categories;

	public RestSearchCategoryList() {
	}

	public RestSearchCategoryList( List<RestSearchCategory> categories ) {
		this.categories = categories;
	}

	public String getNumberOfHits() {
		return hits;
	}

	public void setNumberOfHits( String numberOfHits ) {
		hits = numberOfHits;
	}

	public List<RestSearchCategory> getCategories() {
		if( categories == null ) {
			categories = new ArrayList<RestSearchCategory>();
		}

		return categories;
	}

	public void setCategories( List<RestSearchCategory> categories ) {
		this.categories = categories;
	}
}
