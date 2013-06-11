package no.sumo.api.vo.metadata.definitions;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Root;

@Root( name = "metadataDefinitions" )
public class RestMetadataDefinitionList {

	private List<RestMetadataDefinition> metadataDefinition = null;

	public RestMetadataDefinitionList() {
		metadataDefinition = new ArrayList<RestMetadataDefinition>();
	}

	public RestMetadataDefinitionList( List<RestMetadataDefinition> metadataDefinition ) {
		this.metadataDefinition = metadataDefinition;
	}

	public List<RestMetadataDefinition> getMetadataDefinition() {
		return metadataDefinition;
	}

	public void setMetadataDefinition( List<RestMetadataDefinition> metadataDefinition ) {
		this.metadataDefinition = metadataDefinition;
	}
}
