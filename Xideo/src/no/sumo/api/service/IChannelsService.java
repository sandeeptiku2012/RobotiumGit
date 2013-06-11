package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import no.sumo.api.exception.NoValidChannelListException;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.asset.RestAsset;
import no.sumo.api.vo.asset.RestAssetList;
import no.sumo.api.vo.platform.RestPlatform;

@Consumes( MediaType.WILDCARD )
@Path( "/{platform}/channels" )
public interface IChannelsService {

	/**
	 * Return a collection of {@link RestAsset asset} items representing the
	 * available TV channels in the Sumo system.
	 *
	 * @param platform
	 *            The platform to filter the channel list against.
	 * @return A list of live channels available for the service.
	 * @throws NoValidChannelListException
	 *             If the channel list is not set up correctly.
	 */
	@GET
	@Formatted
	@Path( "/" )
	public RestAssetList getChannels( @PathParam( "platform" ) RestPlatform platform ) throws NoValidChannelListException;

}
