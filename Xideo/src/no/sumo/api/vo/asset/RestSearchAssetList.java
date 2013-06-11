package no.sumo.api.vo.asset;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * @author <a href="mailto:thomas.aalen@tv2.no">Thomas Aal�n</a>
 * @version $Revision: 1 $
 */
@Root( name = "assets" )
public class RestSearchAssetList extends RestObject {


	@Element( name = "numberOfHits" )
	private String numberOfHits;

	@ElementList( entry = "asset", inline = true, required = false )
	private List<RestSearchAsset> assets = new ArrayList<RestSearchAsset>();

	public RestSearchAssetList() {
	}

	public RestSearchAssetList( List<RestSearchAsset> assets ) {
		this.assets = assets;
	}

	public void addAsset( RestSearchAsset asset ) {

		if( assets == null ) {
			assets = new ArrayList<RestSearchAsset>();
		}

		assets.add( asset );
	}


	public String getNumberOfHits() {
		return numberOfHits;
	}

	public void setNumberOfHits( String numberOfHits ) {
		this.numberOfHits = numberOfHits;
	}

	public List<RestSearchAsset> getAssets() {
		return assets;
	}
}
