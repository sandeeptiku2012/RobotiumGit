package no.sumo.api.vo.article;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * @author <a href="mailto:thomas.aalen@tv2.no">Thomas Aal_n</a>
 * @version $Revision: 1 $
 */
@Root( name = "articleImages" )
public class RestArticleImageList extends RestObject {
	private List<RestArticleImage> articleImages = new ArrayList<RestArticleImage>();;

	public RestArticleImageList() {
	}

	public void addArticleImage( RestArticleImage articleImages ) {
		this.articleImages.add( articleImages );
	}

	@Element( name = "articleImage" )
	public List<RestArticleImage> getArticles() {
		return articleImages;
	}
}
