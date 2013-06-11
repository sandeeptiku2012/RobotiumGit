package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

import java.util.Arrays;

import com.comcast.xideo.XideoApplication;

public class RESOURCE_STRING extends Converter<String> {
	public RESOURCE_STRING( IObservable<?>... dependents ) {
		super( String.class, dependents );
	}

	@Override
	public String calculateValue( Object... args ) throws Exception {
		if( args.length > 0 ) {
			if( args[ 0 ] instanceof Integer ) {
				return XideoApplication.getContext().getString( (Integer)args[ 0 ], Arrays.copyOfRange( args, 1, args.length ) );
			}
		}
		return null;
	}
}
