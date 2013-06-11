package no.sumo.api.vo.metadata.definitions;

import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;

@Root( name = "metadataDefinition" )
@Default( DefaultType.FIELD )
public class RestMetadataDefinition {

	private String name;
	private String title;
	private String description;
	private int sortOrder;
	private boolean required;
	private boolean searchable;
	private String validation;
	private String fieldType;
	private String dataType;
	private String options;
	private boolean enabled;
	private boolean hidden;
	private String defaultValue;

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle( String title ) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription( String description ) {
		this.description = description;
	}

	public int getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder( int sortOrder ) {
		this.sortOrder = sortOrder;
	}

	public boolean isRequired() {
		return required;
	}

	public void setRequired( boolean required ) {
		this.required = required;
	}

	public boolean isSearchable() {
		return searchable;
	}

	public void setSearchable( boolean searchable ) {
		this.searchable = searchable;
	}

	public String getValidation() {
		return validation;
	}

	public void setValidation( String validation ) {
		this.validation = validation;
	}

	public String getFieldType() {
		return fieldType;
	}

	public void setFieldType( String fieldType ) {
		this.fieldType = fieldType;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType( String dataType ) {
		this.dataType = dataType;
	}

	public String getOptions() {
		return options;
	}

	public void setOptions( String options ) {
		this.options = options;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled( boolean enabled ) {
		this.enabled = enabled;
	}

	public boolean isHidden() {
		return hidden;
	}

	public void setHidden( boolean hidden ) {
		this.hidden = hidden;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue( String defaultValue ) {
		this.defaultValue = defaultValue;
	}
}
