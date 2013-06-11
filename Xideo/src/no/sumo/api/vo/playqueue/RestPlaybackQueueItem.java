package no.sumo.api.vo.playqueue;

import java.util.Date;

import no.sumo.api.contracts.IPlaybackQueueItem;
import no.sumo.api.entity.vman.Program;
import no.sumo.api.vo.RestObject;
import no.sumo.api.vo.asset.RestAsset;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "playqueueitem" )
public class RestPlaybackQueueItem extends RestObject implements IPlaybackQueueItem {
	@Attribute( required = false )
	private Long id;

	private Long programItemId;
	private Boolean addedByUser = true;
	private Boolean seenByUser = false;

	@Transient
	private Boolean deletedByUser = false;
	
	@Element( name = "asset", required = false )
	private RestAsset asset;

	@Attribute( required = false )
	private Long indexOfItem;
	
	public RestAsset getAsset() {
		return asset;
	}

	public void setAsset( RestAsset asset ) {
		this.asset = asset;
	}
//	<asset id="2177347" channelId="1" categoryId="127" uri="/api/web/asset/2177347">
//	<archive>true</archive>
//	<aspect16x9>true</aspect16x9>
//	<assetTypeId>0</assetTypeId>
//	<autoEncode>false</autoEncode>
//	<autoPublish>false</autoPublish>
//	<category uri="/api/web/category/127"/>
//	<copyLiveStream>true</copyLiveStream>
//	<createTime>2012-04-18T02:55:25-07:00</createTime>
//	<deleted>false</deleted>
//	<distributed>0</distributed>
//	<drmProtected>false</drmProtected>
//	<duration>0</duration>

	

	private Date addDate = new Date();

	@Transient
	private Boolean filteredByPlatform = false;

	@Transient
	private Boolean deletedBySystem = false;

	@Transient
	private boolean dirty = false;

	@Transient
	private Program program;
	private Date expireDate;
	private Boolean free;

	private String assetUri;

	@Element( name = "assetName" )
	private String assetName;

	@Element( name = "progId" )
	private Long progId;

	public String getAssetName() {
		return assetName;
	}

	public void setDirty( boolean dirty ) {
		this.dirty = dirty;
	}

	public boolean isDirty() {
		return dirty;
	}

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}


	public Long getProgramId() {
		return progId;
	}

	public Long getProgramItemId() {
		return programItemId;
	}

	public void setProgramItemId( Long programItemId ) {
		this.programItemId = programItemId;
	}

	public boolean getAddedByUser() {
		return addedByUser;
	}

	public void setAddedByUser( boolean addedByUser ) {
		this.addedByUser = addedByUser;
	}

	public void setSeenByUser( boolean seenByUser ) {
		this.seenByUser = seenByUser;
	}

	public boolean getSeenByUser() {
		return seenByUser;
	}

	public void setDeletedByUser( boolean deletedByUser ) {
		this.deletedByUser = deletedByUser;
	}

	public boolean getDeletedByUser() {
		return deletedByUser;
	}

	public Long getIndexOfItem() {
		return indexOfItem;
	}

	public void setIndexOfItem( Long indexOfItem ) {
		this.indexOfItem = indexOfItem;
	}

	public Date getAddDate() {
		return addDate;
	}

	public void setAddDate( Date addDate ) {
		this.addDate = addDate;
	}

	public Boolean getFilteredByPlatform() {
		return filteredByPlatform;
	}

	public void setFilteredByPlatform( Boolean filteredByPlatform ) {
		this.filteredByPlatform = filteredByPlatform;
	}

	public void setDeletedBySystem( Boolean deletedBySystem ) {
		this.deletedBySystem = deletedBySystem;
	}

	public Boolean getDeletedBySystem() {
		return deletedBySystem;
	}

	public String getTitle() {
		return program.getTitle();
	}

	public String getDescription() {
		return program.getText();
	}

	public String getChannelId() {
		return program.getChannelId().toString();
	}

	public Date getTxDate() {
		return program.getTxTime();
	}

	public double getLength() {
		if( program.getTimeEnd() != null && program.getTimeBegin() != null ) {
			return program.getTimeEnd() - program.getTimeBegin();
		}
		return 0;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate( Date expireDate ) {
		this.expireDate = expireDate;
	}

	public void setProgram( Program program ) {
		this.program = program;
	}

	public Program getProgram() {
		return program;
	}

	public void setFree( Boolean free ) {
		this.free = free;
	}

	public Boolean getFree() {
		return free;
	}

	public Boolean isHiddenOnPlatform() {
		return deletedBySystem || deletedByUser || filteredByPlatform || seenByUser;
	}

	public Boolean isHiddenAlways() {
		return deletedBySystem || deletedByUser || seenByUser;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder( "[" );
		if( programItemId == null ) {
			sb.append( "episode " );
			sb.append( program.getId() ).append( "; " );
		}
		if( programItemId != null ) {
			sb.append( "clip " );
			sb.append( program.getId() ).append( ", " );
			sb.append( programItemId ).append( "; " );
		}
		//sb.append("index ").append(indexOfItem).append("; ");
		//sb.append("deletedByUser ").append(deletedByUser).append("; ");
		sb.append( "title " ).append( getTitle() ).append( "; " );
		sb.append( "desc " ).append( getDescription() ).append( "; " );
		sb.append( "]" );
		return sb.toString();
	}

	public void setAssetUri( String assetUri ) {
		this.assetUri = assetUri;
	}

	public String getAssetUri() {
		return assetUri;
	}

}
