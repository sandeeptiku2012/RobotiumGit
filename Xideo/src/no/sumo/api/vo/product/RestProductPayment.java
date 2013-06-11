package no.sumo.api.vo.product;

import no.sumo.api.contracts.IProductPayment;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "productPayment" )
public class RestProductPayment extends RestObject implements IProductPayment {
	@Element( name = "description" )
	private String description;

	@Element
	private Boolean enabled;

	@Attribute
	private Long id;

	@Element
	private Long initPeriod;

	@Element
	private Double initPrice;

	@Element( name = "discountedPrice", required = false )
	private Double discountedPrice;

	@Element
	private Long paymentProviderId;

	@Element
	private Long productId;

	@Element
	private Integer sortIndex;

	@Element( name = "paymentObjectUri" )
	private RestObject paymentObjectUri = new RestObject();

	public RestProductPayment() {

	}

	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public Boolean isEnabled() {
		return enabled;
	}

	public void setEnabled( Boolean enabled ) {
		this.enabled = enabled;
	}

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Long getInitPeriod() {
		return initPeriod;
	}

	public void setInitPeriod( Long initPeriod ) {
		this.initPeriod = initPeriod;
	}

	public Double getInitPrice() {
		return initPrice;
	}

	public void setInitPrice( Double initPrice ) {
		this.initPrice = initPrice;
	}

	public Long getPaymentProviderId() {
		return paymentProviderId;
	}

	public void setPaymentProviderId( Long paymentProviderId ) {
		this.paymentProviderId = paymentProviderId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId( Long productId ) {
		this.productId = productId;
	}

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex( Integer sortIndex ) {
		this.sortIndex = sortIndex;
	}

	public Double getDiscountedPrice() {
		return this.discountedPrice;
	}

	public void setDiscountedPrice( Double discountedPrice ) {
		this.discountedPrice = discountedPrice;
	}

	public RestObject getPaymentObjectUri() {
		return paymentObjectUri;
	}

	public void setPaymentObjectUri( RestObject paymentObjectUri ) {
		this.paymentObjectUri = paymentObjectUri;
	}
}
