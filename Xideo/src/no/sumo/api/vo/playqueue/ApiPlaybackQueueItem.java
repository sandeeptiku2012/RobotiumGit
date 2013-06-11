package no.sumo.api.vo.playqueue;

import java.util.Date;

import no.sumo.api.contracts.IPlaybackQueueItem;
import no.sumo.api.entity.vman.Program;

public class ApiPlaybackQueueItem implements IPlaybackQueueItem {

	private Long id;
	private Long programItemId;
	private Boolean addedByUser = true;
	private Boolean seenByUser = false;
	private Boolean deletedByUser = false;
	private Long indexOfItem;
	private Date addDate = new Date();
	private Boolean filteredByPlatform = false;
	private Boolean deletedBySystem = false;
	private boolean dirty = false;
	private Program program;
	private Date expireDate;
	private Boolean free;

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
		return program.getId();
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

}