package no.sumo.api.vo.product;

import no.sumo.api.contracts.IProductGroup;
import no.sumo.api.entity.sumo.enums.ProductGroupAccessType;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "productGroup" )
@Default( value = DefaultType.FIELD, required = false )
public class RestProductGroup extends RestObject implements IProductGroup {
	@Element( name = "description", required = false )
	private String description;

	@Attribute
	private Long id;

	@Element( name = "name", required = false )
	private String name;

	private int sortIndex;

	@Element( name = "categories", required = false )
	private RestObject categoriesUri;

	@Element( name = "products", required = false )
	private RestObject productsUri;

	private ProductGroupAccessType accessType;

	public RestProductGroup() {

	}

	public void setAccessType( ProductGroupAccessType accessType ) {
		this.accessType = accessType;
	}

	public ProductGroupAccessType getAccessType() {
		return accessType;
	}

	public RestObject getCategoriesUri() {
		if( categoriesUri == null ) {
			categoriesUri = new RestObject();
		}
		return categoriesUri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public int getSortIndex() {
		return sortIndex;
	}

	public void setSortIndex( int sortIndex ) {
		this.sortIndex = sortIndex;
	}

	public RestObject getProductsUri() {
		if( productsUri == null ) {
			productsUri = new RestObject();
		}
		return productsUri;
	}
}
