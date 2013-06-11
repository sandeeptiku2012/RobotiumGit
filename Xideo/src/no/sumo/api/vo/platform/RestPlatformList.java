package no.sumo.api.vo.platform;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

/**
 * Provides a REST value object for a list of platforms.
 */
@Root( name = "platforms" )
@Default( DefaultType.FIELD )
public class RestPlatformList implements Iterable<RestPlatform> {
	private final List<RestPlatform> platforms;

	public RestPlatformList( List<RestPlatform> platforms ) {
		this.platforms = new ArrayList<RestPlatform>();
	}

	public RestPlatformList( RestPlatform... platforms ) {
		this.platforms = new ArrayList<RestPlatform>();
		for( RestPlatform restPlatform : platforms ) {
			this.platforms.add( restPlatform );
		}
	}

	/**
	 * Gets a list of the platforms
	 */
	public List<RestPlatform> getPlatforms() {
		return platforms;
	}

	/**
	 * Returns a list of platform names separated by +. Example: web+mobil+iptv
	 */
	@Override
	public String toString() {
		String data = "";

		for( RestPlatform platform : platforms ) {
			data += platform.toString() + "+";
		}

		return data.replaceAll( "\\+$", "" );
	}

	@Override
	public Iterator<RestPlatform> iterator() {
		return platforms.iterator();
	}

	public int size() {
		return platforms.size();
	}

	public RestPlatform get( int index ) {
		return platforms.get( index );
	}

	public List<ApiPlatform> asApiPlatform() {
		List<ApiPlatform> platforms = new ArrayList<ApiPlatform>( size() );

		for( RestPlatform platform : this ) {
			platforms.add( platform.asApiPlatform() );
		}

		return platforms;
	}

}
