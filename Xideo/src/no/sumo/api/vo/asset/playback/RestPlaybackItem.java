package no.sumo.api.vo.asset.playback;

import no.sumo.api.contracts.IPlaybackItem;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "item" )
@Default( DefaultType.FIELD )
public class RestPlaybackItem extends RestObject implements IPlaybackItem {

	@Element( name = "license", required = false )
	private RestObject drmLicenseUri;

	@Element( name = "log" )
	private RestObject logUri;

	private Integer bitrate;
	private String mediaFormat;
	private String scheme;
	private String server;
	private String base;
	String url;

	@Element( required = false )
	Long fileId;

	public RestObject getLogUri() {
		if( logUri == null ) {
			logUri = new RestObject();
		}

		return logUri;
	}

	public RestObject getDrmLicenseUri() {
		if( drmLicenseUri == null ) {
			drmLicenseUri = new RestObject();
		}
		return drmLicenseUri;
	}

	public Long getFileId() {
		return fileId;
	}

	public void setFileId( Long fileId ) {
		this.fileId = fileId;
	}

	public void setDrmLicenseUri( RestObject drmLicenseUri ) {
		this.drmLicenseUri = drmLicenseUri;
	}

	public Integer getBitrate() {
		return bitrate;
	}

	public void setBitrate( Integer bitrate ) {
		this.bitrate = bitrate;
	}

	public String getMediaFormat() {
		return mediaFormat;
	}

	public void setMediaFormat( String mediaFormat ) {
		this.mediaFormat = mediaFormat;
	}

	public String getScheme() {
		return scheme;
	}

	public void setScheme( String scheme ) {
		this.scheme = scheme;
	}

	public String getServer() {
		return server;
	}

	public void setServer( String server ) {
		this.server = server;
	}

	public String getBase() {
		return base;
	}

	public void setBase( String base ) {
		this.base = base;
	}

	@Override
	public String getUrl() {
		return url;
	}

	public void setUrl( String url ) {
		this.url = url;
	}

	public RestPlaybackItem() {
	}
}
