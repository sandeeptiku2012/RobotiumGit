package no.sumo.api.vo.profile;

import no.sumo.api.contracts.IUserProperty;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "property" )
@Default( DefaultType.FIELD )
public class RestUserProperty extends RestObject implements IUserProperty {
	@Attribute( name = "id" )
	private Long id;

	@Element( name = "value" )
	private String value;

	@Element( name = "name" )
	private String name;

	@Transient
	private Boolean hidden;

	@Transient
	private Long userId;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId( Long userId ) {
		this.userId = userId;
	}

	public String getValue() {
		return value;
	}

	public void setValue( String value ) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public Boolean getHidden() {
		return hidden;
	}

	public void setHidden( Boolean hidden ) {
		this.hidden = hidden;
	}
}
