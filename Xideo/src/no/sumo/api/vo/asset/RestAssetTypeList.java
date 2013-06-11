package no.sumo.api.vo.asset;

import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Root;

@Root( name = "assetTypes" )
public class RestAssetTypeList extends RestObject {

	private List<RestAssetType> assetType;

	public List<RestAssetType> getAssetType() {
		return assetType;
	}

	public void setAssetType( List<RestAssetType> assetType ) {
		this.assetType = assetType;
	}
}
