package no.sumo.api.vo.metadata;

import java.util.Iterator;
import java.util.List;

public class Languages implements Iterable<Language> {
	private List<Language> languages;
	private String languagesString;

	public Languages( String str ) {
		languagesString = str;
	}

	public Languages( List<Language> languages ) {
		this.languages = languages;
	}

	@Override
	public Iterator<Language> iterator() {
		return languages.iterator();
	}

	@Override
	public String toString() {
		return languagesString;
	}
}