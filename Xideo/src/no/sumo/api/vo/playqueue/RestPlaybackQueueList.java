package no.sumo.api.vo.playqueue;

import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root( name = "playqueues" )
public class RestPlaybackQueueList {
	@ElementList( entry = "playqueue", inline = true, required = false )
	private List<RestPlaybackQueue> queues;

	public List<RestPlaybackQueue> getQueues() {
		return queues;
	}
}
