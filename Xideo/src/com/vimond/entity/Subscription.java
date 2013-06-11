package com.vimond.entity;

import java.util.Date;

public class Subscription {

	private SubscriptionStatus status;

	public SubscriptionStatus getStatus() {
		return status;
	}

	public void setStatus( SubscriptionStatus status ) {
		this.status = status;
	}

	private Date subdate;

	public Date getSubdate() {
		return subdate;
	}

	public void setSubdate( Date subdate ) {
		this.subdate = subdate;
	}

}