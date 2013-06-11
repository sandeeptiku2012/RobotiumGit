package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.exception.base.ValidationException;
import no.sumo.api.service.annotations.Beta;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.StringList;

@Consumes( MediaType.WILDCARD )
@Path( "/" )
public interface IBaseService {

	/**
	 * Method for "pinging" the API. This method will always return "pong!"
	 */
	@GET
	@Path( "/ping" )
	@Consumes( MediaType.WILDCARD )
	@Produces( MediaType.TEXT_PLAIN )
	String ping();

	@PUT
	@Path( "/ping" )
	@Consumes( MediaType.WILDCARD )
	@Produces( MediaType.TEXT_PLAIN )
	String pingPUT();

	@POST
	@Path( "/ping" )
	@Consumes( MediaType.WILDCARD )
	@Produces( MediaType.TEXT_PLAIN )
	String pingPOST();

	@DELETE
	@Path( "/ping" )
	@Consumes( MediaType.WILDCARD )
	@Produces( MediaType.TEXT_PLAIN )
	String pingDELETE();

	@GET
	@Path( "/version" )
	@Produces( MediaType.TEXT_PLAIN )
	@Beta
	String getVersion();

	@GET
	@Path( "/build/date" )
	@Produces( MediaType.TEXT_PLAIN )
	@Beta
	String getBuildTimestamp();

	@GET
	@Formatted
	@Path( "/errors" )
	@Beta
	StringList getErrors();

	@GET
	@Formatted
	@Path("/platforms")
	@Beta StringList getPlatforms() throws NotFoundException, ValidationException;

    @GET
    @Path("system/date")
    @Produces(MediaType.TEXT_PLAIN)
    String getServerDateTime();
}
