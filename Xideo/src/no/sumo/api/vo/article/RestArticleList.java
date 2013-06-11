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
@Root( name = "articles" )
public class RestArticleList extends RestObject {
	@Element( name = "article", required = false )
	private List<RestArticle> articles = new ArrayList<RestArticle>();;

	public RestArticleList() {
	}

	public RestArticleList( List<RestArticle> articles ) {
		this.articles = articles;
	}

	public void addArticle( RestArticle article ) {
		this.articles.add( article );
	}

	public List<RestArticle> getArticles() {
		return articles;
	}
}
