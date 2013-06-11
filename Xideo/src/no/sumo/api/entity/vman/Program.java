package no.sumo.api.entity.vman;

import java.util.Date;
import java.util.List;

import no.sumo.api.entity.sumo.ProgramViewCount;
import no.sumo.api.entity.vman.enums.ProgramRestriction;

import org.simpleframework.xml.Transient;

public class Program {

	private static final long serialVersionUID = -6876715785169233688L;

	private Long id;

	private Boolean aspect16x9;

	private Boolean autoPublish;

	private Long categoryId;

	private Long channelId;

	private String comments;

	private Date createTime;

	private Boolean deleted;

	private Integer distributed;

	private Boolean drm;

	private Long encoderGroupId;

	private Long hostProgramId;

	private Boolean indexed;

	private String keywords;

	/**
	 * Use ProgramPlatform instead
	 */
	@Deprecated
	private Boolean live;

	/**
	 * Use ProgramPlatform instead
	 */
	@Deprecated
	private Boolean livePublished;

	private String picture;

	private Integer priority;

	private Long programTypeId;

	private Boolean published;

	private ProgramRestriction restriction;

	private String subtitle;

	private String text;

	private Double timeBegin;

	private Double timeEnd;

	private String title;

	private Date txTime;

	private Date updateTime;

	private Date encoderStartTime;

	private Date encoderStopTime;

	private Long duration;

	private Boolean autoEncode;

	@Transient
	private long treeId;

	/*@OneToOne
	@JoinColumn(name="TYPE",
	                referencedColumnName="PROGRAM_TYPE_ID",
	                insertable=false,
	                updatable=false
	)
	private ProgramTypeTree tree;*/

	private ProgramViewCount viewcount;

	private List<ProgramPublished> programPublisheds;

	public Boolean getAspect16x9() {
		return aspect16x9;
	}

	public Boolean getAutoPublish() {
		return autoPublish;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public Long getChannelId() {
		return channelId;
	}

	public String getComments() {
		return comments;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public Integer getDistributed() {
		return distributed;
	}

	public Boolean getDrm() {
		return drm;
	}

	public Long getEncoderGroupId() {
		return encoderGroupId;
	}

	public Long getHostProgramId() {
		return hostProgramId;
	}

	public Long getId() {
		return id;
	}

	public Boolean getIndexed() {
		return indexed;
	}

	public String getKeywords() {
		return keywords;
	}

	/**
	 * Use ProgramPlatform instead
	 */
	@Deprecated
	public Boolean getLive() {
		return live;
	}

	/**
	 * Use ProgramPlatform instead
	 */
	@Deprecated
	public Boolean getLivePublished() {
		return livePublished;
	}

	public String getPicture() {
		return picture;
	}

	public Integer getPriority() {
		return priority;
	}

	public Long getProgramTypeId() {
		return programTypeId;
	}

	public Boolean getPublished() {
		return published;
	}

	public ProgramRestriction getRestriction() {
		return restriction;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public String getText() {
		return text;
	}

	public Double getTimeBegin() {
		return timeBegin;
	}

	public Double getTimeEnd() {
		return timeEnd;
	}

	public String getTitle() {
		return title;
	}

	public Date getTxTime() {
		return txTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	/*public ProgramTypeTree getTree() {
	        return tree;
	}
	public void setTree(ProgramTypeTree tree) {
	        this.tree = tree;
	}*/
	public Date getEncoderStartTime() {
		return encoderStartTime;
	}

	public Date getEncoderStopTime() {
		return encoderStopTime;
	}

	public Long getDuration() {
		return duration;
	}

	public Boolean getAutoEncode() {
		return autoEncode;
	}

	public void setAspect16x9( Boolean aspect16x9 ) {
		this.aspect16x9 = aspect16x9;
	}

	public void setAutoPublish( Boolean autoPublish ) {
		this.autoPublish = autoPublish;
	}

	public void setCategoryId( Long categoryId ) {
		this.categoryId = categoryId;
	}

	public void setChannelId( Long channelId ) {
		this.channelId = channelId;
	}

	public void setComments( String comments ) {
		this.comments = comments;
	}

	public void setCreateTime( Date createTime ) {
		this.createTime = createTime;
	}

	public void setDeleted( Boolean deleted ) {
		this.deleted = deleted;
	}

	public void setDistributed( Integer distributed ) {
		this.distributed = distributed;
	}

	public void setDrm( Boolean drm ) {
		this.drm = drm;
	}

	public void setEncoderGroupId( Long encoderGroupId ) {
		this.encoderGroupId = encoderGroupId;
	}

	public void setHostProgramId( Long hostProgramId ) {
		this.hostProgramId = hostProgramId;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public void setIndexed( Boolean indexed ) {
		this.indexed = indexed;
	}

	public void setKeywords( String keywords ) {
		this.keywords = keywords;
	}

	public void setLive( Boolean live ) {
		this.live = live;
	}

	public void setLivePublished( Boolean livePublished ) {
		this.livePublished = livePublished;
	}

	public void setPicture( String picture ) {
		this.picture = picture;
	}

	public void setPriority( Integer priority ) {
		this.priority = priority;
	}

	public void setProgramTypeId( Long programTypeId ) {
		this.programTypeId = programTypeId;
	}

	public void setPublished( Boolean published ) {
		this.published = published;
	}

	public void setRestriction( ProgramRestriction restricted ) {
		this.restriction = restricted;
	}

	public void setSubtitle( String subtitle ) {
		this.subtitle = subtitle;
	}

	public void setText( String text ) {
		this.text = text;
	}

	public void setTimeBegin( Double timeBegin ) {
		this.timeBegin = timeBegin;
	}

	public void setTimeEnd( Double timeEnd ) {
		this.timeEnd = timeEnd;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public void setTxTime( Date txTime ) {
		this.txTime = txTime;
	}

	public void setUpdateTime( Date updateTime ) {
		this.updateTime = updateTime;
	}

	public void setEncoderStartTime( Date encoderStartTime ) {
		this.encoderStartTime = encoderStartTime;
	}

	public void setEncoderStopTime( Date encoderStopTime ) {
		this.encoderStopTime = encoderStopTime;
	}

	public void setDuration( Long duration ) {
		this.duration = duration;
	}

	public void setAutoEncode( Boolean autoEncode ) {
		this.autoEncode = autoEncode;
	}

	public Long getViewcount() {
		return viewcount == null ? 0L : viewcount.getViewCount();
	}

	public void setViewcount( Long viewcount ) {
		if( this.viewcount != null ) {
			this.viewcount.setViewCount( viewcount );
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (aspect16x9 == null ? 0 : aspect16x9.hashCode());
		result = prime * result + (autoEncode == null ? 0 : autoEncode.hashCode());
		result = prime * result + (autoPublish == null ? 0 : autoPublish.hashCode());
		result = prime * result + (categoryId == null ? 0 : categoryId.hashCode());
		result = prime * result + (channelId == null ? 0 : channelId.hashCode());
		result = prime * result + (comments == null ? 0 : comments.hashCode());
		result = prime * result + (createTime == null ? 0 : createTime.hashCode());
		result = prime * result + (deleted == null ? 0 : deleted.hashCode());
		result = prime * result + (distributed == null ? 0 : distributed.hashCode());
		result = prime * result + (drm == null ? 0 : drm.hashCode());
		result = prime * result + (duration == null ? 0 : duration.hashCode());
		result = prime * result + (encoderGroupId == null ? 0 : encoderGroupId.hashCode());
		result = prime * result + (encoderStartTime == null ? 0 : encoderStartTime.hashCode());
		result = prime * result + (encoderStopTime == null ? 0 : encoderStopTime.hashCode());
		result = prime * result + (hostProgramId == null ? 0 : hostProgramId.hashCode());
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (indexed == null ? 0 : indexed.hashCode());
		result = prime * result + (keywords == null ? 0 : keywords.hashCode());
		result = prime * result + (live == null ? 0 : live.hashCode());
		result = prime * result + (livePublished == null ? 0 : livePublished.hashCode());
		result = prime * result + (picture == null ? 0 : picture.hashCode());
		result = prime * result + (priority == null ? 0 : priority.hashCode());
		result = prime * result + (programTypeId == null ? 0 : programTypeId.hashCode());
		result = prime * result + (published == null ? 0 : published.hashCode());
		result = prime * result + (restriction == null ? 0 : restriction.hashCode());
		result = prime * result + (subtitle == null ? 0 : subtitle.hashCode());
		result = prime * result + (text == null ? 0 : text.hashCode());
		result = prime * result + (timeBegin == null ? 0 : timeBegin.hashCode());
		result = prime * result + (timeEnd == null ? 0 : timeEnd.hashCode());
		result = prime * result + (title == null ? 0 : title.hashCode());
		result = prime * result + (int)(treeId ^ treeId >>> 32);
		result = prime * result + (txTime == null ? 0 : txTime.hashCode());
		result = prime * result + (updateTime == null ? 0 : updateTime.hashCode());
		result = prime * result + (viewcount == null ? 0 : viewcount.hashCode());
		return result;
	}

	@Override
	public boolean equals( Object obj ) {
		if( this == obj ) {
			return true;
		}
		if( obj == null ) {
			return false;
		}
		if( getClass() != obj.getClass() ) {
			return false;
		}
		Program other = (Program)obj;
		if( aspect16x9 == null ) {
			if( other.aspect16x9 != null ) {
				return false;
			}
		} else if( !aspect16x9.equals( other.aspect16x9 ) ) {
			return false;
		}
		if( autoEncode == null ) {
			if( other.autoEncode != null ) {
				return false;
			}
		} else if( !autoEncode.equals( other.autoEncode ) ) {
			return false;
		}
		if( autoPublish == null ) {
			if( other.autoPublish != null ) {
				return false;
			}
		} else if( !autoPublish.equals( other.autoPublish ) ) {
			return false;
		}
		if( categoryId == null ) {
			if( other.categoryId != null ) {
				return false;
			}
		} else if( !categoryId.equals( other.categoryId ) ) {
			return false;
		}
		if( channelId == null ) {
			if( other.channelId != null ) {
				return false;
			}
		} else if( !channelId.equals( other.channelId ) ) {
			return false;
		}
		if( comments == null ) {
			if( other.comments != null ) {
				return false;
			}
		} else if( !comments.equals( other.comments ) ) {
			return false;
		}
		if( createTime == null ) {
			if( other.createTime != null ) {
				return false;
			}
		} else if( !createTime.equals( other.createTime ) ) {
			return false;
		}
		if( deleted == null ) {
			if( other.deleted != null ) {
				return false;
			}
		} else if( !deleted.equals( other.deleted ) ) {
			return false;
		}
		if( distributed == null ) {
			if( other.distributed != null ) {
				return false;
			}
		} else if( !distributed.equals( other.distributed ) ) {
			return false;
		}
		if( drm == null ) {
			if( other.drm != null ) {
				return false;
			}
		} else if( !drm.equals( other.drm ) ) {
			return false;
		}
		if( duration == null ) {
			if( other.duration != null ) {
				return false;
			}
		} else if( !duration.equals( other.duration ) ) {
			return false;
		}
		if( encoderGroupId == null ) {
			if( other.encoderGroupId != null ) {
				return false;
			}
		} else if( !encoderGroupId.equals( other.encoderGroupId ) ) {
			return false;
		}
		if( encoderStartTime == null ) {
			if( other.encoderStartTime != null ) {
				return false;
			}
		} else if( !encoderStartTime.equals( other.encoderStartTime ) ) {
			return false;
		}
		if( encoderStopTime == null ) {
			if( other.encoderStopTime != null ) {
				return false;
			}
		} else if( !encoderStopTime.equals( other.encoderStopTime ) ) {
			return false;
		}
		if( hostProgramId == null ) {
			if( other.hostProgramId != null ) {
				return false;
			}
		} else if( !hostProgramId.equals( other.hostProgramId ) ) {
			return false;
		}
		if( id == null ) {
			if( other.id != null ) {
				return false;
			}
		} else if( !id.equals( other.id ) ) {
			return false;
		}
		if( indexed == null ) {
			if( other.indexed != null ) {
				return false;
			}
		} else if( !indexed.equals( other.indexed ) ) {
			return false;
		}
		if( keywords == null ) {
			if( other.keywords != null ) {
				return false;
			}
		} else if( !keywords.equals( other.keywords ) ) {
			return false;
		}
		if( live == null ) {
			if( other.live != null ) {
				return false;
			}
		} else if( !live.equals( other.live ) ) {
			return false;
		}
		if( livePublished == null ) {
			if( other.livePublished != null ) {
				return false;
			}
		} else if( !livePublished.equals( other.livePublished ) ) {
			return false;
		}
		if( picture == null ) {
			if( other.picture != null ) {
				return false;
			}
		} else if( !picture.equals( other.picture ) ) {
			return false;
		}
		if( priority == null ) {
			if( other.priority != null ) {
				return false;
			}
		} else if( !priority.equals( other.priority ) ) {
			return false;
		}
		if( programTypeId == null ) {
			if( other.programTypeId != null ) {
				return false;
			}
		} else if( !programTypeId.equals( other.programTypeId ) ) {
			return false;
		}
		if( published == null ) {
			if( other.published != null ) {
				return false;
			}
		} else if( !published.equals( other.published ) ) {
			return false;
		}
		if( restriction == null ) {
			if( other.restriction != null ) {
				return false;
			}
		} else if( !restriction.equals( other.restriction ) ) {
			return false;
		}
		if( subtitle == null ) {
			if( other.subtitle != null ) {
				return false;
			}
		} else if( !subtitle.equals( other.subtitle ) ) {
			return false;
		}
		if( text == null ) {
			if( other.text != null ) {
				return false;
			}
		} else if( !text.equals( other.text ) ) {
			return false;
		}
		if( timeBegin == null ) {
			if( other.timeBegin != null ) {
				return false;
			}
		} else if( !timeBegin.equals( other.timeBegin ) ) {
			return false;
		}
		if( timeEnd == null ) {
			if( other.timeEnd != null ) {
				return false;
			}
		} else if( !timeEnd.equals( other.timeEnd ) ) {
			return false;
		}
		if( title == null ) {
			if( other.title != null ) {
				return false;
			}
		} else if( !title.equals( other.title ) ) {
			return false;
		}
		if( treeId != other.treeId ) {
			return false;
		}
		if( txTime == null ) {
			if( other.txTime != null ) {
				return false;
			}
		} else if( !txTime.equals( other.txTime ) ) {
			return false;
		}
		if( updateTime == null ) {
			if( other.updateTime != null ) {
				return false;
			}
		} else if( !updateTime.equals( other.updateTime ) ) {
			return false;
		}
		if( viewcount == null ) {
			if( other.viewcount != null ) {
				return false;
			}
		} else if( !viewcount.equals( other.viewcount ) ) {
			return false;
		}
		return true;
	}

	protected Program clone( Program source, Program destination ) {

		destination.setTreeId( source.getTreeId() );
		destination.setAspect16x9( source.getAspect16x9() );
		destination.setAutoPublish( source.getAutoPublish() );
		destination.setCategoryId( source.getCategoryId() );
		destination.setChannelId( source.getChannelId() );
		destination.setComments( source.getComments() );
		destination.setCreateTime( source.getCreateTime() );
		destination.setDeleted( source.getDeleted() );
		destination.setDistributed( source.getDistributed() );
		destination.setDrm( source.getDrm() );
		destination.setEncoderGroupId( source.getEncoderGroupId() );
		destination.setHostProgramId( source.getHostProgramId() );
		destination.setId( source.getId() );
		destination.setIndexed( source.getIndexed() );
		destination.setKeywords( source.getKeywords() );
		destination.setLive( source.getLive() );
		destination.setLivePublished( source.getLivePublished() );
		destination.setPicture( source.getPicture() );
		destination.setPriority( source.getPriority() );
		destination.setProgramTypeId( source.getProgramTypeId() );
		destination.setPublished( source.getPublished() );
		destination.setSubtitle( source.getSubtitle() );
		destination.setText( source.getText() );
		destination.setTimeBegin( source.getTimeBegin() );
		destination.setTimeEnd( source.getTimeEnd() );
		destination.setTitle( source.getTitle() );
		destination.setTxTime( source.getTxTime() );
		destination.setUpdateTime( source.getUpdateTime() );
		destination.setRestriction( source.getRestriction() );
		destination.setEncoderStartTime( source.getEncoderStartTime() );
		destination.setDuration( source.getDuration() );
		destination.setAutoEncode( source.getAutoEncode() );
		destination.setViewcount( source.getViewcount() );

		return destination;
	}

	@Override
	public Program clone() {
		return clone( this, new Program() );
	}

	public long getTreeId() {
		return treeId;
	}

	public void setTreeId( long treeId ) {
		this.treeId = treeId;
	}

	public List<ProgramPublished> getProgramPublisheds() {
		return programPublisheds;
	}

	public void setProgramPublisheds( List<ProgramPublished> programPublisheds ) {
		this.programPublisheds = programPublisheds;
	}

}
