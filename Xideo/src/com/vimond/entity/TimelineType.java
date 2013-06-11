package com.vimond.entity;

public class TimelineType {

	public final static TimelineType LAST_24_HOURS = new TimelineType( "Last 24 Hours", "latestPublishedAssetDate:[NOW-1DAY TO NOW]" );
	public final static TimelineType LAST_WEEK = new TimelineType( "Last Week", "latestPublishedAssetDate:[NOW-1WEEK TO NOW]" );
	public final static TimelineType LAST_ONE_MONTH = new TimelineType( "Last Month", "latestPublishedAssetDate:[NOW-1MONTH TO NOW]" );
	public final static TimelineType LAST_YEAR = new TimelineType( "Last Year", "latestPublishedAssetDate:[NOW-1YEAR TO NOW]" );

	String name, query;

	public TimelineType(String name, String query) {
		this.name = name;
		this.query = query;
	}

	public String getName() {
		return name;
	}

	public String getQuery() {
		return query;
	}
}
