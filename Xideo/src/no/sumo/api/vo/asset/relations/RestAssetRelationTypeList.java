package no.sumo.api.vo.asset.relations;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "assetRelationTypes" )
public class RestAssetRelationTypeList extends RestObject {

	private List<RestAssetRelationType> assetRelationTypes;

	public RestAssetRelationTypeList() {
		assetRelationTypes = new ArrayList<RestAssetRelationType>();
	}

	public RestAssetRelationTypeList( List<RestAssetRelationType> assetRelationTypes ) {
		this.assetRelationTypes = assetRelationTypes;
	}

	@Element( name = "assetRelationType" )
	public List<RestAssetRelationType> getAssetRelationTypes() {
		return assetRelationTypes;
	}

	public void setAssetRelationTypes( List<RestAssetRelationType> assetRelationTypes ) {
		this.assetRelationTypes = assetRelationTypes;
	}
}
