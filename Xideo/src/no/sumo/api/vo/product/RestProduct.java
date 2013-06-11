package no.sumo.api.vo.product;

import no.sumo.api.contracts.IPaymentPlan;
import no.sumo.api.contracts.IProduct;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "product" )
@Default( DefaultType.FIELD )
public class RestProduct extends RestObject implements IProduct {
	@Element( name = "paymentPlan", type = RestPaymentPlan.class )
	private IPaymentPlan paymentPlan;

	@Element( name = "description" )
	private String description;

	@Element( name = "enabled" )
	private Boolean enabled;

	@Element( name = "minimumPeriods" )
	private Integer minimumPeriods;

	@Element( name = "price" )
	private Double price;

	@Element( name = "productGroupId" )
	private Long productGroupId;

	private Integer sortIndex;

	@Element( name = "productPaymentsUri" )
	private RestObject productPaymentsUri;

	@Attribute( name = "id" )
	private Long id;

	public RestProduct() {
	}

	public RestObject getProductPaymentsUri() {
		if( productPaymentsUri == null ) {
			productPaymentsUri = new RestObject();
		}
		return productPaymentsUri;
	}

	public IPaymentPlan getPaymentPlan() {
		return paymentPlan;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled( Boolean enabled ) {
		this.enabled = enabled;
	}

	public Integer getMinimumPeriods() {
		return minimumPeriods;
	}

	public void setMinimumPeriods( Integer minimumPeriods ) {
		this.minimumPeriods = minimumPeriods;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice( Double price ) {
		this.price = price;
	}

	public Long getProductGroupId() {
		return productGroupId;
	}

	public void setProductGroupId( Long productGroupId ) {
		this.productGroupId = productGroupId;
	}

	public Integer getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex( Integer sortIndex ) {
		this.sortIndex = sortIndex;
	}

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}
}
