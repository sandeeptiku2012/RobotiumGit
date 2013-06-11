package no.sumo.api.vo.contentpanel;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * @author tea
 */
@Root( name = "contentPanelElements" )
@Default( DefaultType.FIELD )
public class RestContentPanel extends RestObject {
	@ElementList( name = "contentPanelElement", inline = true, required = false )
	private List<RestContentPanelElement> panels;

	public RestContentPanel() {
	}

	public RestContentPanel( List<RestContentPanelElement> contentPanels ) {
		panels = contentPanels;
	}

	public List<RestContentPanelElement> getElements() {
		if( panels == null ) {
			panels = new ArrayList<RestContentPanelElement>();
		}

		return panels;
	}

	public void setElements( List<RestContentPanelElement> contentPanels ) {
		panels = contentPanels;
	}
}
