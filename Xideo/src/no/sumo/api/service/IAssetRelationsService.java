package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import no.sumo.api.exception.asset.relations.AssetRelationNotFoundException;
import no.sumo.api.exception.asset.relations.AssetRelationTypeNotFoundException;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.asset.relations.RestAssetRelation;
import no.sumo.api.vo.asset.relations.RestAssetRelationList;
import no.sumo.api.vo.asset.relations.RestAssetRelationType;
import no.sumo.api.vo.asset.relations.RestAssetRelationTypeList;

@Consumes( { MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON, MediaType.WILDCARD } )
@Path( "/assetRelations" )
public interface IAssetRelationsService {

	/**
	 * Gets all assetRelationTypes
	 */
	@GET
	@Formatted
	@Path( "/types" )
	public RestAssetRelationTypeList getAllAssetRelationTypes();

	/**
	 * Gets one assetRelationType by name
	 */
	@GET
	@Formatted
	@Path( "/type/{relationType}" )
	public RestAssetRelationType getAssetRelationType( @PathParam( "relationType" ) String relationType ) throws AssetRelationTypeNotFoundException;

	/**
	 * Stores one assetRelationType by name.
	 */
	@PUT
	@Formatted
	@Path( "/type/{relationType}" )
	public void putAssetRelationType( @PathParam( "relationType" ) String relationType );

	/**
	 * Deletes one assetRelationType by name
	 */
	@DELETE
	@Path( "/type/{relationType}" )
	public void deleteAssetRelationType( @PathParam( "relationType" ) String relationType ) throws AssetRelationTypeNotFoundException;

	public enum AssetRelationDirection {
		IN,
		OUT,
		BOTH
	}

	/**
	 * Gets all relations to and/or from an asset of specific type
	 */
	@GET
	@Formatted
	@Path( "/type/{relationType}/{assetId}/" )
	public RestAssetRelationList getAssetRelations( @PathParam( "relationType" ) String relationType, @PathParam( "assetId" ) Long assetId, @QueryParam( "direction" ) AssetRelationDirection direction );

	/**
	 * Gets one single assetRelation instance - by id
	 */
	@GET
	@Formatted
	@Path( "/relation/{id}" )
	public RestAssetRelation getAssetRelation( @PathParam( "id" ) Long id ) throws AssetRelationNotFoundException;

	/**
	 * Updates one single assetRelation instance - by id
	 */
	@PUT
	@Formatted
	@Path( "/relation/{id}" )
	public RestAssetRelation putAssetRelation( @PathParam( "id" ) Long id, RestAssetRelation relation ) throws AssetRelationNotFoundException, AssetRelationTypeNotFoundException;

	/**
	 * Deletes one single assetRelation instance - by id
	 */
	@DELETE
	@Formatted
	@Path( "/relation/{id}" )
	public void deleteAssetRelation( @PathParam( "id" ) Long id ) throws AssetRelationNotFoundException;

	/**
	 * Creates a new relation between assets
	 */
	@POST
	@Formatted
	@Path( "/relations" )
	public RestAssetRelation postAssetRelation( RestAssetRelation relation ) throws AssetRelationTypeNotFoundException;

}
