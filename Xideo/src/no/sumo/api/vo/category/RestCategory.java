package no.sumo.api.vo.category;

import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import no.sumo.api.contracts.ConversionFactory;
import no.sumo.api.contracts.IArticle;
import no.sumo.api.contracts.IAsset;
import no.sumo.api.contracts.ICategory;
import no.sumo.api.vo.RestObject;
import no.sumo.api.vo.article.RestArticle;
import no.sumo.api.vo.article.RestArticleList;
import no.sumo.api.vo.asset.RestAsset;
import no.sumo.api.vo.asset.RestAssetList;
import no.sumo.api.vo.metadata.RestMetadata;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "category" )
public class RestCategory extends RestObject implements ICategory {
	@Attribute( name = "id", required = true )
	private long id;

	@Element( name = "assetCount", required = false )
	private long assetCount;

	private long viewCount;

	@Transient
	private List<RestArticle> articles;

	private String geoRegion;

	private long layoutTemplate;

	@Element( name = "level", required = false )
	private String level;

	@Element(name = "levelTypeId", required = false)
	private int levelTypeId;

	@Element( name = "parent", required = false )
	private RestCategory parent;

	@Element( name = "parentId", required = false )
	private long parentId;

	private Integer priority;

	@Attribute( name = "ghostId", required = false )
	private long ghostId;

	private Hashtable<String, String> properties;

	@Element( name = "title", required = false )
	private String title;

	private long visualTemplate;

	private long sectionId;

	private long programTypeId;

	@Element( name = "favoritable", required = false )
	private Boolean favoritable;

	@Element( name = "categories", required = false )
	private RestCategoryList childCategory;

	@Element( name = "assets", required = false )
	private RestAssetList assets;

	@Element( name = "articles", required = false )
	private RestArticleList articleList;

	@Transient
	private RestCategoryList ancestorCategoriesUri = new RestCategoryList();

	@Element( name = "section", required = false )
	private RestObject sectionUri;

	@Element( name = "metadata", required = false )
	private RestMetadata metadata;

	@Element( name = "labeledAsFree", required = false )
	private Boolean freeLabeled;

	@Element( name = "autoExpireDate", required = false )
	private Date autoExpireDate;

	@Element( name = "autoExpireOffset", required = false )
	private Integer autoExpireOffset;

	@Element( name = "categoryPath", required = false )
	private String categoryPath;

	public long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public List<IArticle> getArticles() {
		return ConversionFactory.getArticleConverter().transfer( articles );
	}

	public void setArticles( List<IArticle> articles ) {
		this.articles = ConversionFactory.getArticleConverter().transfer( articles, RestArticle.class );
	}

	public String getGeoRegion() {
		return geoRegion;
	}

	public void setGeoRegion( String geoRegion ) {
		this.geoRegion = geoRegion;
	}

	public String getCategoryPath() {
		return categoryPath;
	}

	public void setCategoryPath( String categoryPath ) {
		this.categoryPath = categoryPath != null ? categoryPath : "";
	}

	public Long getLayoutTemplate() {
		return layoutTemplate;
	}

	public void setLayoutTemplate( Long layoutTemplate ) {
		this.layoutTemplate = layoutTemplate;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel( String level ) {
		this.level = level != null ? level.toUpperCase() : "";
	}

	public int getLevelTypeId() {
		return levelTypeId;
	}

	public void setLevelTypeId( int levelTypeId ) {
		this.levelTypeId = levelTypeId;
	}

	public RestObject getParentCategoryUri() {
		getParent().setId( getParentId() );

		return getParent();
	}

	public RestCategory getParent() {
		if( parent == null ) {
			parent = new RestCategory();
		}

		return parent;
	}

	public void setParent( ICategory parent ) {
		if( parent != null ) {
			if( !(parent instanceof RestCategory) ) {
				RestCategory cat = getParent();

				ConversionFactory.getProgramTypeTreeConverter().transfer( parent, cat );

				this.parent = cat;
			} else {
				this.parent = (RestCategory)parent;
			}
		} else {
			this.parent = null;
		}
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId( Long parentId ) {
		this.parentId = parentId;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority( Integer priority ) {
		this.priority = priority;
	}

	public Hashtable<String, String> getProperties() {
		return properties;
	}

	public void setProperties( Hashtable<String, String> properties ) {
		this.properties = properties;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public Long getVisualTemplate() {
		return visualTemplate;
	}

	public void setVisualTemplate( Long visualTemplate ) {
		this.visualTemplate = visualTemplate;
	}

	public RestArticleList getArticleList() {
		if( articleList == null ) {
			articleList = new RestArticleList();
		}

		return articleList;
	}

	public void setArticleList( RestArticleList articleList ) {
		this.articleList = articleList;
	}

	private RestAssetList getAssetList() {
		if( assets == null ) {
			assets = new RestAssetList();
		}

		return assets;
	}

	public List<RestAsset> getAssets() {
		return getAssetList().getAssets();
	}

	public void setAssets( List<? extends IAsset> assets ) {
		List<RestAsset> data = ConversionFactory.getProgramConverter().transfer( assets, RestAsset.class );

		getAssetList().setAssets( data );
	}

	public RestObject getAssetsUri() {
		return getAssetList();
	}

	public RestObject getAncestorCategories() {
		return ancestorCategoriesUri;
	}

	public Long getSectionId() {
		return sectionId;
	}

	public void setSectionId( Long sectionId ) {
		this.sectionId = sectionId;
	}

	public RestObject getSectionUri() {
		if( sectionUri == null ) {
			sectionUri = new RestObject();
		}

		return sectionUri;
	}

	public void setSectionUri( RestObject sectionUri ) {
		this.sectionUri = sectionUri;
	}

	public Long getProgramTypeId() {
		return programTypeId;
	}

	public void setProgramTypeId( Long programTypeId ) {
		this.programTypeId = programTypeId;
	}

	private RestCategoryList getChildCategory() {
		if( childCategory == null ) {
			childCategory = new RestCategoryList();
		}

		return childCategory;
	}

	public RestObject getCategoriesUri() {
		return getChildCategory();
	}

	public List<RestCategory> getChildren() {
		return getChildCategory().getCategories();
	}

	public void setChildren( List<? extends ICategory> children ) {
		List<RestCategory> data = ConversionFactory.getProgramTypeTreeConverter().transfer( children, RestCategory.class );

		getChildCategory().setCategories( data );
	}

	/*
	public RestCategory getGhostedBy() {
		return ghost;
	}

	public void setGhostedBy(ICategory ghost) {
		if(ghost != null){
			if(!(ghost instanceof RestCategory)){
				RestCategory cat = new RestCategory();

				ConversionFactory
					.getProgramTypeTreeConverter()
					.transfer(ghost, cat);

				this.ghost = cat;
			} else {
				this.ghost = (RestCategory)ghost;
			}
		}
	}
	*/
	public Long getGhostTreeId() {
		return ghostId;
	}

	public void setGhostTreeId( Long ghostId ) {
		this.ghostId = ghostId;
	}

	public Long getAssetCount() {
		return assetCount;
	}

	public void setAssetCount( Long assetCount ) {
		this.assetCount = assetCount;
	}

	public Long getViewCount() {
		return viewCount;
	}

	public void setViewCount( Long viewCount ) {
		this.viewCount = viewCount;
	}

	public RestMetadata getMetadata() {
		if( metadata == null ) {
			metadata = new RestMetadata();
		}

		return metadata;
	}

	public void setMetadata( RestMetadata metadata ) {
		this.metadata = metadata;
	}

	public Boolean getFreeLabeled() {
		return freeLabeled;
	}

	public void setFreeLabeled( Boolean freeLabeled ) {
		this.freeLabeled = freeLabeled;
	}

	public Boolean getFavoritable() {
		return favoritable;
	}

	public void setFavoritable( Boolean favoritable ) {
		this.favoritable = favoritable;
	}

	public Date getAutoExpireDate() {
		return autoExpireDate;
	}

	public void setAutoExpireDate( Date autoExpireDate ) {
		this.autoExpireDate = autoExpireDate;
	}

	public Integer getAutoExpireOffset() {
		return autoExpireOffset;
	}

	public void setAutoExpireOffset( Integer autoExpireOffset ) {
		this.autoExpireOffset = autoExpireOffset;
	}

	public boolean getIsTopLevelCategory() {
		return "MAIN_CATEGORY".equals( level );
	}

	public boolean getIsGenre() {
		return "MAIN_CATEGORY".equalsIgnoreCase( level );
	}

	public boolean getIsCategory() {
		return "CATEGORY".equalsIgnoreCase( level );
	}

	public boolean getIsSubGenre() {
		return "CATEGORY".equalsIgnoreCase( level );
	}

	public boolean getIsChannel() {
		return "SHOW".equalsIgnoreCase( level );
	}

	public boolean getIsShow() {
		return "SUB_SHOW".equalsIgnoreCase( level );
	}

	public String getDefaultImagePackReference() {
		return metadata.get( "image-pack" );
	}
}
