package no.sumo.api.entity.sumo.enums;

/*
 * Currently only using ACTIVE and INITIALIZE, the rest is intended for future use:
 * Track failed captures, track free accesses due to failed autorenewals etc.
 */
public enum MemberAccessStatus {
	ACTIVE, INACTIVE, INITIALIZE, CAPTURE, FREE_ACCESS, STOPPED, UPGRADED
}
