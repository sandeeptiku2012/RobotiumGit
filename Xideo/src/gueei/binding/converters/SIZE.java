package gueei.binding.converters;

import gueei.binding.CollectionChangedEventArg;
import gueei.binding.CollectionObserver;
import gueei.binding.Converter;
import gueei.binding.IObservable;
import gueei.binding.IObservableCollection;
import gueei.binding.collections.ArrayListObservable;

import java.util.ArrayList;
import java.util.Collection;

public class SIZE extends Converter<Integer> implements CollectionObserver {
	public SIZE( IObservable<?>... dependents ) {
		super( Integer.class, dependents );
		for( IObservable<?> o : dependents ) {
			if( o instanceof IObservableCollection<?> ) {
				((IObservableCollection<?>)o).subscribe( (CollectionObserver)this );
			}
		}
	}

	@Override
	public Integer calculateValue( Object... args ) throws Exception {
		if( args[ 0 ] instanceof ArrayListObservable<?> ) {
			ArrayListObservable<?> arrayListObservable = (ArrayListObservable<?>)args[ 0 ];
			return arrayListObservable.size();
		}
		return null;
	}

	@Override
	public void onCollectionChanged( IObservableCollection<?> collection, CollectionChangedEventArg args, Collection<Object> initiators ) {
		onPropertyChanged( null, new ArrayList<Object>() );
	}

}
