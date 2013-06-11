package no.sumo.api.vo.article;

import no.sumo.api.contracts.IArticle;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "article" )
@Default( DefaultType.FIELD )
public class RestArticle extends RestObject implements IArticle {
	@Attribute
	private Integer id;

	@Transient
	private Long categoryId = -1l;
	@Transient
	private Long assetId = -1L;

	@Element( name = "category" )
	private RestObject categoryUri;
	@Element( name = "asset" )
	private RestObject assetUri;
	@Element( name = "playback" )
	private RestObject playUri;

	private String title;
	private String text;
	private String text2;
	private String type;
	private String platform;
	private String image;
	//private List<IArticleImage> iArticleImages = new ArrayList<IArticleImage>();
	private RestArticleImageList articleImages = new RestArticleImageList();

	public String getPlatform() {
		return platform;
	}

	public void setPlatform( String platform ) {
		this.platform = platform;
	}

	public RestArticle() {
	}

	public RestArticle( Integer id, String title, String text, String image, Long categoryId, Long assetId ) {
		setId( id );
		setTitle( title );
		setText( text );
		setImage( image );
		/*setCategory(new RestCategory());
		getCategory().setId(categoryId);
		setAsset(new RestAsset());
		getAsset().setId(assetId);*/
	}

	public RestArticle( Article article ) {
		setId( article.getId() );
		setCategoryId( article.getCategoryId() );
		setAssetId( article.getAssetId() );
		setTitle( article.getTitle() );
		setText( article.getText() );
		setText2( article.getText2() );
		setType( article.getType() );
		setPlatform( article.getPlatform() );

		for( ArticleImage image : article.getArticleImages() ) {
			RestArticleImage restImage = new RestArticleImage();
			restImage.setSrc( image.getSrc() );
			restImage.setTile( image.getTitle() );
			restImage.setType( image.getType() );
			getArticleImages().addArticleImage( restImage );
		}

	}

	public Integer getId() {
		return id;
	}

	public void setId( Integer id ) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public String getText() {
		return text;
	}

	public void setText( String text ) {
		this.text = text;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId( Long categoryId ) {
		this.categoryId = categoryId;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId( Long assetId ) {
		this.assetId = assetId;
	}

	public RestObject getCategoryUri() {
		if( categoryUri == null ) {
			categoryUri = new RestObject();
		}
		return categoryUri;
	}

	public RestObject getAssetUri() {
		if( assetUri == null ) {
			assetUri = new RestObject();
		}
		return assetUri;
	}

	public String getType() {
		return type;
	}

	public void setType( String type ) {
		this.type = type;
	}

	public String getText2() {
		return text2;
	}

	public void setText2( String text2 ) {
		this.text2 = text2;
	}

	public String getImage() {
		return image;
	}

	public void setImage( String image ) {
		this.image = image;
	}

	public RestArticleImageList getArticleImages() {
		return articleImages;
	}

	public RestObject getPlayUri() {
		if( playUri == null ) {
			playUri = new RestObject();
		}
		return playUri;
	}

}
