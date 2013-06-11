package no.sumo.api.vo.voucher;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "voucherCodes" )
@Default( DefaultType.FIELD )
public class RestVoucherCodeList extends RestObject {

	@Element( name = "code" )
	private List<String> voucherCodes;

	public List<String> getVoucherCodes() {
		if( voucherCodes == null ) {
			voucherCodes = new ArrayList<String>();
		}
		return voucherCodes;
	}

	public void setVoucherCodes( List<String> voucherCodes ) {
		this.voucherCodes = voucherCodes;
	}
}
