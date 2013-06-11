package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.message.RestMessageBundle;

@Consumes( MediaType.WILDCARD )
@Path( "/message/" )
public interface IMessageService {

	@GET
	@Formatted
	@Path( "/key/{messageKey}/{langCode}" )
	@Produces( MediaType.TEXT_PLAIN )
	public String getMessage( @PathParam( "messageKey" ) String messageKey, @PathParam( "langCode" ) String langCode );

	@GET
	@Formatted
	@Path( "/messagebundle" )
	public RestMessageBundle getMessageBundle( @HeaderParam( value = HttpHeaders.ACCEPT_LANGUAGE ) String locale );

}
