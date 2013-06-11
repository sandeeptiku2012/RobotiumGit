package no.sumo.api.entity.sumo;

import no.sumo.api.vo.voucher.IVoucher;

public enum VoucherStatus {
	/**
	 * Returns all vouchers
	 */
	ALL,
	/**
	 * Returns only vouchers that is available. An available voucher is one that
	 * has not been used, and {@link IVoucher#getStartDate() startdate} is gt
	 * now, and {@link IVoucher#getExpiry() expiry} is lt now
	 */
	AVAILABLE,
	/**
	 * Returns the vouchers that has been activated/used
	 */
	ACTIVATED,
	/**
	 * Returns vouchers that is flagged as flagged as campaign
	 */
	CAMPAIGN
}