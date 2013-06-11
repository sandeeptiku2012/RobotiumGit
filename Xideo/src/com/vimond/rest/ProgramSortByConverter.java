package com.vimond.rest;

import javax.ws.rs.ext.Provider;

import no.sumo.api.entity.vman.enums.ProgramSortBy;

import org.jboss.resteasy.spi.StringConverter;

import android.text.TextUtils;

@Provider
public class ProgramSortByConverter implements StringConverter<ProgramSortBy> {

	@Override
	public ProgramSortBy fromString( String sort ) {
		//Default sorting
		ProgramSortBy sortBy = ProgramSortBy.DATE_DESC;

		if( TextUtils.isEmpty( sort ) ) {
			return sortBy;
		}

		sort = sort.replace( '+', ' ' );

		if( sort.equalsIgnoreCase( "date asc" ) ) {
			sortBy = ProgramSortBy.DATE_ASC;
		} else if( sort.equalsIgnoreCase( "date desc" ) || sort.equalsIgnoreCase( "date" ) ) {
			sortBy = ProgramSortBy.DATE_DESC;
		} else if( sort.equalsIgnoreCase( "name asc" ) || sort.equalsIgnoreCase( "name" ) ) {
			sortBy = ProgramSortBy.NAME;
		} else if( sort.equalsIgnoreCase( "name desc" ) ) {
			sortBy = ProgramSortBy.NAME_DESC;
		} else if( sort.equalsIgnoreCase( "priority desc" ) || sort.equalsIgnoreCase( "priority" ) ) {
			sortBy = ProgramSortBy.PRIORITY;
		} else if( sort.equalsIgnoreCase( "rating desc" ) || sort.equalsIgnoreCase( "rating" ) ) {
			sortBy = ProgramSortBy.RATING;
		} else if( sort.equalsIgnoreCase( "view" ) || sort.equalsIgnoreCase( "view asc" ) ) {
			sortBy = ProgramSortBy.VIEW_ASC;
		} else if( sort.equalsIgnoreCase( "view desc" ) ) {
			sortBy = ProgramSortBy.VIEW_DESC;
		} else if( sort.equalsIgnoreCase( "count" ) ) {
			sortBy = ProgramSortBy.COUNT;
		} else if( sort.equalsIgnoreCase( "ratingcount desc" ) || sort.equalsIgnoreCase( "ratingcount" ) || sort.equalsIgnoreCase( "assetratingcount" ) || sort.equalsIgnoreCase( "asset rating count" ) ) {
			sortBy = ProgramSortBy.RATINGCOUNT;
		} else if( sort.equalsIgnoreCase( "category" ) ) {
			sortBy = ProgramSortBy.NODEID_ASC;
		} else if( sort.equalsIgnoreCase( "expiry" ) || sort.equalsIgnoreCase( "expiry asc" ) ) {
			sortBy = ProgramSortBy.EXPIRYDATE_ASC;
		} else if( sort.equalsIgnoreCase( "expiry desc" ) ) {
			sortBy = ProgramSortBy.EXPIRYDATE_DESC;
		} else if( sort.equalsIgnoreCase( "published" ) || sort.equalsIgnoreCase( "published asc" ) ) {
			sortBy = ProgramSortBy.PUBLISDATE_ASC;
		} else if( sort.equalsIgnoreCase( "published desc" ) ) {
			sortBy = ProgramSortBy.PUBLISDATE_DESC;
		} else if( sort.equalsIgnoreCase( "asset" ) || sort.equalsIgnoreCase( "asset title" ) || sort.equalsIgnoreCase( "asset title asc" ) ) {
			sortBy = ProgramSortBy.ASSET_TITLE;
		} else if( sort.equalsIgnoreCase( "asset desc" ) || sort.equalsIgnoreCase( "asset title desc" ) ) {
			sortBy = ProgramSortBy.ASSET_TITLE_DESC;
		} else if( sort.equalsIgnoreCase( "category name" ) || sort.equalsIgnoreCase( "category name asc" ) || sort.equalsIgnoreCase( "category asc" ) ) {
			sortBy = ProgramSortBy.CATEGORY_NAME;
		} else if( sort.equalsIgnoreCase( "category name desc" ) || sort.equalsIgnoreCase( "category desc" ) ) {
			sortBy = ProgramSortBy.CATEGORY_NAME_DESC;
		} else if( sort.equalsIgnoreCase( "categoryratingcount desc" ) || sort.equalsIgnoreCase( "categoryratingcount" ) || sort.equalsIgnoreCase( "category rating count" ) ) {
			sortBy = ProgramSortBy.CATEGORY_RATING_COUNT;
		}

		return sortBy;
	}

	@Override
	public String toString( ProgramSortBy value ) {
		switch( value ) {
			case CATEGORY_RATING_COUNT:
				return "categoryratingcount";
			case PUBLISDATE_ASC:
				return "published";
			case PUBLISDATE_DESC:
				return "published desc";
			case NODEID_ASC:
				return "category";
			default:
				return value.toString().toLowerCase().replace( '_', ' ' );
		}
	}
}
