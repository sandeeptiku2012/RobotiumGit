package no.sumo.api.contracts;

import no.sumo.api.entity.sumo.enums.PaymentType;

public interface IPaymentPlan {

	long getId();

	String getName();

	long getPeriod();

	PaymentType getPaymentType();

}
