package no.sumo.api.vo.voucher;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementArray;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "voucher" )
@Default( DefaultType.FIELD )
public class RestVoucher extends RestObject implements IVoucher {

	@Transient
	private String code;
	@Transient
	private VoucherType voucherType;
	@Transient
	private DiscountType discountType;
	@Transient
	private HashSet<String> codes;
	@Transient
	private boolean unmarshalling = false;

	@Element( name = "pool" )
	private String pool;

	@Element( name = "startDate" )
	private Date startDate;
	@Element( name = "expiry" )
	private Date expiry;
	@Element( name = "registered" )
	private Date registered;

	@Element( name = "usages" )
	private Integer usages;
	@Element( name = "available" )
	private Boolean available;

	@Element( name = "discount" )
	private Double discount;
	@Element( name = "discountFraction" )
	private Double discountFraction;

	@Element( name = "productPaymentId" )
	private Long productPaymentId;

	@Element( name = "userUri" )
	private RestObject userUri;
	@Element( name = "paymentUri" )
	private RestObject productPaymentUri;

	@Override
	@Element( name = "code" )
	public String getCode() {
		if( codes != null ) {
			if( codes.size() == 1 ) {
				code = codes.iterator().next();
			} else if( codes.size() > 1 ) {
				return null;
			}
		}

		return code;
	}

	@Override
	public void setCode( String code ) {
		if( !unmarshalling ) {
			codes = null;
		}
		this.code = code;
	}

	@ElementArray( name = "codes", entry = "code" )
	protected HashSet<String> getCodes() {
		if( codes != null && codes.size() == 1 ) {
			return null;
		}
		return codes;
	}

	@Transient
	public Set<String> getAllCodes() {
		if( codes == null ) {
			codes = new HashSet<String>();
		}

		if( code != null ) {
			codes.add( code );
		}

		return codes;
	}

	public void addCode( String code ) {
		getAllCodes().add( code );
	}

	@Override
	public String getPool() {
		return pool;
	}

	@Override
	public void setPool( String pool ) {
		this.pool = pool;
	}

	@Override
	public Date getStartDate() {
		return startDate;
	}

	@Override
	public void setStartDate( Date startDate ) {
		this.startDate = startDate;
	}

	@Override
	public Date getExpiry() {
		return expiry;
	}

	@Override
	public void setExpiry( Date expiry ) {
		this.expiry = expiry;
	}

	@Override
	public Date getRegistered() {
		return registered;
	}

	@Override
	public void setRegistered( Date registered ) {
		this.registered = registered;
	}

	@Override
	public Integer getUsages() {
		return usages;
	}

	@Override
	public void setUsages( Integer usages ) {
		this.usages = usages;
	}

	@Override
	public Boolean getAvailable() {
		return available;
	}

	@Override
	public void setAvailable( Boolean available ) {
		this.available = available;
	}

	@Override
	public Double getDiscount() {
		return discount;
	}

	@Override
	public void setDiscount( Double discount ) {
		this.discount = discount;
	}

	@Override
	public Double getDiscountFraction() {
		return discountFraction;
	}

	@Override
	public void setDiscountFraction( Double discountFraction ) {
		this.discountFraction = discountFraction;
	}

	public RestObject getUserUri() {
		if( userUri == null ) {
			userUri = new RestObject();
		}
		return userUri;
	}

	public RestObject getProductPaymentUri() {
		if( productPaymentUri == null ) {
			productPaymentUri = new RestObject();
		}
		return productPaymentUri;
	}

	@Override
	public Long getProductPaymentId() {
		return productPaymentId;
	}

	@Override
	public void setProductPaymentId( Long productPaymentId ) {
		this.productPaymentId = productPaymentId;
	}

	@Override
	public VoucherType getVoucherType() {
		return voucherType;
	}

	@Attribute( name = "voucherType" )
	public String getVoucherTypeString() {
		if( voucherType != null ) {
			return voucherType.toString();
		}
		return null;
	}

	@Override
	public void setVoucherType( VoucherType voucherType ) {
		this.voucherType = voucherType;
	}

	@Override
	public DiscountType getDiscountType() {
		return discountType;
	}

	@Attribute( name = "discountType" )
	public String getDiscountTypeString() {
		if( discountType != null ) {
			return discountType.toString();
		}
		return null;
	}

	@Override
	public void setDiscountType( DiscountType discountType ) {
		this.discountType = discountType;
	}
}
