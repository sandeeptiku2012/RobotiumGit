package com.comcast.xideo;

import gueei.binding.AttributeBinder;
import gueei.binding.Binder;
import gueei.binding.bindingProviders.ExtraViewProvider;

import java.util.Date;

import roboguice.RoboGuice;
import android.app.Application;
import android.content.Context;
import android.os.Looper;

import com.adobe.mediacore.sample.logging.InMemoryLogger;
import com.comcast.cim.httpcomponentsandroid.impl.client.DefaultHttpClient;
import com.comcast.playerplatform.primetime.android.player.PlayerPlatformAPI;
import com.comcast.playerplatform.util.android.ConfigurationManager;
import com.comcast.playerplatform.util.android.IConfigurationEventListener;
import com.comcast.xideo.authentication.SilentLoginModule;
import com.comcast.xideo.viewmodel.XideoModel;
import com.google.inject.Module;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;
import com.vimond.errorreporting.ErrorReportSenderModule;
import com.vimond.imageservice.VimondImageServiceModule;
import com.vimond.rest.VimondApiModule;

public class XideoApplication extends Application {

	private static Context context;
	private static Date startupTime; // Keep track of when we started
	private static Bus bus;

	private static XideoModel model;
	public static PlayerPlatformAPI playerplatform;

	public final static boolean DEFAULT_CC_VISIBILITY = true,
			DEFAULT_QOS_VISIBILITY = true,
			DEFAULT_RETURN_HOME_ON_PLAY_COMPLETE = false,
			DEFAULT_AUTOHIDE_CONTROL_BAR = true;

	public static final String SETTINGS_CONTENT_URL_KEY = "settings_content_url";
	public static final String SETTINGS_LOG_MAX_ENTRY_COUNT_KEY = "settings_log_items_count";
	public static final String SETTINGS_LOG_VERBOSITY = "settings_log_verbosity";
	public static final String SETTINGS_CONTROLBAR_AUTOHIDE = "settings_controlbar_autohide";
	public static final String SETTINGS_CC_VISIBILITY = "settings_cc_visibility";
	public static final String SETTINGS_RETURN_HOME_ON_PLAY_COMPLETE = "settings_return_home_on_play_complete";
	public static final String SETTINGS_QOS_VISIBILITY = "settings_qos_visibility";
	public static final String SETTINGS_DRM_USERNAME = "settings_drm_username";
	public static final String SETTINGS_DRM_PASSWORD = "settings_drm_password";
	public static final String SETTINGS_DRM_KEY = "settings_drm_key";

	public static final String SETTINGS_ABR_CTRL_ENABLED = "settings_abr_enabled";
	public static final String SETTINGS_ABR_INITIAL_BITRATE = "settings_abr_initial_bit_rate";
	public static final String SETTINGS_ABR_MIN_BITRATE = "settings_abr_min_bit_rate";
	public static final String SETTINGS_ABR_MAX_BITRATE = "settings_abr_max_bit_rate";
	public static final String SETTINGS_ABR_POLICY = "settings_abr__policy";
	public static final String SETTINGS_CC_STYLE_FONT = "settings_cc_style_font";
	public static final String SETTINGS_CC_STYLE_FONT_COLOR = "settings_cc_style_font_color";
	public static final String SETTINGS_CC_STYLE_BACKGROUND_COLOR = "settings_cc_style_bg_color";
	public static final String SETTINGS_CC_STYLE_EDGE_COLOR = "settings_cc_style_edge_color";
	public static final String SETTINGS_CC_STYLE_SIZE = "settings_cc_style_size";
	public static final String SETTINGS_CC_STYLE_FONT_EDGE = "settings_cc_style_font_edge";
	public static final String SETTINGS_CC_STYLE_FONT_OPACITY = "settings_cc_style_font_opacity";
	public static final String SETTINGS_CC_STYLE_BACKGROUND_OPACITY = "settings_cc_style_bg_opacity";

	public static InMemoryLogger logger = new InMemoryLogger();

	@Override
	public void onCreate() {
		super.onCreate();

		XideoApplication.context = getApplicationContext();
		XideoApplication.startupTime = new Date();
		ConfigurationManager.getInstance().addListener( new IConfigurationEventListener() {
			@Override
			public void configurationLoaded() {
				checkCurrentThreadIsMainUiThread();
				playerplatform = new PlayerPlatformAPI( getApplicationContext(), new DefaultHttpClient() );
				ConfigurationManager.getInstance().removeListener( this );
			}

			@Override
			public void configurationFailed( String s ) {
				checkCurrentThreadIsMainUiThread();
				// PMPDemoApp.logger.i( LOG_TAG + "#configFailed",
				// "ConfigurationLoaded" );
				ConfigurationManager.getInstance().removeListener( this );
			}
		} );

		ConfigurationManager.getInstance().loadConfigurationFromXml(
				"<configuration type=\"stage\" version=\"0.10\">\n" +
				"<drmSettings>\n" +
				"<add key=\"CimaEndPoint\" value=\"https://csd11.ccp.xcal.tv/cima/qa/login\"/>\n" +
				"<add key=\"MetadataEndPoint\" value=\"https://csd11.ccp.xcal.tv/flashaccess/resource/metadata?env=bdn\"/>\n" +
				"<add key=\"XactEndPoint\" value=\"https://csd11.ccp.xcal.tv/xcalAuthCtxService/xcalAuthCtxToken\"/>\n" +
				"<add key=\"XsctEndPoint\" value=\"https://csd11.ccp.xcal.tv:8080/xbo/session\"/>\n" +
				"<add key=\"ProvisionEndPoint\" value=\"https://csd11.ccp.xcal.tv:8080/xbo/provision\"/>\n" +
				"<add key=\"DeprovisionEndPoint\" value=\"https://csd11.ccp.xcal.tv:8080/xbo/deprovision\"/>\n" +
				"<add key=\"ProductType\" value=\"quantum\"/>\n" +
				"</drmSettings>\n" +
				"<easSettings>\n" +
				"<add key=\"ZipToFipsEndPoint\" value=\"http://69.252.105.154/easws/api/fips/zip/\"/>\n" +
				"<add key=\"AlertServiceEndPoint\" value=\"http://69.252.105.154/easws/api/alert/history/fipscode/\"/>\n" +
				"<add key=\"EasUpdateInterval\" value=\"15000\"/>\n" +
				"<add key=\"EasAlertRepeat\" value=\"1\"/>\n" +
				"<add key=\"EasAlertFontSize\" value=\"20.0f\"/>\n" +
				"<add key=\"EasAlertFont\" value=\"Helvetica-Bold\"/>\n" +
				"</easSettings>\n" +
				"<analyticsSettings>\n" +
				"<add key=\"AnalyticsEndpoint\" value=\"http://analytics.xcal.tv/qa/player\"/>\n" +
				"<add key=\"AnalyticsProtocol\" value=\"1.2-p1\"/>\n" +
				"<add key=\"AnalyticsDeviceType\" value=\"IOS:1.0\"/>\n" +
				"<add key=\"MaxBatchSize\" value=\"10\"/>\n" +
				"<add key=\"MaxQueueSize\" value=\"100\"/>\n" +
				"<add key=\"BatchInterval\" value=\"5\"/>\n" +
				"</analyticsSettings>\n" +
				"<appSettings>\n" +
				"<add key=\"HeartbeatInterval\" value=\"60000\"/>\n" +
				"<add key=\"Autoplay\" value=\"true\"/>\n" +
				"<add key=\"MinimumBitrate\" value=\"0\"/>\n" +
				"<add key=\"MaximumBitrate\" value=\"12000000\"/>\n" +
				"<add key=\"InitialBitrate\" value=\"0\"/>\n" +
				"<add key=\"InitialBuffer\" value=\"5000\"/>\n" +
				"<add key=\"PlaybackBuffer\" value=\"60000\"/>\n" +
				"</appSettings>\n" +
				"<auditudeSettings>\n" +
				"<add key=\"DomainId\" value=\"auditude.com\"/>\n" +
				"<add key=\"ZoneId\" value=\"2637\"/>\n" +
				"<add key=\"CustomParams\" paramKey=\"plr\" paramValue=\"androidpts\"/>\n" +
				"</auditudeSettings>\n" +
				"</configuration>" );

		Module[] modules = new Module[] {
				RoboGuice.newDefaultRoboModule( this ),
				new XideoModule(),
				new VimondApiModule(),
				new VimondImageServiceModule(),
				new ErrorReportSenderModule(),
				new SilentLoginModule(),
		};

		RoboGuice.setBaseApplicationInjector( this, RoboGuice.DEFAULT_STAGE, modules );

		Binder.init( this );
		AttributeBinder.getInstance().registerProvider( new ExtraViewProvider() );
		bus = new Bus( ThreadEnforcer.ANY );
	}

	public static Context getContext() {
		return context;
	}

	public static Date getStartupTime() {
		return startupTime;
	}

	public static Bus getBus() {
		return bus;
	}

	public static XideoModel getModel() {
		return model;
	}

	public static void setModel( XideoModel m ) {
		model = m;
	}

	public void checkCurrentThreadIsMainUiThread() {
		if (!Looper.getMainLooper().getThread().equals( Thread.currentThread() )) {
			throw new RuntimeException( "Method called from non-UI thread" );
		}
	}

}
