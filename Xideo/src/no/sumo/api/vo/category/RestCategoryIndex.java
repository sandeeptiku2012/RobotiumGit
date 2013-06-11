/**
 * 
 */
package no.sumo.api.vo.category;

import java.util.LinkedHashMap;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

/**
 * @author tea
 * 
 */
@Root( name = "categories" )
@Default( DefaultType.FIELD )
public class RestCategoryIndex extends RestObject {
	private LinkedHashMap<String, RestCategoryList> index = new LinkedHashMap<String, RestCategoryList>();

	public LinkedHashMap<String, RestCategoryList> getIndex() {
		return index;
	}

	public void setIndex( LinkedHashMap<String, RestCategoryList> index ) {
		this.index = index;
	}
}
