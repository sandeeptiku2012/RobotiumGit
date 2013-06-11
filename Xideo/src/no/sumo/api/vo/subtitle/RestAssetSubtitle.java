package no.sumo.api.vo.subtitle;

import no.sumo.api.entity.subtitle.enums.SubtitleType;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "subtitles" )
@Default( DefaultType.FIELD )
public class RestAssetSubtitle extends RestObject {

	@Transient
	private Long id;

	@Transient
	private Long assetId;

	@Attribute
	private String contentType;

	@Attribute
	private String name;

	@Attribute
	private String locale;

	@Attribute
	private SubtitleType type;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId( Long assetId ) {
		this.assetId = assetId;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType( String contentType ) {
		this.contentType = contentType;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale( String locale ) {
		this.locale = locale;
	}

	public SubtitleType getType() {
		return type;
	}

	public void setType( SubtitleType type ) {
		this.type = type;
	}
}
