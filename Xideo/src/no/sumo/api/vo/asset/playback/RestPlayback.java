package no.sumo.api.vo.asset.playback;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.sumo.api.contracts.IPlayback;
import no.sumo.api.contracts.IPlaybackItem;
import no.sumo.api.session.asset.playback.PlaybackStatus;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root( name = "playback" )
@Default( DefaultType.FIELD )
public class RestPlayback extends RestObject implements IPlayback {

	@Attribute
	Long assetId;
	String title;
	Boolean live;
	Boolean aspect16x9;
	Boolean drmProtected;
	private Date liveBroadcastTime;

	PlaybackStatus playbackStatus;
	boolean hasItems;

	@Attribute( required = false )
	Double timeEnd;

	@Attribute( required = false )
	Double timeBegin;

	@Attribute( required = false )
	String drmServer;

	@ElementList( name = "items", entry = "item" )
	List<RestPlaybackItem> playbackItems;

	String logData;

	public RestPlayback() {
	}

	public PlaybackStatus getPlaybackStatus() {
		return playbackStatus;
	}

	public void setPlaybackStatus( PlaybackStatus playbackStatus ) {
		this.playbackStatus = playbackStatus;
	}

	public Long getAssetId() {
		return assetId;
	}

	public void setAssetId( Long assetId ) {
		this.assetId = assetId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public Boolean isLive() {
		return live;
	}

	public void setLive( Boolean live ) {
		this.live = live;
	}

	public Boolean isAspect16x9() {
		return aspect16x9;
	}

	public void setAspect16x9( Boolean aspect16x9 ) {
		this.aspect16x9 = aspect16x9;
	}

	public Boolean isDrmProtected() {
		return drmProtected;
	}

	public void setDrmProtected( Boolean drmProtected ) {
		this.drmProtected = drmProtected;
	}

	public String getDrmServer() {
		return drmServer;
	}

	public void setDrmServer( String drmServer ) {
		this.drmServer = drmServer;
	}

	public Double getTimeBegin() {
		return timeBegin;
	}

	public void setTimeBegin( Double timeBegin ) {
		this.timeBegin = timeBegin;
	}

	public Double getTimeEnd() {
		return timeEnd;
	}

	public void setTimeEnd( Double timeEnd ) {
		this.timeEnd = timeEnd;
	}

	public List<IPlaybackItem> getPlaybackItems() {
		if( playbackItems == null ) {
			playbackItems = new ArrayList<RestPlaybackItem>();
		}

		return (List)playbackItems;
	}

	public void setPlaybackItems( List<RestPlaybackItem> playbackItems ) {
		this.playbackItems = playbackItems;
	}

	public String getLogData() {
		return logData;
	}

	public void setLogData( String logData ) {
		this.logData = logData;
	}

	public boolean getHasItems() {
		return (this.playbackItems != null && this.playbackItems.size() > 0);
	}

	public Date getLiveBroadcastTime() {
		return liveBroadcastTime;
	}

	public void setLiveBroadcastTime( Date liveBroadcastTime ) {
		this.liveBroadcastTime = liveBroadcastTime;
	}

}