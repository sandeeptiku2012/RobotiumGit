package no.sumo.api.vo.metadata;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Default( DefaultType.FIELD )
@Root( name = "metadataEntry" )
public class RestMetadataEntry {
	@Attribute( name = "xml:lang" )
	private String lang;

	@Text
	private String value;

	public RestMetadataEntry() {
	}

	public RestMetadataEntry( String lang, String value ) {
		setLang( lang );
		setValue( value );
	}

	public String getLang() {
		return lang;
	}

	public void setLang( String lang ) {
		this.lang = lang;
	}

	public String getValue() {
		return value;
	}

	public void setValue( String value ) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (lang == null ? 0 : lang.hashCode());
		result = prime * result + (value == null ? 0 : value.hashCode());
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
		RestMetadataEntry other = (RestMetadataEntry)obj;
		if( lang == null ) {
			if( other.lang != null ) {
				return false;
			}
		} else if( !lang.equals( other.lang ) ) {
			return false;
		}
		if( value == null ) {
			if( other.value != null ) {
				return false;
			}
		} else if( !value.equals( other.value ) ) {
			return false;
		}
		return true;
	}
}