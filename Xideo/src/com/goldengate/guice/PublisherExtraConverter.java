package com.goldengate.guice;

import roboguice.inject.ExtraConverter;

import com.vimond.entity.Publisher;

public class PublisherExtraConverter implements ExtraConverter<String, Publisher> {
	@Override
	public Publisher convert( String title ) {
		return new Publisher( title );
	}
}
