package com.vimond.service;

import java.util.ArrayList;
import java.util.List;

import no.sumo.api.entity.sumo.enums.AutorenewStatus;
import no.sumo.api.entity.sumo.enums.PaymentMethod;
import no.sumo.api.entity.vman.enums.ProgramSortBy;
import no.sumo.api.exception.base.NotFoundException;
import no.sumo.api.exception.base.ValidationException;
import no.sumo.api.exception.category.CategoryNotFoundException;
import no.sumo.api.exception.category.rating.CategoryRatingFoundException;
import no.sumo.api.exception.category.rating.CategoryRatingNotFoundException;
import no.sumo.api.exception.generic.DataIncompleteException;
import no.sumo.api.exception.generic.MethodNotAllowedException;
import no.sumo.api.exception.payment.PaymentException;
import no.sumo.api.exception.user.UserNotFoundException;
import no.sumo.api.service.ICategoryService;
import no.sumo.api.service.IOrderService;
import no.sumo.api.service.IProductService;
import no.sumo.api.service.ISearchService;
import no.sumo.api.service.IUserContentService;
import no.sumo.api.service.IUserService;
import no.sumo.api.vo.asset.RestSearchAsset;
import no.sumo.api.vo.asset.RestSearchAssetList;
import no.sumo.api.vo.category.RestCategory;
import no.sumo.api.vo.category.RestSearchCategory;
import no.sumo.api.vo.category.RestSearchCategoryList;
import no.sumo.api.vo.category.rating.RestCategoryRating;
import no.sumo.api.vo.order.RestOrder;
import no.sumo.api.vo.order.RestOrderList;
import no.sumo.api.vo.platform.RestPlatform;
import no.sumo.api.vo.product.RestPaymentObject;
import no.sumo.api.vo.product.RestProduct;
import no.sumo.api.vo.product.RestProductGroup;
import no.sumo.api.vo.product.RestProductGroupList;
import no.sumo.api.vo.product.RestProductList;
import no.sumo.api.vo.product.RestProductPayment;

import org.jboss.resteasy.client.ClientResponseFailure;

import roboguice.util.Ln;

import com.comcast.xideo.Constants;
import com.comcast.xideo.IChannel;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.vimond.entity.CategoryReference;
import com.vimond.entity.ChannelReference;
import com.vimond.entity.Order;
import com.vimond.entity.SubCategory;
import com.vimond.entity.Subscription;
import com.vimond.entity.SubscriptionStatus;
import com.vimond.entity.UserReference;
import com.vimond.entity.Video;
import com.vimond.rest.Converters;

@Singleton
public class StbChannelService {
	private static final String NEW_ASSETS_FILTER = "publish:[NOW/DAY-7DAYS TO NOW]";

	@Inject
	private ISearchService searchService;

	@Inject
	private RestPlatform platform;

	@Inject
	private IUserService userService;

	@Inject
	private ICategoryService categoryService;

	@Inject
	private IProductService productService;

	@Inject
	private IOrderService orderService;

	@Inject
	private IUserContentService userContentService;

	@Inject
	private StbAuthenticationService authenticationService;

	@Inject
	public StbChannelService( ISearchService searchService ) {
		this.searchService = searchService;
	}

	public List<Video> getVideos( ChannelReference channel, int limit ) throws NotFoundException {
		RestSearchAssetList assets = searchService.getAssets( platform, Long.toString( channel.getId() ), null, null, 0, limit, null, null );
		return Converters.convertToVideoList( assets );
	}

	public List<Video> getVideos( ChannelReference channel, int limit, String sort ) throws NotFoundException {
		ProgramSortBy sorting = null;
		try {
			sorting = ProgramSortBy.valueOf( sort );
		} catch( IllegalArgumentException e ) {

		}
		return getVideos( channel, limit, sorting );
	}

	public List<Video> getVideos( ChannelReference channel, int limit, ProgramSortBy sorting ) throws NotFoundException {
		RestSearchAssetList assets = searchService.getAssets( platform, Long.toString( channel.getId() ), null, sorting, 0, limit, null, null );
		return Converters.convertToVideoList( assets );
	}

	public Video getNewestVideo( ChannelReference channel ) throws NotFoundException {
		RestSearchAssetList assets = searchService.getAssets( platform, Long.toString( channel.getId() ), null, null, 0, 1, null, null );
		if( assets.getAssets().size() > 0 ) {
			return Converters.convert( assets.getAssets().get( 0 ) );
		}
		return null;
	}

	public void like( ChannelReference channel ) throws CategoryRatingFoundException, CategoryNotFoundException, UserNotFoundException {
		UserReference user = authenticationService.getUser();
		RestCategoryRating rating = new RestCategoryRating();
		rating.setMemberId( user.getId() );
		rating.setCategoryId( channel.getId() );
		rating.setRating( 5L );
		categoryService.postRating( platform, channel.getId(), rating );
	}

	public RestCategoryRating getCurrentRating( ChannelReference channel ) {
		UserReference user = authenticationService.getUser();
		return categoryService.getRatingByUser( platform, channel.getId(), user.getId() );
	}

	public void unlike( ChannelReference channel ) throws CategoryRatingFoundException, CategoryNotFoundException, UserNotFoundException, CategoryRatingNotFoundException {

		categoryService.deleteRating( platform, channel.getId(), getCurrentRating( channel ).getId() );
	}

	public Subscription checkSubscription( ChannelReference channel ) throws MethodNotAllowedException, NotFoundException {

		try {
			RestOrderList orders = userService.getOrders( platform, authenticationService.getUser().getId(), channel.getId() );

			for( RestOrder order : orders.getOrders() ) {
				if( order.getCategoryId() != null ) {
					if( order.getCategoryId().longValue() == channel.getId() && order.getAutorenewStatus() == AutorenewStatus.STOPPED ) {
						Ln.i( "order: hide both" );
						Subscription subscription = new Subscription();
						subscription.setSubdate( order.getAccessEndDate() );
						subscription.setStatus( SubscriptionStatus.HIDE_BOTH_BUTTONS );
						return subscription;
					} else if( order.getCategoryId().longValue() == channel.getId() && order.getAutorenewStatus() == AutorenewStatus.ACTIVE ) {
						Subscription subscription = new Subscription();
						subscription.setStatus( SubscriptionStatus.SHOW_UNSUBSCRIBE );
						return subscription;
					}
				}
			}
		} catch( NullPointerException e ) {
			// no orderss
		}
		Subscription subscription = new Subscription();
		subscription.setStatus( SubscriptionStatus.SHOW_SUBSCRIBE );

		return subscription;
	}

	public long getNumberOfVideos( ChannelReference channel ) throws NotFoundException {
		String id = Long.toString( channel.getId() );
		RestSearchAssetList assets = searchService.getAssets( platform, id, null, null, 0, 0, null, null );
		return Long.parseLong( assets.getNumberOfHits() );
	}

	public RestSearchAssetList getAssets( long categoryId ) throws NotFoundException {
		String id = Long.toString( categoryId );
		RestSearchAssetList assets = searchService.getAssets( platform, id, null, null, null );
		return assets;
	}

	public List<IChannel> getChannels( CategoryReference category, int size ) throws NotFoundException {
		String id = Long.toString( category.getId() );
		RestSearchCategoryList channels = searchService.getCategories( platform, id, null, null, 0, size, null, null );
		List<IChannel> result = new ArrayList<IChannel>();
		for( RestSearchCategory c : channels.getCategories() ) {
			result.add( Converters.convert( c ) );
		}
		return result;
	}

	public List<IChannel> getMyChannels() throws MethodNotAllowedException, NotFoundException {
		UserReference user = authenticationService.getUser();
		RestSearchCategoryList categories = userContentService.getSubscribedChannels(platform, user.getId(), null, 0, 25, null);
		ArrayList<IChannel> channels = new ArrayList<IChannel>();
		for( RestSearchCategory category : categories.getCategories() ) {
			channels.add( Converters.convert( category ) );
		}
		return channels;
	}

	public List<Video> getNewVideosInMyChannels() throws MethodNotAllowedException, NotFoundException {
		return getNewVideosInMyChannels( 4, (ProgramSortBy)null );
	}

	public List<Video> getNewVideosInMyChannels( int limit, String sort ) throws MethodNotAllowedException, NotFoundException {
		ProgramSortBy sorting = null;
		try {
			sorting = ProgramSortBy.valueOf( sort );
		} catch( NullPointerException e ) {
		}

		return getNewVideosInMyChannels( limit, sorting );
	}

	public List<Video> getNewVideosInMyChannels( int limit, ProgramSortBy sorting ) throws NotFoundException, MethodNotAllowedException {
		UserReference user = authenticationService.getUser();
		RestSearchAssetList assets = userContentService.getAccessibleAssets( platform, user.getId(), NEW_ASSETS_FILTER, sorting, 0, limit, null );
		ArrayList<Video> videos = new ArrayList<Video>();
		for( RestSearchAsset asset : assets.getAssets() ) {
			videos.add( Converters.convert( asset ) );
		}

		return videos;
	}

	public long getNumberOfNewVideosInMyChannels() throws MethodNotAllowedException, NotFoundException {
		UserReference user = authenticationService.getUser();
		RestSearchAssetList assets = userContentService.getAccessibleAssets( platform, user.getId(), NEW_ASSETS_FILTER, null, 0, 0, null );
		return Long.parseLong( assets.getNumberOfHits() );
	}

	public long getNumberOfNewVideos( ChannelReference channel ) throws NotFoundException {
		String subCategory = Long.toString( channel.getId() );
		RestSearchAssetList assets = searchService.getAssets( platform, subCategory, NEW_ASSETS_FILTER, null, 0, 0, null, null );
		return Long.valueOf( assets.getNumberOfHits() );
	}

	public SubCategory getParentCategory( ChannelReference channel ) throws NotFoundException {
		RestCategory restCategory = categoryService.getCategory( platform, channel.getId() );
		final RestCategory parentCategory = restCategory.getParent();
		return new SubCategory() {
			@Override
			public long getId() {
				return parentCategory.getId();
			}

			@Override
			public String getTitle() {
				return parentCategory.getTitle();
			}
		};
	}

	public IChannel getChannel( ChannelReference channel ) throws NotFoundException {
		long channelId = channel.getId();
		RestSearchCategoryList result = searchService.getCategories( platform, Long.toString( channelId ), null, null, 0, 1, null, null );
		List<RestSearchCategory> categories = result.getCategories();

		if( categories != null ) {
			RestSearchCategory category = categories.get( 0 );
			return Converters.convert( category );
		}
		throw new CategoryNotFoundException( channelId );
	}

	// new API -- list of episodes
	public List<Video> getEpisodes(String showId) throws NotFoundException {
		RestSearchAssetList result = searchService.getAssets( platform, showId, null, null, null );
		List<RestSearchAsset> assets = result.getAssets();

		List<Video> episodes = new ArrayList<Video>();
		for (RestSearchAsset asset : assets) {
			Video episode = Converters.convert(asset);
			if (episode != null)
				episodes.add(episode);
		}

		return episodes;
	}

	public Product getProduct( ChannelReference channel ) throws NotFoundException {
		RestProductGroupList groups = categoryService.getProductGroups( platform, channel.getId() );
		assert groups != null;
		for( RestProductGroup group : groups.getProductGroups() ) {
			RestProductList products = productService.getProducts( platform, group.getId() );
			for( RestProduct product : products.getProducts() ) {
				for( RestProductPayment payment : productService.getProductPayments( platform, group.getId(), product.getId(), null ).getProductPaymentList() ) {
					return new Product( payment.getId(), product.getPrice() );
				}
			}
		}
		return null;
	}

	public void subscribe( ChannelReference channel ) throws NotFoundException, DataIncompleteException, MethodNotAllowedException, PaymentException, ValidationException {
		subscribe( getProduct( channel ) );
	}

	public Order subscribe( ProductReference product ) throws NotFoundException, DataIncompleteException, MethodNotAllowedException, PaymentException, ValidationException {
		UserReference user = authenticationService.getUser();
		RestOrder newOrder = new RestOrder();
		newOrder.setUserId( user.getId() );
		newOrder.setProductPaymentId( product.getId() );
		RestOrder restOrder = orderService.initializeOrder( platform, newOrder );

		completeOrder( restOrder );

		return new Order( restOrder.getId() );
	}

	public boolean unsubscribe( ProductReference product ) throws NotFoundException, DataIncompleteException, MethodNotAllowedException, PaymentException, ValidationException {
		UserReference user = authenticationService.getUser();
		RestOrderList orders = userService.getOrders( platform, user.getId(), null );
		for( RestOrder order : orders.getOrders() ) {
			Ln.i( "order: Iterating" );
			if( order.getProductPaymentId() == product.getId() ) {
				Ln.i( "order: Found order." );

				orderService.terminateOrder( platform, order.getId() );
				return true;
			}
		}

		return false;
	}

	public Order subscribe( ChannelReference channel, ProductReference product, Long pinCode ) throws NotFoundException, DataIncompleteException, MethodNotAllowedException, PaymentException, ValidationException {
		UserReference user = authenticationService.getUser();
		RestOrder newOrder = new RestOrder();
		newOrder.setUserId( user.getId() );
		newOrder.setProductPaymentId( product.getId() );
		newOrder.setCategoryId( channel.getId() );
		RestPaymentObject pm = new RestPaymentObject();
		pm.setPaymentMethod( PaymentMethod.ONE_CLICK_BUY_PIN );
		pm.setPinCode( pinCode );
		newOrder.setPaymentObject( pm );

		RestOrder completeOrder = completeOrder( newOrder );
		return new Order( completeOrder.getId() );
	}

	RestOrder completeOrder( RestOrder newOrder ) throws PaymentException, NotFoundException, MethodNotAllowedException, ValidationException {
		try {
			return orderService.completeOrder( platform, newOrder );
		} catch( ClientResponseFailure ex ) {
			if( ex.getCause() instanceof ValidationException ) {
				throw (ValidationException)ex.getCause();
			}
			if( ex.getCause() instanceof PaymentException ) {
				throw (PaymentException)ex.getCause();
			}
			if( ex.getCause() instanceof NotFoundException ) {
				throw (NotFoundException)ex.getCause();
			}
			throw ex;
		}
	}

	public List<IChannel> findChannels( String title, int limit ) throws NotFoundException {
		ArrayList<IChannel> channels = new ArrayList<IChannel>();
		String query = String.format( "\"%s\"", title.replace( "\"", "" ) );
		RestSearchCategoryList result = searchService.getCategories( platform, Long.toString( Constants.MAIN_CATEGORY ), null, null, 0, limit, query, null );
		for( RestSearchCategory category : result.getCategories() ) {
			channels.add( Converters.convert( category ) );
		}
		return channels;
	}
}
