package no.sumo.api.vo.message;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Default;
import org.simpleframework.xml.DefaultType;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "MessageBundle" )
@Default( DefaultType.FIELD )
public class RestMessageBundle extends RestObject {

	@Element( name = "message" )
	private List<RestMessage> messages = new ArrayList<RestMessage>();

	@Attribute( name = "locale" )
	private String locale;

	public RestMessageBundle() {
	}

	public RestMessageBundle( List<RestMessage> messages, String locale ) {
		this.messages = messages;
		this.locale = locale;
	}

}
