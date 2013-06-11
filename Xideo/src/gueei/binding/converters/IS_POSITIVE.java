package gueei.binding.converters;

import gueei.binding.Converter;
import gueei.binding.IObservable;

public class IS_POSITIVE extends Converter<Boolean> {
	public IS_POSITIVE( IObservable<?>[] dependents ) {
		super( Boolean.class, dependents );
	}

	@Override
	public Boolean calculateValue( Object... args ) throws Exception {
		if( args[ 0 ] instanceof Number ) {
			return ((Number)args[ 0 ]).doubleValue() > 0;
		}
		return false;
	}
}
