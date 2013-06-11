package gueei.binding.bindingProviders;

import gueei.binding.ViewAttribute;
import gueei.binding.viewAttributes.view.FocusableViewAttribute;
import android.view.View;

public class ExtraViewProvider extends BindingProvider {
	@Override
	public ViewAttribute<View, ?> createAttributeForView( View view, String attributeId ) {
		if( attributeId.equals( "focusable" ) ) {
			return new FocusableViewAttribute( view, "focusable" );
		}
		return null;
	}

}
