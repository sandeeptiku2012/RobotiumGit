package com.comcast.xideo;

import no.sumo.api.entity.vman.enums.ProgramSortBy;

public class SortEvent {
	private final Object source;
	private ProgramSortBy sortKey;

	public SortEvent( Object source, ProgramSortBy sortKey ) {
		this.source = source;
		this.sortKey = sortKey;
	}

	public Object getSource() {
		return source;
	}

	public ProgramSortBy getSortKey() {
		return sortKey;
	}
}