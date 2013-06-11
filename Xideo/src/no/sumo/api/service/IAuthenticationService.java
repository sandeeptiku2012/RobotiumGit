package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.profile.RestLoginResult;

@Consumes( MediaType.WILDCARD )
@Path( "/authentication/user" )
public interface IAuthenticationService {

	/**
	 * Checks if the current session is authenticated
	 */
	@GET
	@Formatted
	@Path( "" )
	public RestLoginResult isSessionAuthenticated();

	@POST
	@Formatted
	@Path( "/login" )
	public RestLoginResult login( @FormParam( "username" ) String username, @FormParam( "password" ) String password, @FormParam( "rememberMe" ) @DefaultValue( "false" ) boolean rememberMe ) throws Exception;

	@DELETE
	@Formatted
	@Path( "/logout" )
	public RestLoginResult logout();
}
