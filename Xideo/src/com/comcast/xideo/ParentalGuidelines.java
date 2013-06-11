package com.comcast.xideo;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ParentalGuidelines {
	public enum TelevisionRating {
		NO_RATING,

		// TV-Y: Suitable for all children (particularly those of preschool or kindergarten age, as this rating is commonly seen in early childhood shows)
		CHILDREN,

		// TV-Y7: Suitable for children over seven years old. May contain cartoonish slapstick violence, mild innuendo, and/or themes considered too controversial or incomprehensible for children under seven.
		OLDER_CHILDREN,

		// TV-G: Suitable for all audiences; contains little to no adult themes, sexual innuendo, violence, or crude language. Also used for shows with inoffensive content (such as cooking shows, religious programming, and reruns of classic game shows)
		GENERAL_AUDIENCE,

		// TV-PG: Parental guidance suggested; may contain mild to moderate crude language, sexual references, violence, and/or suggestive themes and dialogue
		PARENTAL_GUIDANCE,

		// TV-14: Parents strongly cautioned; may be inappropriate for children younger than 14 years of age. Contains moderate to strong violence (including some blood-letting), moderate sexual references (including censored nudity and heavily implied scenes of sexual intercourse), moderate to strong offensive language (including bleeped-out obscenities), and moderate sexual innuendo.
		PARENTS_STRONGLY_CAUTIONED,

		// TV-MA: Mature audiences; inappropriate for people under 17 years of age. Contains explicit language (which may or may not be censored for advertising reasons), explicit (though, in some cases, not pornographic) sexual content, and extreme violence.
		MATURE_AUDIENCE,
	}

	public enum TelevisionSubRating {
		// V: Used with the TV-PG, TV-14, and TV-MA rating to denote instances of violence, gore, threat, and scenes depicting peril and/or distress.
		VIOLENCE,

		// S: Used with the TV-PG, TV-14, and TV-MA rating to denote instances of sexual content (including innuendo, intercourse, nudity, and references to homosexuality and bisexuality)
		SEXUAL_SITUATIONS,

		// L: Used with the TV-PG, TV-14, and TV-MA rating to denote instances of crude, offensive language (profanity, vulgar slang, racial and ethnic slurs, etc)
		LANGUAGE,

		// D: Used with the TV-PG and TV-14 rating (though some channels attach this to the TV-MA rating) to denote the use of dialogue that hints at something sexual, violent, disturbing, or drug-related.
		SUGGESTIVE_DIALOG,

		// FV: Fantasy violence (only used with the TV-Y7 rating for children's shows that are action-oriented)
		FANTASY_VIOLENCE,
	}

	public static int getResourceId( TelevisionRating rating ) {
		switch( rating ) {
			case CHILDREN:
				return R.drawable.xf_store_ratings_tvy;
			case OLDER_CHILDREN:
				return R.drawable.xf_store_ratings_tvy7;
			case GENERAL_AUDIENCE:
				return R.drawable.xf_store_ratings_tvg;
			case PARENTAL_GUIDANCE:
				return R.drawable.xf_store_ratings_tvpg;
			case PARENTS_STRONGLY_CAUTIONED:
				return R.drawable.xf_store_ratings_tv14;
			case MATURE_AUDIENCE:
				return R.drawable.xf_store_ratings_tvma;
			default:
				return R.drawable.xf_store_ratings_nr;
		}
	}

	public static TelevisionRating getTelevisionRating( String code ) {
		TelevisionRating rating = apiCodeLookup.get( code );
		return rating != null ? rating : TelevisionRating.NO_RATING;
	}

	private final static Map<String, TelevisionRating> apiCodeLookup;

	static {
		Map<String, TelevisionRating> map = new HashMap<String, ParentalGuidelines.TelevisionRating>();
		map.put( "TV-Y", TelevisionRating.CHILDREN );
		map.put( "TV-Y7", TelevisionRating.OLDER_CHILDREN );
		map.put( "TV-G", TelevisionRating.GENERAL_AUDIENCE );
		map.put( "TV-PG", TelevisionRating.PARENTAL_GUIDANCE );
		map.put( "TV-14", TelevisionRating.PARENTS_STRONGLY_CAUTIONED );
		map.put( "TV-MA", TelevisionRating.MATURE_AUDIENCE );
		apiCodeLookup = Collections.unmodifiableMap( map );
	}
}
