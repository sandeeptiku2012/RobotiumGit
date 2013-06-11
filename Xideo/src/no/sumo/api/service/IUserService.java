package no.sumo.api.service;

import java.security.GeneralSecurityException;
import java.util.Collection;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import no.sumo.api.exception.base.GeneralFailureException;
import no.sumo.api.exception.base.MultipleValidationExceptions;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.exception.base.ValidationException;
import no.sumo.api.exception.generic.DataValidationException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.exception.user.UserNotFoundException;
import no.sumo.api.service.annotations.Wrapped;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.RestSubscriptionUpgrade;
import no.sumo.api.vo.order.RestOrder;
import no.sumo.api.vo.order.RestOrderList;
import no.sumo.api.vo.order.RestTransaction;
import no.sumo.api.vo.paymentprovider.RestPaymentProvider;
import no.sumo.api.vo.platform.RestPlatform;
import no.sumo.api.vo.profile.RestPassword;
import no.sumo.api.vo.profile.RestPinCode;
import no.sumo.api.vo.profile.RestUser;
import no.sumo.api.vo.profile.RestUserProperty;

@Consumes( MediaType.WILDCARD )
@Path( "/{platform}/user" )
public interface IUserService {

	@GET
	@Formatted
	@Wrapped( element = "users" )
	@Path( "/property/{propertyName}" )
	public List<RestUser> getUsersWithPropertyNameAndValue( @PathParam( "platform" ) RestPlatform platform, @PathParam( "propertyName" ) String propertyName, @QueryParam( "value" ) String propertyValue );

	@GET
	@Formatted
	@Wrapped( element = "properties" )
	@Path( "/{userId}/properties" )
	public List<RestUserProperty> getUserProperties( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userId" ) Long userId );

	@GET
	@Formatted
	@Path( "/{userId}/property/{propertyId}" )
	public RestUserProperty getUserProperty( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userId" ) Long userId, @PathParam( "propertyId" ) Long propertyId );

	@PUT
	@Formatted
	@Path( "/{userId}/property/{propertyId}" )
	public RestUserProperty putUserProperty( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userId" ) Long userId, @PathParam( "propertyId" ) Long propertyId, RestUserProperty property );

	@DELETE
	@Formatted
	@Path( "/{userId}/property/{propertyId}" )
	public void deleteUserProperty( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userId" ) Long userId, @PathParam( "propertyId" ) Long propertyId );

	@POST
	@Formatted
	@Path( "/{userId}/property" )
	public RestUserProperty postUserProperty( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userId" ) Long userId, RestUserProperty property );

	@GET
	@Formatted
	@Path( "/{emailname}@{emaildomain}" )
	public RestUser getUser( @PathParam( "platform" ) RestPlatform platform, @PathParam( "emailname" ) String emailname, @PathParam( "emaildomain" ) String emaildomain ) throws MethodNotAllowedException, UserNotFoundException;

	@GET
	@Formatted
	@Path( "/{userId}" )
	public RestUser getUser( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userId" ) Long userId ) throws MethodNotAllowedException, UserNotFoundException;

	@GET
	@Formatted
	@Path( "" )
	public RestUser getUser( @PathParam( "platform" ) RestPlatform platform ) throws MethodNotAllowedException;

	@GET
	@Formatted
	@Wrapped( element = "users" )
	@Path( "/connected" )
	public List<RestUser> getConnectedUsers( @PathParam( "platform" ) RestPlatform platform ) throws UserNotFoundException, MethodNotAllowedException;

	@PUT
	@Formatted
	@Path( "" )
	public RestUser updateUser( @PathParam( "platform" ) RestPlatform platform, RestUser user ) throws MethodNotAllowedException, NotFoundException;

	@POST
	@Formatted
	@Path( "" )
	public RestUser createUser( @PathParam( "platform" ) RestPlatform platform, RestUser user ) throws MethodNotAllowedException, DataValidationException, GeneralFailureException, ValidationException, MultipleValidationExceptions, UserNotFoundException;

	@PUT
	@Path( "/password" )
	public void updatePassword( @PathParam( "platform" ) RestPlatform platform, RestPassword password ) throws MethodNotAllowedException, GeneralSecurityException, DataValidationException;

	@DELETE
	@Path( "/{emailAddress}/password" )
	public void resetPassword( @PathParam( "platform" ) RestPlatform platform, @PathParam( "emailAddress" ) String emailAddress ) throws UserNotFoundException;

	@GET
	@Formatted
	@Path( "/{userId}/orders/{orderId}" )
	public RestOrder getOrder( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userId" ) Long userId, @PathParam( "orderId" ) Long orderId ) throws MethodNotAllowedException, NotFoundException;

	@GET
	@Formatted
	@Path( "/{userId}/orders/{orderId}/transactions/{transactionId}" )
	public RestTransaction getTransaction( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userId" ) Long userId, @PathParam( "orderId" ) Long orderId, @PathParam( "transactionId" ) Long transactionId ) throws MethodNotAllowedException;

	@GET
	@Formatted
	@Wrapped( element = "transactions" )
	@Path( "/{userId}/orders/{orderId}/transactions" )
	public List<RestTransaction> getTransactions( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userId" ) Long userId, @PathParam( "orderId" ) Long orderId ) throws MethodNotAllowedException;

	@GET
	@Formatted
	@Wrapped( element = "orders" )
	@Path( "/{userId}/orders/current" )
	public RestOrderList getOrders( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userId" ) Long userId, @QueryParam("categoryId") Long categoryId) throws MethodNotAllowedException;


	
	@GET
	@Formatted
	@Wrapped( element = "orders" )
	@Path( "/{userId}/orders/history" )
	public List<RestOrder> getOrderHistory( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userId" ) Long userId ) throws MethodNotAllowedException, UserNotFoundException;

	@GET
	@Formatted
	@Wrapped( element = "possibleOneClickBuys" )
	@Path( "/{userId}/possibleOneClickBuys" )
	public Collection<RestPaymentProvider> getOneClickBuyPaymentProviders( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userId" ) Long userId ) throws MethodNotAllowedException, UserNotFoundException;

	@GET
	@Formatted
	@Wrapped( element = "subscriptionUpgrades" )
	@Path( "/{userId}/possibleSubscriptionUpgrades" )
	public Collection<RestSubscriptionUpgrade> getSubscriptionUpgrades( @PathParam( "platform" ) RestPlatform platform, @PathParam( "userId" ) Long userId ) throws MethodNotAllowedException, UserNotFoundException;

	@POST
	@Path( "/pinCode" )
	public void setPinCode( @PathParam( "platform" ) RestPlatform platform, RestPinCode pinCode ) throws MethodNotAllowedException, ValidationException;
}
