package no.sumo.api.vo.order;

import java.util.Date;

import no.sumo.api.contracts.ITransaction;
import no.sumo.api.entity.profile.enums.PaymentTransactionStatus;
import no.sumo.api.entity.profile.enums.PaymentTransactionType;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Root;

@Root( name = "transaction" )
public class RestTransaction extends RestObject implements ITransaction {

	private Long id;

	private Long paymentProviderId;

	private Double price;

	private Date registered;

	private PaymentTransactionStatus status;

	private Long transactionNumber;

	private String transactionReference;

	private PaymentTransactionType type;

	private Long orderId;

	public RestTransaction() {

	}

	public static RestTransaction getMock() {
		RestTransaction transaction = new RestTransaction();

		transaction.setId( 123456789L );
		transaction.setPaymentProviderId( 17L );
		transaction.setPrice( 49.0D );
		transaction.setRegistered( new Date() );
		transaction.setStatus( PaymentTransactionStatus.SUCCESS );
		transaction.setTransactionNumber( 987654321L );
		transaction.setTransactionReference( "eejbkl3l4lcxp08" );
		transaction.setType( PaymentTransactionType.PURCHASE );

		return transaction;
	}

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId( Long orderId ) {
		this.orderId = orderId;
	}

	public Long getPaymentProviderId() {
		return paymentProviderId;
	}

	public void setPaymentProviderId( Long paymentProviderId ) {
		this.paymentProviderId = paymentProviderId;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice( Double price ) {
		this.price = price;
	}

	public Date getRegistered() {
		return registered;
	}

	public void setRegistered( Date registered ) {
		this.registered = registered;
	}

	public PaymentTransactionStatus getStatus() {
		return status;
	}

	public void setStatus( PaymentTransactionStatus status ) {
		this.status = status;
	}

	public Long getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber( Long transactionNumber ) {
		this.transactionNumber = transactionNumber;
	}

	public String getTransactionReference() {
		return transactionReference;
	}

	public void setTransactionReference( String transactionReference ) {
		this.transactionReference = transactionReference;
	}

	public PaymentTransactionType getType() {
		return type;
	}

	public void setType( PaymentTransactionType type ) {
		this.type = type;
	}

}
