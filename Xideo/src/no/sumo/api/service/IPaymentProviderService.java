package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.platform.RestPlatform;
import no.sumo.api.vo.product.RestProductList;

@Consumes( MediaType.WILDCARD )
@Path( "/{platform}/paymentProvider" )
public interface IPaymentProviderService {

	/**
	 * Lists all the avalable {@link no.sumo.api.vo.RestProduct}s for a specific
	 * payment provider
	 *
	 * @throws no.sumo.api.exception.base.NotFoundException
	 *             when the payment provider does not exist.
	 */
	@GET
	@Formatted
	@Path( "/{paymentProviderId}/products" )
	public RestProductList getProducts( @PathParam( "platform" ) RestPlatform platform, @PathParam( "paymentProviderId" ) Long paymentProviderId ) throws NotFoundException;

}
