package no.sumo.api.vo.order;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root( name = "orders" )
public class RestOrderList {
	@ElementList( entry = "order", inline = true, required=false )
	private
	List<RestOrder> orders;

	public List<RestOrder> getOrders() {
		return orders;
	}
}
