package no.sumo.api.entity.sumo.enums;

/*
 * Both strategies will convert the value of the old subscription to the value of the new one before applying the strategy.
 */
public enum ProductUpgradeStrategy {
	EXTEND, /* Extends the new subscription with the time remaining from the old subscription */
	DISCOUNT /* Discounts the price of the new subscription with the price remaining of the old subscription */
}
