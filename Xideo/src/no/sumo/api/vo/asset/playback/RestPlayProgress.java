package no.sumo.api.vo.asset.playback;

import java.util.Date;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "playProgress" )
public class RestPlayProgress {
	@Element
	public Long offsetSeconds;

	@Element( required = false )
	public Date lastUpdated;
}
