package no.sumo.api.exception.payment;

import no.sumo.api.exception.Errorcode;
import no.sumo.api.exception.base.SuperException;

public class PaymentException extends SuperException {

	private static final long serialVersionUID = -687989862394691375L;
	private String endUserMessage;

	public PaymentException( String message, String endUserMessage ) {
		super( Errorcode.PAYMENT_ERROR, message );
		this.endUserMessage = endUserMessage;
	}

	public PaymentException( String message ) {
		this( message, (String)null );
	}

	public PaymentException( String message, Throwable cause ) {
		super( Errorcode.PAYMENT_ERROR, message, cause );
	}

	public PaymentException( Errorcode errorCode, String message, Throwable cause ) {
		super( errorCode, message, cause );
	}

	public String getEndUserMessage() {
		return endUserMessage;
	}

	public void setEndUserMessage( String endUserMessage ) {
		this.endUserMessage = endUserMessage;
	}

	@Override
	public String getMessage() {
		if( getEndUserMessage() != null ) {
			return getEndUserMessage();
		}
		return super.getMessage();
	}
}