package no.sumo.api.vo.voucher;

import java.util.Collection;
import java.util.EnumSet;

import no.sumo.api.entity.sumo.VoucherStatus;

public class RestVoucherStatus {
	private final EnumSet<VoucherStatus> values;

	private RestVoucherStatus( EnumSet<VoucherStatus> values ) {
		this.values = values;
	}

	public EnumSet<VoucherStatus> getValues() {
		return values;
	}

	public static RestVoucherStatus all() {
		return new RestVoucherStatus( EnumSet.of( VoucherStatus.ALL ) );
	}

	public static RestVoucherStatus copyOf( Collection<VoucherStatus> status ) {
		return new RestVoucherStatus( EnumSet.copyOf( status ) );
	}

	public static RestVoucherStatus of( VoucherStatus s1 ) {
		return new RestVoucherStatus( EnumSet.of( s1 ) );
	}

	public static RestVoucherStatus of( VoucherStatus s1, VoucherStatus... status ) {
		return new RestVoucherStatus( EnumSet.of( s1, status ) );
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (values == null ? 0 : values.hashCode());
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if( this == obj ) {
			return true;
		}
		if( obj == null ) {
			return false;
		}
		if( getClass() != obj.getClass() ) {
			return false;
		}
		RestVoucherStatus other = (RestVoucherStatus)obj;
		if( values == null ) {
			if( other.values != null ) {
				return false;
			}
		} else if( !values.equals( other.values ) ) {
			return false;
		}
		return true;
	}
}
