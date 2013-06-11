package no.sumo.api.vo.playqueue;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.contracts.ConversionFactory;
import no.sumo.api.contracts.IPlaybackQueue;
import no.sumo.api.vo.RestObject;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.Transient;

@Root( name = "playqueue" )
public class RestPlaybackQueue extends RestObject implements IPlaybackQueue {
	@Attribute( required = false )
	private Long id;

	@Element( name = "name" )
	private String name;

	private Long memberId;

	private Boolean autoAddEnabled;

	private Boolean selected;

	private Boolean collapsed;

	private Integer numberOfItems;

	@Transient
	private List<ApiPlaybackQueueItem> listToStore = new ArrayList<ApiPlaybackQueueItem>();

	@Transient
	private List<ApiPlaybackQueueItem> listToShow = null;

	@ElementList( entry = "queueitem", inline = true, required = false )
	private List<RestPlaybackQueueItem> restListToShow = new ArrayList<RestPlaybackQueueItem>();

	public Long getId() {
		return id;
	}

	public void setId( Long id ) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName( String name ) {
		this.name = name;
	}

	public Long getMemberId() {
		return memberId;
	}

	public void setMemberId( Long memberId ) {
		this.memberId = memberId;
	}

	public Boolean getAutoAddEnabled() {
		return autoAddEnabled;
	}

	public void setAutoAddEnabled( Boolean autoAddEnabled ) {
		this.autoAddEnabled = autoAddEnabled;
	}

	public Boolean getSelected() {
		return selected;
	}

	public void setSelected( Boolean selected ) {
		this.selected = selected;
	}

	public Boolean getCollapsed() {
		return collapsed;
	}

	public void setCollapsed( Boolean collapsed ) {
		this.collapsed = collapsed;
	}

	public List<ApiPlaybackQueueItem> getListToStore() {
		return listToStore;
	}

	public void setListToStore( List<ApiPlaybackQueueItem> listToStore ) {
		this.listToStore = listToStore;
	}

	public List<ApiPlaybackQueueItem> getListToShow() {
		return listToShow;
	}

	public void setListToShow( List<ApiPlaybackQueueItem> listToShow ) {
		this.listToShow = listToShow;

		for( ApiPlaybackQueueItem i : this.listToShow ) {
			RestPlaybackQueueItem ri = new RestPlaybackQueueItem();
			ConversionFactory.getPlaybackQueueItemConverter().transfer( i, ri );
			getRestListToShow().add( ri );
		}

	}

	public void setNumberOfItems( Integer numberOfItems ) {
		this.numberOfItems = numberOfItems;
	}

	public Integer getNumberOfItems() {
		return numberOfItems;
	}

	public void setRestListToShow( List<RestPlaybackQueueItem> restListToShow ) {
		this.restListToShow = restListToShow;
	}

	public List<RestPlaybackQueueItem> getRestListToShow() {
		return restListToShow;
	}

}
