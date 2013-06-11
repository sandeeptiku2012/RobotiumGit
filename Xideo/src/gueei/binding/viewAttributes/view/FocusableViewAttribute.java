package gueei.binding.viewAttributes.view;

import gueei.binding.ViewAttribute;
import android.view.View;

public class FocusableViewAttribute extends ViewAttribute<View, Boolean> {
	public FocusableViewAttribute( View view, String attributeName ) {
		super( Boolean.class, view, attributeName );
	}

	@Override
	protected void doSetAttributeValue( Object newValue ) {
		if( newValue == null ) {
			getView().setFocusable( false );
			return;
		}
		if( newValue instanceof Boolean ) {
			getView().setFocusable( ((Boolean)newValue).booleanValue() );
		}
	}

	@Override
	public Boolean get() {
		return Boolean.valueOf( getView().isFocusable() );
	}
}
