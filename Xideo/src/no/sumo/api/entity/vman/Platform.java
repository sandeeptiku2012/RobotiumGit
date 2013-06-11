package no.sumo.api.entity.vman;

import java.io.Serializable;

public class Platform implements Serializable {

	private static final long serialVersionUID = 1727740385771816426L;

	private Integer id;
	private String name;
	private Integer moduleId;
	private String moduleClass;
	private Integer priority;
	private String sectionName;
	private String apiName;

	public Platform() {

	}

	public Platform( Integer id, String apiName ) {
		this.id = id;
		setApiName( apiName );
	}

	public Integer getId() {
		return this.id;
	}

	public void setId( Integer id ) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId( Integer moduleId ) {
		this.moduleId = moduleId;
	}

	public String getModuleClass() {
		return moduleClass;
	}

	public void setModuleClass( String moduleClass ) {
		this.moduleClass = moduleClass;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority( Integer priority ) {
		this.priority = priority;
	}

	public String getSectionName() {
		return sectionName;
	}

	public void setSectionName( String sectionName ) {
		this.sectionName = sectionName;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName( String apiName ) {
		this.apiName = apiName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (apiName == null ? 0 : apiName.hashCode());
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (moduleClass == null ? 0 : moduleClass.hashCode());
		result = prime * result + (moduleId == null ? 0 : moduleId.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (priority == null ? 0 : priority.hashCode());
		result = prime * result + (sectionName == null ? 0 : sectionName.hashCode());
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
		Platform other = (Platform)obj;
		if( apiName == null ) {
			if( other.apiName != null ) {
				return false;
			}
		} else if( !apiName.equals( other.apiName ) ) {
			return false;
		}
		if( id == null ) {
			if( other.id != null ) {
				return false;
			}
		} else if( !id.equals( other.id ) ) {
			return false;
		}
		if( moduleClass == null ) {
			if( other.moduleClass != null ) {
				return false;
			}
		} else if( !moduleClass.equals( other.moduleClass ) ) {
			return false;
		}
		if( moduleId == null ) {
			if( other.moduleId != null ) {
				return false;
			}
		} else if( !moduleId.equals( other.moduleId ) ) {
			return false;
		}
		if( name == null ) {
			if( other.name != null ) {
				return false;
			}
		} else if( !name.equals( other.name ) ) {
			return false;
		}
		if( priority == null ) {
			if( other.priority != null ) {
				return false;
			}
		} else if( !priority.equals( other.priority ) ) {
			return false;
		}
		if( sectionName == null ) {
			if( other.sectionName != null ) {
				return false;
			}
		} else if( !sectionName.equals( other.sectionName ) ) {
			return false;
		}
		return true;
	}
}