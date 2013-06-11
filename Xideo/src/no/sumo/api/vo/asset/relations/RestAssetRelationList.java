package no.sumo.api.vo.asset.relations;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "assetRelations" )
public class RestAssetRelationList extends RestObject {
	private List<RestAssetRelation> relations;

	public RestAssetRelationList() {
		relations = new ArrayList<RestAssetRelation>();
	}

	public RestAssetRelationList( List<RestAssetRelation> relations ) {
		this.relations = relations;
	}

	@Element( name = "assetRelation" )
	public List<RestAssetRelation> getRelations() {
		return relations;
	}

	public void setRelations( List<RestAssetRelation> relations ) {
		this.relations = relations;
	}
}
