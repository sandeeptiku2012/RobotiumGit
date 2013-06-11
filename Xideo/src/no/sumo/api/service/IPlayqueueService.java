package no.sumo.api.service;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.service.annotations.Wrapped;
import no.sumo.api.service.decorator.Formatted;
import no.sumo.api.vo.platform.RestPlatform;
import no.sumo.api.vo.playqueue.RestPlaybackQueue;
import no.sumo.api.vo.playqueue.RestPlaybackQueueItem;
import no.sumo.api.vo.playqueue.RestPlaybackQueueList;

@Consumes( MediaType.WILDCARD )
@Path( "/" )
public interface IPlayqueueService {

	@GET
	@Formatted
	@Wrapped(element="playqueues")
	@Path("/{platform}/user/playqueues")
	public RestPlaybackQueueList getPlayqueues( 		@PathParam("platform") RestPlatform platform ) throws MethodNotAllowedException;
	
	@POST
	@Formatted
	@Wrapped(element="playqueue")
	@Path("/{platform}/user/playqueues")
	public RestPlaybackQueue postPlayqueue(				@PathParam("platform") RestPlatform platform,
														RestPlaybackQueue restPlaybackQueue) throws MethodNotAllowedException;
	
	@DELETE
	@Formatted
	@Path("/{platform}/user/playqueues/{queueId}")
	public void deletePlayqueue(						@PathParam("platform") RestPlatform platform,
														@PathParam("queueId") Long queueId) throws Exception;
	
	@GET
	@Formatted
	@Wrapped(element="playqueue")
	@Path("/{platform}/user/playqueues/{queueId}")
	public RestPlaybackQueue getPlayqueue(				@PathParam("platform") RestPlatform platform,
														@PathParam("queueId") Long queueId) throws MethodNotAllowedException;
	
	
	@POST
	@Formatted
	@Wrapped(element="playqueue")
	@Path("/{platform}/user/playqueues/{queueId}/{assetId}")
	public RestPlaybackQueueItem postPlayqueueItem(		@PathParam("platform") RestPlatform platform,
														@PathParam("queueId") Long queueId,
														@PathParam("assetId") Long assetId) throws MethodNotAllowedException;
	
	@DELETE
	@Formatted
	@Path("/{platform}/user/playqueues/{queueId}/{itemId}")
	public void deletePlayqueueItem(					@PathParam("platform") RestPlatform platform,
                                                        @PathParam("queueId") Long queueId,
														@PathParam("itemId") Long itemId) throws MethodNotAllowedException;

    @POST
    @Formatted
    @Wrapped(element="playqueue")
    @Path("/{platform}/user/playqueues/{queueId}")
    public RestPlaybackQueue sortPlayqueue(	@PathParam("platform") RestPlatform platform,
                                                        @PathParam("queueId") Long queueId,
                                                        @QueryParam("sortBy") String sortBy) throws MethodNotAllowedException;

}
