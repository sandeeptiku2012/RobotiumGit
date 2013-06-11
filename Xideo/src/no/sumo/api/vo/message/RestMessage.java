package no.sumo.api.vo.message;

import no.sumo.api.contracts.IMessage;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Text;

@Root( name = "message" )
public class RestMessage implements IMessage {

	private Long id;

	@Attribute( name = "key" )
	private String key;

	private String locale;

	@Text
	private String message;

	private String messageType;

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public String getKey() {
		return key;
	}

	public void setKey( String key ) {
		this.key = key;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale( String locale ) {
		this.locale = locale;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage( String message ) {
		this.message = message;
	}

	public String getMessageType() {
		return messageType;
	}

	public void setMessageType( String messageType ) {
		this.messageType = messageType;
	}

}
