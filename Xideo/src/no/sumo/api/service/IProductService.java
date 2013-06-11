package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import no.sumo.api.exception.asset.AssetNotFoundException;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.exception.base.ValidationException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.exception.payment.PaymentException;
import no.sumo.api.exception.product.ProductGroupNotFoundException;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.asset.RestAsset;
import no.sumo.api.vo.asset.RestAssetList;
import no.sumo.api.vo.category.RestCategoryList;
import no.sumo.api.vo.platform.RestPlatform;
import no.sumo.api.vo.product.RestPaymentObject;
import no.sumo.api.vo.product.RestProduct;
import no.sumo.api.vo.product.RestProductGroup;
import no.sumo.api.vo.product.RestProductGroupAccess;
import no.sumo.api.vo.product.RestProductGroupAccessList;
import no.sumo.api.vo.product.RestProductGroupList;
import no.sumo.api.vo.product.RestProductList;
import no.sumo.api.vo.product.RestProductPayment;
import no.sumo.api.vo.product.RestProductPaymentList;

@Consumes( MediaType.WILDCARD )
@Path( "/{platform}/productgroup" )
public interface IProductService {

	@GET
	@Formatted
	@Path( "" )
	public RestProductGroupList getProductGroups( @PathParam( "platform" ) RestPlatform platform );

	/**
	 * Gets a single {@link no.sumo.api.vo.RestProduct}
	 * 
	 * @throws NotFoundException
	 *             when the productGroupId does not exist.
	 */
	@GET
	@Formatted
	@Path( "/{productGroupId}" )
	public RestProductGroup getProductGroup( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId ) throws NotFoundException;

	@GET
	@Formatted
	@Path( "/{productGroupId}/products" )
	public RestProductList getProducts( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId );

	@GET
	@Formatted
	@Path( "{productGroupId}/products/{productId}" )
	public RestProduct getProduct( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId, @PathParam( "productId" ) Long productId ) throws NotFoundException;

	@GET
	@Formatted
	@Path( "{productGroupId}/products/{productId}/productPayments" )
	public RestProductPaymentList getProductPayments( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId, @PathParam( "productId" ) Long productId, @QueryParam( "voucherCode" ) String voucherCode );

	@GET
	@Formatted
	@Path( "{productGroupId}/products/{productId}/productPayments/{productPaymentId}" )
	public RestProductPayment getProductPayment( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId, @PathParam( "productId" ) Long productId, @PathParam( "productPaymentId" ) Long productPaymentId ) throws NotFoundException;

	/**
	 * For now (June 2012), let's assume that the API user will know which
	 * provider_id to post to product_provider (i.e. we will not make an API for
	 * payment_provider to list all available payment methods).
	 * 
	 * discountedPrice is a calculated field and will be ignored in POST
	 * anything set for "enabled" will be ignored. Use productPaymentStatus
	 * instead. if not set, initPrice and initPeriod will default to
	 * product.price and product.period respectively.
	 * 
	 * Required: - totalPrice - productId - productPaymentStatus -
	 * paymentProviderId - productPaymentStatus (one of HIDDEN, DISABLED,
	 * ENABLED)
	 * 
	 * @throws ValidationException
	 */

	@POST
	@Formatted
	@Path( "{productGroupId}/products/{productId}/productPayments" )
	public RestProductPayment postProductPayment( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId, @PathParam( "productId" ) Long productId, RestProductPayment restProductPayment ) throws ValidationException;

	@PUT
	@Formatted
	@Path( "{productGroupId}/products/{productId}/productPayments/{productPaymentId}" )
	public RestProductPayment putProductPayment( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId, @PathParam( "productId" ) Long productId, @PathParam( "productPaymentId" ) Long productPaymentId, RestProductPayment restProductPayment ) throws ValidationException, NotFoundException;

	@DELETE
	@Formatted
	@Path( "{productGroupId}/products/{productId}/productPayments/{productPaymentId}" )
	public void deleteProductPayment( @PathParam( "productPaymentId" ) Long productPaymentId ) throws NotFoundException, MethodNotAllowedException;

	@GET
	@Formatted
	@Path( "{productGroupId}/products/{productId}/productPayments/{productPaymentId}/payment" )
	public RestPaymentObject getPaymentObject( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId, @PathParam( "productId" ) Long productId, @PathParam( "productPaymentId" ) Long productPaymentId ) throws NotFoundException, PaymentException;

	/**
	 * Gets a collection of {@link no.sumo.api.vo.category.Category} that the
	 * product gives access to
	 */
	@GET
	@Formatted
	@Path( "/{productGroupId}/categories" )
	public RestCategoryList getProductCategories( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId, @QueryParam( "sort" ) @DefaultValue( "priority+desc" ) String sort );

	/**
	 * Gets a collection of {@link RestAsset} that the product gives access to
	 * 
	 * @throws AssetNotFoundException
	 */
	@GET
	@Formatted
	@Path( "/{productGroupId}/assets" )
	public RestAssetList getProductAssets( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productId ) throws AssetNotFoundException;

	/**
	 * Creates a new {@link no.sumo.api.entity.sumo.ProductGroup}. A
	 * ProductGroup will always be set with a default access type
	 * ProductGroupAccessType.PAID
	 * 
	 * @param restProductGroup
	 *            the new ProductGroup to create
	 * @return the representation of the new product group as created.
	 */
	@POST
	@Formatted
	@Path( "" )
	public RestProductGroup postProductGroup( @PathParam( "platform" ) RestPlatform platform, RestProductGroup restProductGroup );

	@DELETE
	@Formatted
	@Path( "/{productGroupId}" )
	public void deleteProductGroup( @PathParam( "productGroupId" ) Long productGroupId );

	/**
	 * Updates an existing {@link no.sumo.api.entity.sumo.ProductGroup}. The
	 * productGroupId in the request body must be identical to the
	 * productGroupId in the path.
	 * 
	 * @param platform
	 *            , the platform
	 * @param productGroupId
	 *            , the id of the product group to update.
	 * @param restProductGroup
	 *            , the changes to update the product group with.
	 * @return the updated representation of the product group.
	 * @throws ProductGroupNotFoundException
	 * @throws ValidationException
	 */
	@PUT
	@Formatted
	@Path( "/{productGroupId}" )
	public RestProductGroup putProductGroup( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId, RestProductGroup restProductGroup ) throws ProductGroupNotFoundException, ValidationException;

	@POST
	@Formatted
	@Path( "{productGroupId}/products" )
	public RestProduct postProduct( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId, RestProduct restProduct ) throws ValidationException;

	@PUT
	@Formatted
	@Path( "{productGroupId}/products/{productId}" )
	public RestProduct putProduct( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId, @PathParam( "productId" ) Long productId, RestProduct restProduct ) throws ValidationException;

	@DELETE
	@Formatted
	@Path( "{productGroupId}/products/{productId}" )
	public void deleteProduct( @PathParam( "productId" ) Long productId );

	@GET
	@Formatted
	@Path( "{productGroupId}/accesses" )
	public RestProductGroupAccessList getProductGroupAccesses( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId );

	/**
	 * Creates a new product group access object. The productGroupId in the
	 * request body must be identical to the productGroupId in the path.
	 * 
	 * Required in the request body: - treeId - platformId - productGroupId -
	 * productGroupAccessType - enabled
	 * 
	 * @param platform
	 *            , one of types specified in
	 *            {@link no.sumo.api.entity.vman.enums.PlatformId}
	 * @param productGroupId
	 *            , the product group id to connect the product group access
	 *            object to
	 * @param restProductGroupAccess
	 *            , the request body
	 * @return
	 * @throws ValidationException
	 */
	@POST
	@Formatted
	@Path( "{productGroupId}/accesses" )
	public RestProductGroupAccess postProductGroupAccess( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId, RestProductGroupAccess restProductGroupAccess ) throws ValidationException;

	@GET
	@Formatted
	@Path( "{productGroupId}/accesses/{accessId}" )
	public RestProductGroupAccess getProductGroupAccess( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId, @PathParam( "accessId" ) Long accessId );

	/**
	 * See documentation for POST.
	 */
	@PUT
	@Formatted
	@Path( "{productGroupId}/accesses/{accessId}" )
	public RestProductGroupAccess putProductGroupAccess( @PathParam( "platform" ) RestPlatform platform, @PathParam( "productGroupId" ) Long productGroupId, @PathParam( "accessId" ) Long accessId, RestProductGroupAccess restProductGroupAccess ) throws ValidationException;

	@DELETE
	@Formatted
	@Path( "{productGroupId}/accesses/{accessId}" )
	public void deleteProductGroupAccess( @PathParam( "accessId" ) Long accessId );
}
