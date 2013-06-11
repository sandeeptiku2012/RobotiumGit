package no.sumo.api.vo.article;

import no.sumo.api.contracts.IArticleImage;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root( name = "image" )
@Default( DefaultType.FIELD )
public class RestArticleImage {

	private String src;
	String type;
	String title;

	public RestArticleImage() {
	}

	public RestArticleImage( String src, String type, String title ) {

		this.src = src;
		this.type = type;
		this.title = title;

	}

	public RestArticleImage( IArticleImage articleImage ) {

		src = articleImage.getSrc();
		type = articleImage.getType();
		title = articleImage.getTitle();

	}

	public String getSrc() {
		return src;
	}

	public void setSrc( String src ) {
		this.src = src;
	}

	public String getType() {
		return type;
	}

	public void setType( String type ) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTile( String title ) {
		this.title = title;
	}

}