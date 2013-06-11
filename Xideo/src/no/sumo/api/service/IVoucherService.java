package no.sumo.api.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import no.sumo.api.exception.base.GeneralFailureException;
import no.sumo.api.exception.generic.DataValidationException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.service.annotations.Wrapped;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.StringList;
import no.sumo.api.vo.platform.RestPlatform;
import no.sumo.api.vo.voucher.RestVoucher;
import no.sumo.api.vo.voucher.RestVoucherCodeList;
import no.sumo.api.vo.voucher.RestVoucherStatus;

@Consumes( MediaType.WILDCARD )
@Path( "voucher" )
public interface IVoucherService {

	/**
	 * Accepts a list of voucher codes
	 *
	 * @return A list of activated voucher codes
	 */
	@POST
	@Path( "/{platform}/codelist/submit" )
	//Why include platform in the path??
	@Formatted
	public RestVoucherCodeList submitVoucherList( @PathParam( "platform" ) RestPlatform platform, RestVoucherCodeList voucherList ) throws MethodNotAllowedException, DataValidationException, GeneralFailureException;

	@GET
	@Path( "/{code}" )
	@Formatted
	public RestVoucher getVoucher( @PathParam( "code" ) String code );

	/**
	 * Returns a list of all the vouchers in a pool
	 *
	 * @param pool
	 *            The name of the voucher pool
	 * @param firstPosition
	 *            start of the paging. Defaults to 0
	 * @param length
	 *            Length of the returned list. Defaults to 25
	 * @param status
	 *            The status of the vouchers
	 */
	@GET
	@Path( "/{pool}/vouchers/{status}" )
	@Wrapped( element = "vouchers" )
	@Formatted
	public List<RestVoucher> getVouchersInPool( @PathParam( "pool" ) String pool, @QueryParam( "firstPosition" ) @DefaultValue( "0" ) Integer firstPosition, @QueryParam( "length" ) @DefaultValue( "25" ) Integer length, @PathParam( "status" ) @DefaultValue( "all" ) RestVoucherStatus status );

	/**
	 * Returns a list of all the voucher pools
	 */
	@GET
	@Path( "/pools" )
	@Formatted
	public StringList getVoucherPools();

	@POST
	@Path( "" )
	@Formatted
	public StringList postVouchers( @QueryParam( "autoGenerate" ) @DefaultValue( "false" ) Boolean autoGenerate, @QueryParam( "count" ) @DefaultValue( "0" ) Integer count, @QueryParam( "prefix" ) String prefix, RestVoucher voucher );
}
