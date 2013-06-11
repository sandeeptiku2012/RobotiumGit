package no.sumo.api.vo.product;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root( name = "productPayments" )
public class RestProductPaymentList {
	@ElementList( entry = "productPayment", inline = true )
	private List<RestProductPayment> productPaymentList;

	public RestProductPaymentList() {
		productPaymentList = new ArrayList<RestProductPayment>();
	}

	public RestProductPaymentList( List<RestProductPayment> productPaymentList ) {
		this.productPaymentList = productPaymentList;
	}

	public List<RestProductPayment> getProductPaymentList() {
		return productPaymentList;
	}

	public void setProductPaymentList( List<RestProductPayment> productPaymentList ) {
		this.productPaymentList = productPaymentList;
	}
}
