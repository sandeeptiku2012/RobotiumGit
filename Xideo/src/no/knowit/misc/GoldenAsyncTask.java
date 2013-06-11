package no.knowit.misc;

import java.util.concurrent.Executor;

import roboguice.RoboGuice;
import roboguice.util.Ln;
import roboguice.util.SafeAsyncTask;
import android.os.Handler;

import com.comcast.xideo.XideoApplication;
import com.google.inject.Inject;
import com.vimond.errorreporting.ErrorReportSender;

public abstract class GoldenAsyncTask<ResultT> extends SafeAsyncTask<ResultT> {
	public static Executor defaultExecutor = null;
	public static Handler defaultHandler = null;

	@Inject
	private ErrorReportSender errorReportSender;

	public GoldenAsyncTask() {
		super( defaultHandler, defaultExecutor != null ? defaultExecutor : DEFAULT_EXECUTOR );
	}

	@Override
	protected void onException( Exception e ) throws RuntimeException {
		super.onException( e );
		try {
			if( errorReportSender == null ) {
				RoboGuice.injectMembers( XideoApplication.getContext(), this );
			}
			errorReportSender.send( e.getMessage(), e );
		} catch( Throwable ex ) {
			Ln.e( ex, "Got exception inside onException-handler" );
		}
	}
}
