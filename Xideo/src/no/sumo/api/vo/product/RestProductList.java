package no.sumo.api.vo.product;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root( name = "products" )
public class RestProductList extends RestObject {
	@ElementList( entry = "product", inline = true, required = false )
	private List<RestProduct> products;

	public RestProductList() {
		this.products = new ArrayList<RestProduct>();
	}

	public RestProductList( List<RestProduct> products ) {
		this.products = products;
	}

	public List<RestProduct> getProducts() {
		return products;
	}

	public void setProducts( List<RestProduct> products ) {
		this.products = products;
	}

}
