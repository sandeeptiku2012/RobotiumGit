package no.sumo.api.vo.voucher;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root( name = "voucherPool" )
public class RestVoucherPool extends RestObject {
	@Element( name = "name" )
	private String name;

	@ElementList( name = "codes", entry = "code" )
	private List<String> codes = new ArrayList<String>();

}
