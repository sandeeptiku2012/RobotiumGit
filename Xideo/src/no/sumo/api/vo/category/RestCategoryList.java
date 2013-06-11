package no.sumo.api.vo.category;

import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * @author <a href="mailto:thomas.aalen@tv2.no">Thomas Aal_n</a>
 * @version $Revision: 1 $
 */
@Root( name = "categories" )
public class RestCategoryList extends RestObject {
	@Element( name = "numberOfHits", required = false )
	private String numberOfHits;

	@ElementList( entry = "category", required = false, inline = true, empty = false )
	private List<RestCategory> categories;

	public RestCategoryList() {
	}

	public RestCategoryList( List<RestCategory> categories ) {
		this.categories = categories;
	}

	public List<RestCategory> getCategories() {
		return categories;
	}

	public void setCategories( List<RestCategory> categories ) {
		this.categories = categories;
	}

	public String getNumberOfHits() {
		return numberOfHits;
	}
}
