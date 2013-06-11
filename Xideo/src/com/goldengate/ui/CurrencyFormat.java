package com.goldengate.ui;

import java.text.DecimalFormat;
import java.util.Currency;

public class CurrencyFormat extends DecimalFormat{
	private static final long serialVersionUID = 5441787660042997987L;

	public CurrencyFormat( String format, Currency currency ) {
		super( format );
		setCurrency( currency );
	}
}
