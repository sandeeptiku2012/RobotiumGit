package no.sumo.api.service;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;

import no.sumo.api.service.annotations.Wrapped;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.service.vo.MultipartFormDataInput;
import no.sumo.api.vo.platform.RestPlatform;
import no.sumo.api.vo.subtitle.RestAssetSubtitle;

@Consumes( MediaType.WILDCARD )
@Path( "/" )
public interface ISubtitleService {

	@GET
	@Formatted
	@Path( "/{platform}/asset/{assetId}/subtitles" )
	@Wrapped( element = "subtitles" )
	public List<RestAssetSubtitle> getAvailableSubtitles( @PathParam( "platform" ) RestPlatform platform, @PathParam( "assetId" ) Long assetId );

	@GET
	@Formatted
	@Path( "/{platform}/asset/{assetId}/subtitle/{id}" )
	public byte[] getSubtitle( @PathParam( "assetId" ) Long assetId, @PathParam( "id" ) Long subtitleId ) throws UnsupportedEncodingException;

	@PUT
	@Path( "/asset/{assetId}/subtitle" )
	@Consumes( "multipart/form-data" )
	@Produces( MediaType.TEXT_PLAIN )
	public void putSubtitle( MultipartFormDataInput input, @PathParam( "assetId" ) Long assetId, @HeaderParam( value = HttpHeaders.ACCEPT_LANGUAGE ) String locale ) throws IOException;

	@POST
	@Path( "/asset/{assetId}/subtitle" )
	@Consumes( "multipart/form-data" )
	@Produces( MediaType.TEXT_PLAIN )
	public void postSubtitle( MultipartFormDataInput input, @PathParam( "assetId" ) Long assetId, @HeaderParam( value = HttpHeaders.ACCEPT_LANGUAGE ) String locale ) throws IOException;

}
