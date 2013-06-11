package com.comcast.xideo.viewmodel;

import gueei.binding.Command;
import gueei.binding.Observable;
import gueei.binding.collections.ArrayListObservable;
import gueei.binding.observables.BitmapObservable;
import gueei.binding.observables.BooleanObservable;
import gueei.binding.observables.IntegerObservable;
import gueei.binding.observables.LongObservable;
import gueei.binding.observables.StringObservable;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import no.knowit.misc.GoldenAsyncTask;
import no.sumo.api.entity.vman.enums.ProgramSortBy;
import no.sumo.api.service.IUserService;
import no.sumo.api.vo.category.rating.RestCategoryRating;

import org.jboss.resteasy.client.ClientResponseFailure;

import roboguice.event.EventManager;
import roboguice.event.Observes;
import roboguice.util.Ln;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.comcast.xideo.IChannel;
import com.comcast.xideo.ParentalGuidelines;
import com.comcast.xideo.R;
import com.comcast.xideo.SortButtonList;
import com.comcast.xideo.SortEvent;
import com.comcast.xideo.utils.BitmapLoader;
import com.comcast.xideo.viewmodel.VideoInfoViewModel;
import com.goldengate.guice.DataReadyEvent;
import com.goldengate.guice.VideoInfoViewModelFactory;
//import com.goldengate.publisher.PublisherActivity;
import com.goldengate.ui.CurrencyFormat;
import com.google.inject.Inject;
import com.vimond.entity.AssetProgress;
import com.vimond.entity.CategoryReference;
import com.vimond.entity.ChannelReference;
import com.vimond.entity.Order;
import com.vimond.entity.SubCategory;
import com.vimond.entity.Subscription;
import com.vimond.entity.SubscriptionStatus;
import com.vimond.entity.Video;
import com.vimond.service.Product;
import com.vimond.service.StbChannelService;

public class ChannelViewModel {
	// Video List
	public final ArrayListObservable<VideoInfoViewModel> videos = new ArrayListObservable<VideoInfoViewModel>( VideoInfoViewModel.class );
	public final Observable<VideoInfoViewModel> selectedVideo = new Observable<VideoInfoViewModel>( VideoInfoViewModel.class );
	// Header
	public final BitmapObservable logo = new BitmapObservable();
	public final BitmapObservable logoBig = new BitmapObservable();

	public final StringObservable title = new StringObservable();
	public final StringObservable description = new StringObservable();
	public final StringObservable expires = new StringObservable();
	// Menu
	public StringObservable price = new StringObservable();
	public final StringObservable subscribeError = new StringObservable();
	public final StringObservable publisher = new StringObservable();

	public final IntegerObservable parentalGuidanceResourceId = new IntegerObservable();
	public final IntegerObservable selectedIndex = new IntegerObservable();
	public final LongObservable numberOfVideos = new LongObservable();
	public LongObservable numberOfLikes = new LongObservable();
	public final BooleanObservable isEditorsPick = new BooleanObservable( false );

	public final BooleanObservable showSubscribed = new BooleanObservable();

	public final BooleanObservable showUnsubscribe = new BooleanObservable();

	public final BooleanObservable isLoading = new BooleanObservable();

	public final BooleanObservable loadingVideos = new BooleanObservable( false );

	public BooleanObservable hasAccess = new BooleanObservable();
	public BooleanObservable showSubscribeText = new BooleanObservable( false );
	public BooleanObservable hasLiked = new BooleanObservable( true );
	public BooleanObservable isTrending = new BooleanObservable( false );

	public StringObservable currentSortKey = new StringObservable();
	public BooleanObservable isChannelInfoVisible = new BooleanObservable( false );

	public BooleanObservable isVideoListVisible = new BooleanObservable( true );
	public final Observable<CategoryReference> category = new Observable<CategoryReference>( CategoryReference.class );

	private ChannelReference channel;

	@Inject
	private BitmapLoader loader;

	@Inject
	private StbChannelService channelService;

	@Inject
	private BitmapLoader bitmapLoader;

	@Inject
	private IUserService userService;

	@Inject
	private Context context;

	@Inject
	private AssetProgress progressService;

	@Inject
	private CurrencyFormat currencyFormat;

	@Inject
	private DateFormat dateFormat;

	@Inject
	private VideoInfoViewModelFactory videoInfoViewModelFactory;

	@Inject
	EventManager eventManager;

	@Inject
	public SortButtonList sortButtons;

	@Inject
	public ChannelViewModel( StbChannelService channelService ) {
		this.channelService = channelService;
	}

	public ChannelReference getChannel() {
		return channel;
	}

	public Command like = new Command() { // NO_UCD (unused code)
		@Override
		public void Invoke( View view, Object... args ) {
			new GoldenAsyncTask<Void>() {
				@Override
				public Void call() throws Exception {
					channelService.like( channel );
					return null;
				}

				@Override
				protected void onSuccess( Void t ) throws Exception {
					hasLiked.set( true );
					numberOfLikes.set( 1 + (numberOfLikes.isNull() ? 0 : numberOfLikes.get()) );
				};

				@Override
				protected void onException( Exception e ) throws RuntimeException {
				};
			}.execute();
		}
	};

	public Command unlike = new Command() { // NO_UCD (unused code)
		@Override
		public void Invoke( View view, Object... args ) {
			hasLiked.set( false );
			try {
				numberOfLikes.set( numberOfLikes.get() - 1 );
			} catch( NullPointerException e ) {
				e.printStackTrace();
			}
			new GoldenAsyncTask<Void>() {
				@Override
				public Void call() throws Exception {
					channelService.unlike( channel );
					return null;
				}

				@Override
				protected void onSuccess( Void t ) throws Exception {
					hasLiked.set( false );
				};

				@Override
				protected void onException( Exception e ) throws RuntimeException {
					e.printStackTrace();
					if( e.getClass() == ClientResponseFailure.class ) {

					}
					hasLiked.set( false );

				};
			}.execute();
		}
	};

	public Command showMoreFromPublisher = new Command() { // NO_UCD (unused code)
		@Override
		public void Invoke( View view, Object... args ) {
//			Intent intent = PublisherActivity.createIntent( context, publisher.get() );
//			Activity act = (Activity)context;
//			act.startActivityForResult( intent, FrontPageActivity.SHOW_MY_CHANNELS_CODE );
		}
	};

	public Command showRelatedChannels = null; // NO_UCD (unused code)

	public Command unsubscribe = new Command() { // NO_UCD (unused code)
		@Override
		public void Invoke( View arg0, Object... arg1 ) {
			setIsLoading( true );
			new GoldenAsyncTask<Boolean>() {
				@Override
				public Boolean call() throws Exception {

					return channelService.unsubscribe( channelService.getProduct( channel ) );
				}

				@Override
				protected void onSuccess( Boolean t ) throws Exception {
					setIsLoading( false );

					if( t ) {
						checkAccess( channel );
						successfulUnsubscription();
					} else {
						ErrorUnSubscribing();
					}

				}

				private void successfulUnsubscription() {
					showNoSubscribe();
					Toast.makeText( context, R.string.unsubscribed, Toast.LENGTH_SHORT ).show();
				};

				@Override
				protected void onException( Exception e ) throws RuntimeException {
					e.printStackTrace();
					setIsLoading( false );
					ErrorUnSubscribing();
				};
			}.execute();
		}

	};

	private void setIsLoading( Boolean isloading ) {
		isLoading.set( isloading );
	}

	public void makeSubscription( final Context activity ) {
		new GoldenAsyncTask<Product>() {
			@Override
			public Product call() throws Exception {
				Product product = channelService.getProduct( channel );
				return product;
			}

			@Override
			protected void onException( Exception e ) throws RuntimeException {
				e.printStackTrace();
				onSubscribeError( e.getMessage() );
			};

			@Override
			protected void onSuccess( final Product product ) throws Exception {
				if( product.isFree() ) {
					new GoldenAsyncTask<Order>() {

						@Override
						public Order call() throws Exception {
							return channelService.subscribe( product );
						}

						@Override
						protected void onSuccess( Order t ) throws Exception {
							successfulSubscription();
						};

						@Override
						protected void onException( Exception e ) throws RuntimeException {
							onSubscribeError( e.getMessage() );
						};
					}.execute();
				} else {
					final Dialog dialog = new Dialog( activity, R.style.FullHeightDialog );
					dialog.setContentView( R.layout.subscribe_dialog );
					dialog.setTitle( "Subscribe" );
					dialog.show();
					dialog.findViewById( R.id.menu_subscribe ).setOnClickListener( new OnClickListener() {
						@Override
						public void onClick( final View v ) {
							EditText pin = (EditText)dialog.findViewById( R.id.pinsubedit );
							final TextView tv = (TextView)dialog.findViewById( R.id.subscribe_error );
							if( pin.getText().length() > 0 ) {
								final Integer pinnumber = Integer.valueOf( pin.getText().toString() );
								new GoldenAsyncTask<Order>() {

									@Override
									public Order call() throws Exception {
										return channelService.subscribe( channel, product, pinnumber.longValue() );
									}

									@Override
									protected void onSuccess( Order t ) throws Exception {
										successfulSubscription();

										dialog.dismiss();
									}

									@Override
									protected void onException( Exception e ) throws RuntimeException {
										tv.setText( e.getMessage() );

										if( tv.getVisibility() == View.VISIBLE ) {
											tv.setVisibility( View.INVISIBLE );
										}
										tv.setVisibility( View.VISIBLE );
										showSubscribeOnly();
									};
								}.execute();

							} else {
								Toast.makeText( activity, R.string.please_enter_your_pin_, Toast.LENGTH_SHORT ).show();
							}
						}
					} );
				}
			};
		}.execute();
	}

	private Command focusVideo = new Command() {
		@Override
		public void Invoke( View view, Object... args ) {
			Ln.i( "focusVideo: %s", Arrays.asList( args ) );
			VideoInfoViewModel video = (VideoInfoViewModel)args[ 0 ];
			selectedIndex.set( videos.indexOf( video ) + 1 );
			selectedVideo.set( video );
		}
	};

	public void downloadChannelInfo() {
		setIsLoading( true );
		new GoldenAsyncTask<IChannel>() {
			@Override
			public IChannel call() throws Exception {
				return channelService.getChannel( channel );
			}

			@Override
			protected void onSuccess( IChannel t ) throws Exception {
				checkAccess( t );
				setChannel( t );
			}

			@Override
			protected void onException( Exception e ) throws RuntimeException {
				Ln.e( e );
				setIsLoading( false );
			};
		}.execute();

		new GoldenAsyncTask<SubCategory>() {
			@Override
			public SubCategory call() throws Exception {
				return channelService.getParentCategory( channel );
			}

			@Override
			protected void onSuccess( SubCategory cat ) throws Exception {
				if( cat != null && cat.getId() != 0 ) {
					category.set( cat );
				} else {
					onException( null );
				}
			}

			@Override
			protected void onException( Exception e ) throws RuntimeException {
				category.set( null );
			};
		}.execute();
	}

	public void downloadVideos( final ProgramSortBy sort ) {
		loadingVideos.set( true );
		new GoldenAsyncTask<List<Video>>() {
			@Override
			public List<Video> call() throws Exception {
				return channelService.getVideos( channel, 100, sort );
			}

			@Override
			protected void onException( Exception e ) throws RuntimeException {
				loadingVideos.set( false );
			};

			@Override
			protected void onSuccess( List<Video> result ) throws Exception {
				setVideos( result );
			}
		}.execute();
	}

	public final Command sort = new Command() {
		@Override
		public void Invoke( View view, Object... args ) {
			if( args.length > 0 ) {
				videos.clear();
				String sorting = (String)args[ 0 ];
				Ln.i( "sort: %s", sorting );
				downloadVideos( sortButtons.getSortKey() );
			}
			isChannelInfoVisible.set( false );
			isVideoListVisible.set( true );
		}
	};

	private void checkAccess( final ChannelReference channel ) {
		new GoldenAsyncTask<Subscription>() {
			@Override
			public Subscription call() throws Exception {
				return channelService.checkSubscription( channel );
			}

			@Override
			protected void onException( Exception e ) throws RuntimeException {
				e.printStackTrace();
				setIsLoading( false );
			};

			@Override
			protected void onSuccess( Subscription status ) throws Exception {
				super.onSuccess( status );
				new GoldenAsyncTask<RestCategoryRating>() {
					@Override
					public RestCategoryRating call() throws Exception {
						return channelService.getCurrentRating( channel );
					}

					@Override
					protected void onSuccess( RestCategoryRating t ) throws Exception {
						hasLiked.set( true );
					};

					@Override
					protected void onException( Exception e ) throws RuntimeException {
						hasLiked.set( false );
					};

				}.execute();
				new GoldenAsyncTask<Product>() {
					@Override
					public Product call() throws Exception {
						return channelService.getProduct( channel );
					}

					@Override
					protected void onException( Exception e ) throws RuntimeException {
						e.printStackTrace();
						setIsLoading( false );
					};

					@Override
					protected void onSuccess( Product t ) throws Exception {
						if( t.getPrice() != null && t.getPrice() > 0 ) {
							price.set( currencyFormat.format( t.getPrice() ) );
						} else {
							price.set( "FREE" );
						}
						setIsLoading( false );

					};

				}.execute();

				if( status.getStatus() == SubscriptionStatus.SHOW_SUBSCRIBE ) {
					showSubscribeOnly();
				} else if( status.getStatus() == SubscriptionStatus.SHOW_UNSUBSCRIBE ) {
					showUnsubscribeOnly();
				} else {
					try {
						expires.set( context.getString( R.string.your_access_expires_at_ ) + dateFormat.format( status.getSubdate() ) );
					} catch( NullPointerException e ) {
						expires.set( context.getString( R.string.no_expiration_ ) );
					}

					showNoSubscribe();
				}

			}
		}.execute();
	}

	private void onSubscribeError( String message ) {
		if( message != null ) {
			Toast.makeText( context, message, Toast.LENGTH_LONG ).show();
		}
		showSubscribeOnly();
	}

	private void ErrorUnSubscribing() {
		Toast.makeText( context, R.string.was_not_able_to_unsubscribe_please_try_again_later, Toast.LENGTH_SHORT ).show();
		showUnsubscribeOnly();
	}

	private void successfulSubscription() {
		Toast.makeText( context, R.string.you_are_now_subscribed_to_this_channel, Toast.LENGTH_SHORT ).show();
		showUnsubscribeOnly();
	};

	public void showSubscribeOnly() {
		showUnsubscribe.set( false );
		showSubscribed.set( true );
	}

	public void showUnsubscribeOnly() {
		showUnsubscribe.set( true );
		showSubscribed.set( false );
	}

	public void showNoSubscribe() {
		showSubscribeText.set( true );
		showUnsubscribe.set( false );
		showSubscribed.set( false );
	}

	public void start( final ChannelReference channelRef ) {
		addSortButtons();
		channel = channelRef;
		numberOfLikes.set( Long.valueOf( 0 ) );
		downloadChannelInfo();
		downloadVideos( sortButtons.getSortKey() );
	}

	private void addSortButtons() {
		sortButtons.clear();

		sortButtons.addButton( R.string.newest, ProgramSortBy.PUBLISDATE_DESC );
		sortButtons.addButton( R.string.most_viewed, ProgramSortBy.COUNT );
		sortButtons.addButton( R.string.most_popular, ProgramSortBy.RATINGCOUNT );

		sortButtons.setSortKey( ProgramSortBy.PUBLISDATE_DESC );
	}

	public void onSort( @Observes SortEvent event ) { // NO_UCD (unused code)
		Ln.i( "SORT" );
		if( event.getSource() == sortButtons ) {
			downloadVideos( sortButtons.getSortKey() );
			isChannelInfoVisible.set( false );
			isVideoListVisible.set( true );
		}
	}

	private void showChannelInfo() {
		isChannelInfoVisible.set( true );
		isVideoListVisible.set( false );
		currentSortKey.set( "channel-info" );
		sortButtons.selectButton( null );
	}

	public Command setMode = new Command() {
		@Override
		public void Invoke( View view, Object... args ) {
			showChannelInfo();
		}
	};

	private VideoInfoViewModel convert( Video video ) {
		VideoInfoViewModel vm = videoInfoViewModelFactory.create( video );
		vm.onGainFocus = focusVideo;
		vm.publisher.set( null );
		vm.updateProgress( progressService );

		if( video.getImagePack() != null ) {

		}
		bitmapLoader.loadImage( vm.image, video );
		return vm;
	};

	void setChannel( IChannel t ) {
		title.set( t.getTitle() );
		description.set( t.getDescription() );
		publisher.set( t.getPublisher() );
		parentalGuidanceResourceId.set( ParentalGuidelines.getResourceId( t.getTelevisionRating() ) );
		isEditorsPick.set( t.isEditorsPick() );
		if( loader != null ) {
			loader.loadImage( logo, t );
			loader.loadBigChannelLogo( logoBig, t );
		}

		Ln.i( "likes: %s", t.getLikes() );
		numberOfLikes.set( t.getLikes() );
	}

	private void setVideos( List<Video> result ) {
		ArrayList<Long> assetIds = new ArrayList<Long>( videos.size() );

		loadingVideos.set( false );
		videos.clear();

		Ln.i( "Received %d videos", result.size() );

		numberOfVideos.set( (long)result.size() );

		for( Video video : result ) {
			assetIds.add( video.getId() );
			videos.add( convert( video ) );
		}

		for( VideoInfoViewModel videoModel : videos ) {
			videoModel.setPlaylist( assetIds );
		}

		selectedVideo.set( videos.get( 0 ) );

		eventManager.fire( new DataReadyEvent( this ) );
	};
}
