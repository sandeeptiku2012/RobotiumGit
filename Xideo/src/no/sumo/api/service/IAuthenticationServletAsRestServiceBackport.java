package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.profile.RestLoginResult;

/**
 * This service is a replacement for the deleted AuthenticationServlet. It
 * exists only so that old authentication-url's will continue to work.
 */
@Consumes( MediaType.WILDCARD )
@Path( "/authentication" )
public interface IAuthenticationServletAsRestServiceBackport {

	@GET
	@Path( "" )
	@Formatted
	RestLoginResult login( @QueryParam( "action" ) String action, @QueryParam( "username" ) String username, @QueryParam( "password" ) String password, @QueryParam( "rememberMe" ) @DefaultValue( "false" ) boolean rememberMe );

	/**
	 * REST-52 TV 4's developers managed to do a non standard post. This request
	 * was unfortunately supported, because the previous implementation was a
	 * servlet.
	 */
	@POST
	@Path( "" )
	@Formatted
	RestLoginResult loginUsingPost( @QueryParam( "action" ) @DefaultValue( "" ) String actionAsQueryParam, @FormParam( "action" ) @DefaultValue( "" ) String actionAsFormParam, @FormParam( "username" ) String username, @FormParam( "password" ) String password, @FormParam( "rememberMe" ) @DefaultValue( "false" ) boolean rememberMe );
}
