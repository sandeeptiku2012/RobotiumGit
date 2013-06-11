package no.sumo.api.vo.category;

/**
 * Vimond Media Solutions AS
 * <p/>
 * User: dirceuvjr, Date: 03/07/12, Time: 12:54
 */

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root( name = "category" )
public class RestSearchCategory extends RestCategory {

	@Element( name = "voteAverage", required = false )
	private Long voteAverage;

	@Element( name = "voteAverageDouble", required = false )
	private Double voteAverageDouble;

	@Element( name = "voteCount", required = false )
	private Long voteCount;

	public Long getVoteAverage() {
		return voteAverage;
	}

	public void setVoteAverage( Long voteAverage ) {
		this.voteAverage = voteAverage;
	}

	public Double getVoteAverageDouble() {
		return voteAverageDouble;
	}

	public void setVoteAverageDouble( Double voteAverageDouble ) {
		this.voteAverageDouble = voteAverageDouble;
	}

	public Long getVoteCount() {
		return voteCount;
	}

	public void setVoteCount( Long voteCount ) {
		this.voteCount = voteCount;
	}
}
