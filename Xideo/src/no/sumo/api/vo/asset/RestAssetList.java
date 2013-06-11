package no.sumo.api.vo.asset;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * @author <a href="mailto:thomas.aalen@tv2.no">Thomas Aal_n</a>
 * @version $Revision: 1 $
 */
@Root( name = "assets" )
public class RestAssetList extends RestObject {

	@Element( name = "numberOfHits", required = false )
	private String numberOfHits;

	@ElementList( entry = "asset", inline = true, required = false )
	private List<RestAsset> assets;

	public RestAssetList() {
	}

	public RestAssetList( List<RestAsset> assets ) {
		this.assets = assets;
	}

	public void addAsset( RestAsset asset ) {

		if( assets == null ) {
			assets = new ArrayList<RestAsset>();
		}

		assets.add( asset );
	}

	public String getNumberOfHits() {
		return numberOfHits;
	}

	public void setNumberOfHits( String numberOfHits ) {
		this.numberOfHits = numberOfHits;
	}

	public List<RestAsset> getAssets() {
		return assets;
	}

	public void setAssets( List<RestAsset> assets ) {
		this.assets = assets;
	}

}
