package no.sumo.api.contracts;

import no.sumo.api.entity.sumo.enums.ProductGroupAccessType;
import no.sumo.api.entity.vman.enums.PlatformId;

public interface IProductGroupAccess {
	Long getId();
	void setId(Long id);
	Long getTreeId();
	void setTreeId(Long treeId);
	PlatformId getPlatformId();
	void setPlatformId(PlatformId platformId);
	Long getProductGroupId();
	void setProductGroupId(Long productGroupId);
	ProductGroupAccessType getProductGroupAccessType();
	void setProductGroupAccessType(ProductGroupAccessType productGroupAccessType);
	Boolean getEnabled();
	void setEnabled(Boolean enabled);
}
