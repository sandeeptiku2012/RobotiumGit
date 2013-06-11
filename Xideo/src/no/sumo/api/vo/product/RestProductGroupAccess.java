package no.sumo.api.vo.product;

import no.sumo.api.contracts.IProductGroupAccess;
import no.sumo.api.entity.sumo.enums.ProductGroupAccessType;
import no.sumo.api.entity.vman.enums.PlatformId;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "productGroupAccess" )
public class RestProductGroupAccess extends RestObject implements IProductGroupAccess {
	@Attribute
	private Long id;

	@Element
	private Long treeId;

	@Element
	private PlatformId platformId;

	@Element
	private Long productGroupId;

	@Element
	private ProductGroupAccessType productGroupAccessType;

	@Element
	private Boolean enabled;

	public RestProductGroupAccess() {
	}

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public Long getTreeId() {
		return treeId;
	}

	@Override
	public void setTreeId(Long treeId) {
		this.treeId = treeId;
	}

	@Override
	public PlatformId getPlatformId() {
		return platformId;
	}

	@Override
	public void setPlatformId(PlatformId platformId) {
		this.platformId = platformId;
	}

	@Override
	public Long getProductGroupId() {
		return productGroupId;
	}

	@Override
	public void setProductGroupId(Long productGroupId) {
		this.productGroupId = productGroupId;
	}

	@Override
	public ProductGroupAccessType getProductGroupAccessType() {
		return productGroupAccessType;
	}

	@Override
	public void setProductGroupAccessType(ProductGroupAccessType productGroupAccessType) {
		this.productGroupAccessType = productGroupAccessType;
	}

	@Override
	public Boolean getEnabled() {
		return enabled;
	}

	@Override
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
}
