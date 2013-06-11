package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import no.sumo.api.vo.platform.RestPlatform;

@Consumes( MediaType.WILDCARD )
@Path( "/{platform}/user" )
public interface IFindUserService {

	@GET
	@Path( "/find/email/{email}" )
	public Response findByEmail( @PathParam( "platform" ) RestPlatform platform, @PathParam( "email" ) String email );

	@GET
	@Path( "/find/userName/{userName}" )
	public Response findByUsername( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userName" ) String username );

	@GET
	@Path( "/find/mobileNumber/{mobileNumber}" )
	public Response findByMobileNumber( @PathParam( "platform" ) RestPlatform platform, @PathParam( "mobileNumber" ) String mobileNumber );
}