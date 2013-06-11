package no.sumo.api.vo.asset.comment;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "comments" )
public class RestAssetCommentList {
	private String uri;
	private List<RestAssetComment> comments = new ArrayList<RestAssetComment>();

	public RestAssetCommentList() {
	}

	public RestAssetCommentList( List<RestAssetComment> comments ) {
		this.comments = comments;
	}

	@Attribute
	public String getUri() {
		return uri;
	}

	public void setUri( String uri ) {
		this.uri = uri;
	}

	@Element( name = "comment" )
	public List<RestAssetComment> getComments() {
		return comments;
	}
}
