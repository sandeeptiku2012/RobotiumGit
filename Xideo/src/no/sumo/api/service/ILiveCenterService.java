package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.service.vo.WindowDefinitionManger;

@Consumes( MediaType.WILDCARD )
@Path( "/{platform}/livecenter" )
public interface ILiveCenterService {

	@GET
	@Formatted
	@Path( "/{appId}/definitions" )
	WindowDefinitionManger getWindowDefinitionManager( @PathParam( "appId" ) Long applicationId, @QueryParam( "screenWidth" ) Integer screenWidth, @QueryParam( "screenHeight" ) Integer screenHeight );
}
