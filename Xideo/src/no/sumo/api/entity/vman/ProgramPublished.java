package no.sumo.api.entity.vman;

import java.io.Serializable;
import java.util.Date;

import no.sumo.api.entity.vman.enums.ProgramLiveStatus;

public class ProgramPublished implements Serializable {

	private static final long serialVersionUID = 5011637425210136065L;

	private Long id;

	private Long programId;

	private Integer platformId;

	private Boolean livePublished = Boolean.FALSE;

	private Boolean onDemandPublished = Boolean.FALSE;

	private Date expire;

	private Date publish;

	private Platform platform;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Integer getPlatformId() {
		return platformId;
	}

	public void setPlatformId( Integer platformId ) {
		this.platformId = platformId;
	}

	public Long getProgramId() {
		return programId;
	}

	public void setProgramId( Long progId ) {
		this.programId = progId;
	}

	public void setLiveAndOnDemandPublished( ProgramLiveStatus liveStatus ) {
		setLivePublished( liveStatus != ProgramLiveStatus.ONDEMAND );
		setOnDemandPublished( liveStatus != ProgramLiveStatus.LIVE );
	}

	public Boolean getLivePublished() {
		return livePublished;
	}

	public void setLivePublished( Boolean livePublished ) {
		this.livePublished = livePublished;
	}

	public Boolean getOnDemandPublished() {
		return onDemandPublished;
	}

	public void setOnDemandPublished( Boolean onDemandPublished ) {
		this.onDemandPublished = onDemandPublished;
	}

	public Date getExpire() {
		return expire;
	}

	public void setExpire( Date expire ) {
		this.expire = expire;
	}

	public Platform getPlatform() {
		return platform;
	}

	public void setPlatform( Platform platform ) {
		this.platform = platform;
	}

	public Date getPublish() {
		return publish;
	}

	public void setPublish( Date publish ) {
		this.publish = publish;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (expire == null ? 0 : expire.hashCode());
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (livePublished == null ? 0 : livePublished.hashCode());
		result = prime * result + (onDemandPublished == null ? 0 : onDemandPublished.hashCode());
		result = prime * result + (platformId == null ? 0 : platformId.hashCode());
		result = prime * result + (programId == null ? 0 : programId.hashCode());
		result = prime * result + (publish == null ? 0 : publish.hashCode());
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
		ProgramPublished other = (ProgramPublished)obj;
		if( expire == null ) {
			if( other.expire != null ) {
				return false;
			}
		} else if( !expire.equals( other.expire ) ) {
			return false;
		}
		if( id == null ) {
			if( other.id != null ) {
				return false;
			}
		} else if( !id.equals( other.id ) ) {
			return false;
		}
		if( livePublished == null ) {
			if( other.livePublished != null ) {
				return false;
			}
		} else if( !livePublished.equals( other.livePublished ) ) {
			return false;
		}
		if( onDemandPublished == null ) {
			if( other.onDemandPublished != null ) {
				return false;
			}
		} else if( !onDemandPublished.equals( other.onDemandPublished ) ) {
			return false;
		}
		if( platformId == null ) {
			if( other.platformId != null ) {
				return false;
			}
		} else if( !platformId.equals( other.platformId ) ) {
			return false;
		}
		if( programId == null ) {
			if( other.programId != null ) {
				return false;
			}
		} else if( !programId.equals( other.programId ) ) {
			return false;
		}
		if( publish == null ) {
			if( other.publish != null ) {
				return false;
			}
		} else if( !publish.equals( other.publish ) ) {
			return false;
		}
		return true;
	}

}