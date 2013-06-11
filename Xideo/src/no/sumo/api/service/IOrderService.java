package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.exception.base.ValidationException;
import no.sumo.api.exception.generic.DataIncompleteException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.exception.payment.PaymentException;
import no.sumo.api.exception.payment.PaymentInputException;
import no.sumo.api.exception.user.UserNotFoundException;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.order.RestOrder;
import no.sumo.api.vo.platform.RestPlatform;

@Consumes( MediaType.WILDCARD )
@Path( "/{platform}/order" )
public interface IOrderService {

	/* Can't change PUT to POST without synchronizing with API users. At least support both in a period. */
	@PUT
	@Formatted
	@Path( "/{referenceId}/terminateAll/{paymentProviderId}" )
	public void terminateByProvider( @PathParam( "platform" ) RestPlatform platform, @PathParam( "referenceId" ) String referenceId, @PathParam( "paymentProviderId" ) Long paymentProviderId );

	@PUT
	@Formatted
	@Path( "/{orderId}/terminate" )
	public RestOrder terminateOrder( @PathParam( "platform" ) RestPlatform platform, @PathParam( "orderId" ) Long orderId ) throws MethodNotAllowedException, NotFoundException;

	@PUT
	@Formatted
	@Path( "/{orderId}" )
	public RestOrder updateOrder( @PathParam( "platform" ) RestPlatform platform, @PathParam( "orderId" ) Long orderId, RestOrder restOrder ) throws MethodNotAllowedException, NotFoundException;

	@GET
	@Formatted
	@Path( "/{orderId}/callback" )
	public RestOrder completeExternalOrder( @PathParam( "platform" ) RestPlatform platform, @PathParam( "orderId" ) Long orderId, @Context UriInfo uri ) throws PaymentInputException, PaymentException, NotFoundException, MethodNotAllowedException;

	@GET
	@Formatted
	@Path( "/callback" )
	public RestOrder completeExternal( @PathParam( "platform" ) RestPlatform platform, @QueryParam( "orderRef" ) String orderRef, @Context UriInfo uri ) throws PaymentInputException, PaymentException, NotFoundException, MethodNotAllowedException;

	@PUT
	@Formatted
	@Path( "" )
	public RestOrder completeOrder( @PathParam( "platform" ) RestPlatform platform, RestOrder restOrder ) throws PaymentException, NotFoundException, MethodNotAllowedException, ValidationException;

	@POST
	@Formatted
	@Path( "" )
	public RestOrder initializeOrder( @PathParam( "platform" ) RestPlatform platform, RestOrder restOrder ) throws NotFoundException, PaymentException, DataIncompleteException, MethodNotAllowedException;

	/**
	 * Creates a new order without the use of any payment methods
	 */
	@POST
	@Formatted
	@Path( "/{userId}/create" )
	public RestOrder create( @PathParam( "userId" ) Long userId, @PathParam( "platform" ) RestPlatform platform, RestOrder restOrder ) throws UserNotFoundException;

}
