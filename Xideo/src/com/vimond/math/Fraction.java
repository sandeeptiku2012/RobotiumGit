package com.vimond.math;

public class Fraction {
	private final long numerator;
	private final long denominator;

	public Fraction( long numerator, long denominator ) {
		if( denominator == 0 ) {
			throw new IllegalArgumentException( "Denomiator cannot be zero" );
		}
		long divisor = gcd( numerator, denominator );
		this.numerator = numerator / divisor;
		this.denominator = denominator / divisor;
	}

	static long gcd( long a, long b ) {
		return b == 0 ? a : gcd( b, a % b );
	}

	public double toDouble() {
		return (double)getNumerator() / getDenominator();
	}

	public long getNumerator() {
		return numerator;
	}

	public long getDenominator() {
		return denominator;
	}
}
