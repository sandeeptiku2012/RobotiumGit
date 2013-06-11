package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import no.sumo.api.exception.asset.AssetNotFoundException;
import no.sumo.api.exception.category.CategoryNotFoundException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.exception.metadata.MetadataDefinitionNotFoundException;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.metadata.Languages;
import no.sumo.api.vo.metadata.RestMetadata;
import no.sumo.api.vo.metadata.definitions.RestMetadataDefinition;
import no.sumo.api.vo.metadata.definitions.RestMetadataDefinitionList;
import no.sumo.api.vo.metadata.lookup.RestMetadataWithIdList;

@Consumes( MediaType.WILDCARD )
@Path( "/metadata/" )
public interface IMetadataService {

	@GET
	@Formatted
	@Path( "/asset/{id}" )
	RestMetadata getAssetMetadata( @HeaderParam( value = HttpHeaders.ACCEPT_LANGUAGE ) Languages languages, @PathParam( "id" ) Long assetId );

	@POST
	@Formatted
	@Path( "/asset/{id}" )
	RestMetadata postAssetMetadata( @PathParam( "id" ) Long assetId, RestMetadata meta );

	@DELETE
	@Formatted
	@Path( "/asset/{id}" )
	void deleteAssetMetadata( @PathParam( "id" ) Long assetId ) throws AssetNotFoundException, MethodNotAllowedException;

	@DELETE
	@Formatted
	@Path( "/asset/{id}/{name}" )
	void deleteAssetMetadata( @PathParam( "id" ) Long assetId, @PathParam( "name" ) String name ) throws AssetNotFoundException, MethodNotAllowedException;

	@DELETE
	@Formatted
	@Path( "/asset/{id}/{name}/{language}" )
	void deleteAssetMetadata( @PathParam( "id" ) Long assetId, @PathParam( "name" ) String name, @PathParam( "language" ) Languages languages ) throws AssetNotFoundException, MethodNotAllowedException;

	@GET
	@Formatted
	@Path( "/category/{id}" )
	RestMetadata getCategoryMetadata( @HeaderParam( value = HttpHeaders.ACCEPT_LANGUAGE ) Languages languages, @PathParam( "id" ) Long categoryId );

	@POST
	@Formatted
	@Path( "/category/{id}" )
	RestMetadata postCategoryMetadata( @PathParam( "id" ) Long categoryId, RestMetadata meta );

	@DELETE
	@Formatted
	@Path( "/category/{id}" )
	void deleteCategoryMetadata( @PathParam( "id" ) Long categoryId ) throws CategoryNotFoundException, MethodNotAllowedException;

	@DELETE
	@Formatted
	@Path( "/category/{id}/{name}" )
	void deleteCategoryMetadata( @PathParam( "id" ) Long categoryId, @PathParam( "name" ) String name ) throws CategoryNotFoundException, MethodNotAllowedException;

	@DELETE
	@Formatted
	@Path( "/category/{id}/{name}/{language}" )
	void deleteCategoryMetadata( @PathParam( "id" ) Long categoryId, @PathParam( "name" ) String name, @PathParam( "language" ) Languages languages ) throws CategoryNotFoundException, MethodNotAllowedException;

	@GET
	@Formatted
	@Path( "/definitions" )
	RestMetadataDefinitionList getMetadataDefinitions();

	@GET
	@Formatted
	@Path( "/definition/{name}" )
	RestMetadataDefinition getMetadataDefinition( @PathParam( "name" ) String name ) throws MetadataDefinitionNotFoundException;

	/**
	 * Updates a metadata definition - if it does not exist, it is created
	 *
	 * @param name
	 *            name of the metadata definition to update
	 * @param metadataDefinition
	 *            the new metadata definition state
	 * @return the new metadata definition state
	 */
	@PUT
	@Formatted
	@Path( "/definition/{name}" )
	RestMetadataDefinition putMetadataDefinition( @PathParam( "name" ) String name, RestMetadataDefinition metadataDefinition );

	/**
	 * Use this to find asset-ids with a specific metadata present. If value is
	 * not specified, all assets with this metadata present are returned. IF
	 * value is specified, only those assets with that metadata == value is
	 * returned.
	 *
	 * @param metadataName
	 *            name of the asset-metadata-name
	 * @param value
	 *            optional : metadata value
	 * @param language
	 *            optional: if not specified => language = *
	 * @return
	 */
	@GET
	@Formatted
	@Path( "/lookup/assets/{metadataName}" )
	RestMetadataWithIdList getLookupAsset( @PathParam( "metadataName" ) String metadataName, @QueryParam( "value" ) String value, @QueryParam( "language" ) String language );

	/**
	 * Use this to find category-ids with a specific metadata present. If value
	 * is not specified, all categories with this metadata present are returned.
	 * IF value is specified, only those categories with that metadata == value
	 * is returned.
	 *
	 * @param metadataName
	 *            name of the category-metadata-name
	 * @param value
	 *            optional : metadata value
	 * @param language
	 *            optional: if not specified => language = *
	 * @return
	 */
	@GET
	@Formatted
	@Path( "/lookup/categories/{metadataName}" )
	RestMetadataWithIdList getLookupCategory( @PathParam( "metadataName" ) String metadataName, @QueryParam( "value" ) String value, @QueryParam( "language" ) String language );

}
