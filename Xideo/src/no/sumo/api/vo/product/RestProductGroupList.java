package no.sumo.api.vo.product;

import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root( name = "productGroups" )
public class RestProductGroupList extends RestObject {

	@ElementList( entry = "productgroup", inline = true, required = false )
	private List<RestProductGroup> productGroups;

	public RestProductGroupList() {
	}

	public RestProductGroupList( List<RestProductGroup> productGroups ) {
		this.productGroups = productGroups;
	}

	public List<RestProductGroup> getProductGroups() {
		return productGroups;
	}

	public void setProducts( List<RestProductGroup> productGroups ) {
		this.productGroups = productGroups;
	}
}
