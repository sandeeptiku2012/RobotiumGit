package com.goldengate.ui;

import javax.annotation.Nullable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.inject.Inject;
import com.google.inject.name.Named;

public class GoldenGateLayoutInflater implements LayoutInflater.Factory {
	@Inject
	@Named( "normal" )
	@Nullable
	private Typeface typeface;

	@Inject
	@Named( "bold" )
	@Nullable
	private Typeface boldTypeface;

	@Override
	public View onCreateView( String name, Context context, AttributeSet attrs ) {
		//		Ln.i( "onCreateView: " + name );
		//		for( int i = 0; i < attrs.getAttributeCount(); i++ ) {
		//			Ln.i( "  " + attrs.getAttributeName( i ) + ": " + attrs.getAttributeValue( i ) );
		//		}
		if( "TextView".equals( name ) ) {
			TextView textView = new TextView( context, attrs );
			TypedArray ta = context.obtainStyledAttributes( attrs, new int[] { android.R.attr.textStyle } );
			int textStyle = ta.getInt( 0, Typeface.NORMAL );
			switch( textStyle ) {
				case Typeface.BOLD:
					textView.setTypeface( boldTypeface );
					break;
				default:
					textView.setTypeface( typeface );
					break;
			}
			return textView;
		}
		return null;
	}
}
