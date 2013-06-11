package com.comcast.xideo;

import gueei.binding.Command;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import no.sumo.api.entity.vman.enums.ProgramSortBy;

public class SortButton { // NO_UCD (unused code)
	public final IntegerObservable titleResourceId = new IntegerObservable();
	public final BooleanObservable selected = new BooleanObservable( false );

	private ProgramSortBy sortKey;
	public Command click;

	public ProgramSortBy getSortKey() {
		return sortKey;
	}

	public void setSortKey( ProgramSortBy sortKey ) {
		this.sortKey = sortKey;
	}

	public void setTitle( int resId ) {
		titleResourceId.set( resId );
	}
}
