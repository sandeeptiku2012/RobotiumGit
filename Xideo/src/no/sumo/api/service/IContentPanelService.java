package no.sumo.api.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.exception.base.ValidationException;
import no.sumo.api.service.annotations.Wrapped;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.contentpanel.RestContentPanel;
import no.sumo.api.vo.contentpanel.RestContentPanelElement;
import no.sumo.api.vo.platform.RestPlatform;

/**
 * @author Tor Erik (tea@vimond.com)
 */
@Consumes( MediaType.WILDCARD )
@Path( "" )
public interface IContentPanelService {

	@GET
	@Formatted
	@Wrapped( element = "contentPanel" )
	@Path( "/contentpanel/{type}/{name}" )
	public RestContentPanel getContentPanel( @PathParam( "type" ) String type, @PathParam( "name" ) String name );

	@GET
	@Formatted
	@Wrapped( element = "contentPanel" )
	@Path( "/contentpanel/{name}" )
	public RestContentPanel getContentPanelByName( @PathParam( "name" ) String name );

	/*
	 * In order to support expand parameter we need a platform, as the expanded elements are platform dependent.
	 */
	@GET
	@Formatted
	@Wrapped( element = "contentPanel" )
	@Path( "/{platform}/contentpanel/{name}" )
	public RestContentPanel getExpandableContentPanelByName( @PathParam( "platform" ) RestPlatform platform, @PathParam( "name" ) String name, @QueryParam( "expand" ) @DefaultValue( "" ) String expand, @Context Response response );

	@GET
	@Formatted
	@Wrapped( element = "contentPanelElements" )
	@Path( "/contentpanel" )
	public List<RestContentPanelElement> getAll();

	// Operations on single elements:

	@GET
	@Formatted
	@Path( "/contentpanel/element/{id}" )
	public RestContentPanelElement getPanelElement( @PathParam( "id" ) Long panelElementId ) throws NotFoundException;

	@POST
	@Formatted
	@Path( "/contentpanel/element/" )
	public RestContentPanelElement addPanelElement( RestContentPanelElement contentPanel );

	@PUT
	@Formatted
	@Path( "/contentpanel/element/{id}" )
	public RestContentPanelElement updatePanelElement( @PathParam( "id" ) Long panelElementId, RestContentPanelElement contentPanel ) throws NotFoundException, ValidationException;

	@DELETE
	@Formatted
	@Path( "/contentpanel/element/{id}" )
	public void deletePanelElement( @PathParam( "id" ) Long panelElementId ) throws NotFoundException;

}
