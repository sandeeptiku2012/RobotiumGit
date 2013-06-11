package no.sumo.api.vo.asset;

import java.util.List;

import no.sumo.api.vo.product.RestProductGroup;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root( name = "asset" )
public class RestSearchAsset extends RestAsset {
	@ElementList( name = "productGroups", entry = "productGroup", required = false )
	private List<RestProductGroup> productGroups;

	@ElementList( name = "productGroupAccessTypes", entry = "productGroupAccessType", required = false )
	private List<String> productGroupAccessTypes;

	public List<RestProductGroup> getProductGroups() {
		return productGroups;
	}

	public void setProductGroups( List<RestProductGroup> productGroups ) {
		this.productGroups = productGroups;
	}

	public List<String> getProductGroupAccessTypes() {
		return productGroupAccessTypes;
	}

	public void setProductGroupAccessTypes( List<String> productGroupAccessTypes ) {
		this.productGroupAccessTypes = productGroupAccessTypes;
	}

}
