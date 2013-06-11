package com.comcast.xideo;

import gueei.binding.Command;
import gueei.binding.collections.ArrayListObservable;
import no.sumo.api.entity.vman.enums.ProgramSortBy;
import roboguice.event.EventManager;
import android.view.View;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class SortButtonList extends ArrayListObservable<SortButton> {
	@Inject
	EventManager eventManager;

	@Inject
	Provider<SortButton> sortButtonProvider;

	public Command onSort = new Command() {
		@Override
		public void Invoke( View view, Object... args ) {
			selectButton( (SortButton)args[ 0 ] );
		}
	};

	public void selectButton( SortButton button ) {
		for( SortButton temp : this ) {
			temp.selected.set( temp == button );
		}
		if( button != null ) {
			ProgramSortBy sortKey = button.getSortKey();
			eventManager.fire( new SortEvent( SortButtonList.this, sortKey ) );
		}
	}

	public void setSortKey( ProgramSortBy sortKey ) {
		if( sortKey != null ) {
			for( SortButton button : this ) {
				if( button.getSortKey() == sortKey ) {
					selectButton( button );
					return;
				}
			}
		}
		selectButton( null );
	}

	public ProgramSortBy getSortKey() {
		for( SortButton button : this ) {
			if( button.selected.get() ) {
				return button.getSortKey();
			}
		}
		return null;
	}

	public SortButtonList() {
		super( SortButton.class );
	}

	public SortButton addButton( int resourceId, ProgramSortBy sortKey ) {
		final SortButton button = sortButtonProvider.get();
		button.setTitle( resourceId );
		button.setSortKey( sortKey );
		button.click = onSort;
		add( button );
		return button;
	}
}
