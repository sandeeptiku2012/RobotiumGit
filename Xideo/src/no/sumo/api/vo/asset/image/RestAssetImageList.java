package no.sumo.api.vo.asset.image;

import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root( name = "imageVersions" )
public class RestAssetImageList {
	@ElementList( inline = true, entry = "image", required=false )
	private List<RestAssetImage> images = new ArrayList<RestAssetImage>();

	public RestAssetImageList() {
	}

	public RestAssetImageList( List<RestAssetImage> images ) {
		this.images = images;
	}

	public List<RestAssetImage> getImages() {
		return images;
	}
}
