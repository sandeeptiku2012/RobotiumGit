package no.sumo.api.vo.asset.relations;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root( name = "assetRelation" )
@Default( DefaultType.FIELD )
public class RestAssetRelation extends RestObject {

	private Long id;
	private Long fromAssetId;
	private Long toAssetId;
	private String name;
	private Long priority;
	private String relationType;
	private String relatedUrl;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Long getFromAssetId() {
		return fromAssetId;
	}

	public void setFromAssetId( Long fromAssetId ) {
		this.fromAssetId = fromAssetId;
	}

	public Long getToAssetId() {
		return toAssetId;
	}

	public void setToAssetId( Long toAssetId ) {
		this.toAssetId = toAssetId;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public Long getPriority() {
		return priority;
	}

	public void setPriority( Long priority ) {
		this.priority = priority;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType( String relationType ) {
		this.relationType = relationType;
	}

	public String getRelatedUrl() {
		return relatedUrl;
	}

	public void setRelatedUrl( String relatedUrl ) {
		this.relatedUrl = relatedUrl;
	}
}
