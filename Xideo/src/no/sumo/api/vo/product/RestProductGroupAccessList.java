package no.sumo.api.vo.product;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "productGroupAccesses" )
public class RestProductGroupAccessList {

	@Element( name = "productGroupAccess" )
	private List<RestProductGroupAccess> productGroupAccessList;

	public RestProductGroupAccessList() {
		productGroupAccessList = new ArrayList<RestProductGroupAccess>();
	}

	public RestProductGroupAccessList(List<RestProductGroupAccess> productGroupAccessList) {
		this.productGroupAccessList = productGroupAccessList;
	}

	public List<RestProductGroupAccess> getProductGroupAccessList() {
		return productGroupAccessList;
	}

	public void setProductGroupAccessList(List<RestProductGroupAccess> productGroupAccessList) {
		this.productGroupAccessList = productGroupAccessList;
	}

	public void add(RestProductGroupAccess restProductGroupAccess) {
		productGroupAccessList.add(restProductGroupAccess);
	}
}
