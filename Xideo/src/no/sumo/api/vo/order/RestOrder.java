package no.sumo.api.vo.order;

import java.util.Calendar;
import java.util.Date;

import no.sumo.api.contracts.IOrder;
import no.sumo.api.entity.sumo.enums.AutorenewStatus;
import no.sumo.api.entity.sumo.enums.MemberAccessStatus;
import no.sumo.api.vo.RestObject;
import no.sumo.api.vo.notification.NotificationType;
import no.sumo.api.vo.product.RestPaymentObject;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "order", strict = false )
@Default( required = false, value = DefaultType.FIELD )
public class RestOrder extends RestObject implements IOrder {
	private String callbackUrl;

	private AutorenewStatus autorenewStatus;

	private Date earliestEndDate;

	private Date endDate;

	private Date accessEndDate;

	private Long id;

	private Long categoryId;

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( Long categoryId ) {
		this.categoryId = categoryId;
	}

	private Double price;

	private String productName;

	private Long progId;

	private Long productId;

	private Long productGroupId;

	@Element( name = "productGroup", required = false )
	private RestObject productGroupUri;

	private Date startDate;

	private Long userId;

	private String ip;

	private String isp;

	private Long period;

	private Integer platformId;

	private Long productPaymentId;

	private String referrer;

	private String voucherCode;

	private String orderRef;

	private MemberAccessStatus status;

	@Element( name = "payment", required = false )
	private RestPaymentObject paymentObject;

	@Transient
	private String currency;

	// not part of the user domain object
	private NotificationType notifyUserOnCreation;

	public RestOrder() {

	}

	public static RestOrder getMock() {

		RestOrder mock = new RestOrder();

		Calendar cal = Calendar.getInstance();

		mock.setStartDate( cal.getTime() );

		cal.add( Calendar.MONTH, 1 );

		mock.setEndDate( cal.getTime() );

		cal.add( Calendar.MONTH, 5 );

		mock.setEarliestEndDate( cal.getTime() );
		mock.setAutorenewStatus( AutorenewStatus.ACTIVE );
		mock.setPrice( 49.0 );
		mock.setProductName( "Sumo Pluss Uke" );
		mock.setId( 1733992L );

		return mock;
	}

	public String getIp() {
		return ip;
	}

	public void setIp( String ip ) {
		this.ip = ip;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp( String isp ) {
		this.isp = isp;
	}

	public Long getPeriod() {
		return period;
	}

	public void setPeriod( Long period ) {
		this.period = period;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId( Integer platformId ) {
		this.platformId = platformId;
	}

	public Long getProductPaymentId() {
		return productPaymentId;
	}

	public void setProductPaymentId( Long productProviderId ) {
		productPaymentId = productProviderId;
	}

	public String getReferrer() {
		return referrer;
	}

	public void setReferrer( String referrer ) {
		this.referrer = referrer;
	}

	public String getOrderRef() {
		return orderRef;
	}

	public void setOrderRef( String orderRef ) {
		this.orderRef = orderRef;
	}

	public MemberAccessStatus getStatus() {
		return status;
	}

	public void setStatus( MemberAccessStatus status ) {
		this.status = status;
	}

	public String getVoucherCode() {
		return voucherCode;
	}

	public void setVoucherCode( String voucherCode ) {
		this.voucherCode = voucherCode;
	}

	public AutorenewStatus getAutorenewStatus() {
		return autorenewStatus;
	}

	public void setAutorenewStatus( AutorenewStatus autorenewStatus ) {
		this.autorenewStatus = autorenewStatus;
	}

	public Date getEarliestEndDate() {
		return earliestEndDate;
	}

	public void setEarliestEndDate( Date earliestEndDate ) {
		this.earliestEndDate = earliestEndDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate( Date endDate ) {
		this.endDate = endDate;
	}

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice( Double price ) {
		this.price = price;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName( String productName ) {
		this.productName = productName;
	}

	public Long getProgId() {
		return progId;
	}

	public void setProgId( Long progId ) {
		this.progId = progId;
	}

	public Long getProductGroupId() {
		return productGroupId;
	}

	public void setProductGroupId( Long productGroupId ) {
		this.productGroupId = productGroupId;
	}

	public RestObject getProductGroupUri() {
		if( productGroupUri == null ) {
			productGroupUri = new RestObject();
		}
		return productGroupUri;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId( Long productId ) {
		this.productId = productId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate( Date startDate ) {
		this.startDate = startDate;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId( Long userId ) {
		this.userId = userId;
	}

	public RestPaymentObject getPaymentObject() {
		return paymentObject;
	}

	public void setPaymentObject( RestPaymentObject paymentObject ) {
		this.paymentObject = paymentObject;
	}

	public Date getAccessEndDate() {
		return accessEndDate;
	}

	public void setAccessEndDate( Date accessEndDate ) {
		this.accessEndDate = accessEndDate;
	}

	public String getCallbackUrl() {
		return callbackUrl;
	}

	public void setCallbackUrl( String callbackUrl ) {
		this.callbackUrl = callbackUrl;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency( String currency ) {
		this.currency = currency;
	}

	public NotificationType getNotifyUserOnCreation() {
		return notifyUserOnCreation;
	}

	public void setNotifyUserOnCreation( NotificationType notifyUserOnCreation ) {
		this.notifyUserOnCreation = notifyUserOnCreation;
	}
}
