package no.sumo.api.entity.sumo.enums;

public enum PaymentMethod {

	CREDITCARD,
	SMS, /* In-house SMS payment */
	CREDENTIALS, /* The user can provide additional credentials to authenticate use of partner's invoice system */
	REDIRECT, /* Indicates that the payment method relies on a redirect payment model, and the client only needs to forward to the appropriate url */
	VOUCHER,
	ONE_CLICK_BUY,
	ONE_CLICK_BUY_PIN;
}
