package com.vimond.imageservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import android.graphics.Bitmap;

@Path( "image" )
@Consumes( { BitmapReader.IMAGE_PNG, BitmapReader.IMAGE_JPEG } )
public interface IVimondImageService {
	@GET
	@Path( "/{image-pack}/{type}" )
	public Bitmap getBitmap( @PathParam( "image-pack" ) String imagePackId, @PathParam( "type" ) String imageLocation );

	@GET
	@Path( "/{image-pack}/{type}/customSize/{width}x{height}" )
	public Bitmap getBitmap( @PathParam( "image-pack" ) String imagePackId, @PathParam( "type" ) String imageLocation, @PathParam( "width" ) int width, @PathParam( "height" ) int height );

	@GET
	@Path( "/assertimage/{asset-id}/thumb-stb" )
	public Bitmap getAssetThumb( @PathParam( "asset-id" ) long assetId );

	@GET
	@Path( "/assertimage/{asset-id}/thumb-stb/customSize/{width}x{height}" )
	public Bitmap getAssetThumb( @PathParam( "asset-id" ) long assetId, @PathParam( "width" ) int width, @PathParam( "height" ) int height );
}
