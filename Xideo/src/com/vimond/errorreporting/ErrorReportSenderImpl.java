package com.vimond.errorreporting;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jboss.resteasy.util.Hex;

import roboguice.util.Ln;
import android.annotation.SuppressLint;
import android.os.Build;

import com.comcast.xideo.BuildConfig;
import com.comcast.xideo.XideoApplication;
import com.goldengate.guice.ErrorReportSenderSharedSecret;
import com.goldengate.guice.ErrorReportSenderUrl;
import com.google.inject.Inject;

public class ErrorReportSenderImpl implements ErrorReportSender {

	private final String MSG_SIGNATURE = "errorReport:";
	private final HttpClient httpClient = new DefaultHttpClient();
	private final String url;
	private final String sharedSecret;

	@Inject
	public ErrorReportSenderImpl( @ErrorReportSenderUrl String url, @ErrorReportSenderSharedSecret String sharedSecret ) {
		this.url = url;
		this.sharedSecret = sharedSecret;
	}

	@Override
	public void send( String errorMsg, Throwable e ) {
		if( BuildConfig.DEBUG ) {
			Ln.e( e );
		} else {
			// Add timestamp
			SimpleDateFormat sdf = new SimpleDateFormat( "yyyyMMdd HH:mm:ss.SSS" );
			errorMsg = errorMsg + "\nTimestamp startup: " + sdf.format( XideoApplication.getStartupTime() );
			errorMsg = errorMsg + "\nTimestamp now: " + sdf.format( new Date() );

			// Add device info to errorMsg
			errorMsg = errorMsg + "\n" + deviceInfo();

			// Add stacktrace to errorMsg
			errorMsg = errorMsg + "\n" + exception2String( e );

			// Encrypt the message
			String msgToSend = encrypt( MSG_SIGNATURE + errorMsg );

			HttpPost request = new HttpPost( url );
			request.setHeader( "Content-type", "text/plain" );
			request.setEntity( new ByteArrayEntity( msgToSend.getBytes() ) );
			HttpResponse response;
			try {
				response = httpClient.execute( request );
			} catch( IOException e1 ) {
				Ln.e( e, "Error sending this error report: \n" + errorMsg );
				Ln.e( e1, "Got this error while sending error report to " + url );
				return;
			}
			final int statusCode = response.getStatusLine().getStatusCode();
			if( statusCode == 200 ) {
				Ln.d( e, "Sent error report to remote server successfully:\n" + errorMsg );
			} else {
				Ln.e( e, "Error sending error report to remote server " + url + " (status-code: " + statusCode + "):\n" + errorMsg );
			}
		}
	}

	@SuppressLint( "NewApi" )
	private String deviceInfo() {
		StringBuilder sb = new StringBuilder();

		sb.append( "Build.ID=" ).append( Build.ID ).append( '\n' );
		sb.append( "Build.DISPLAY=" ).append( Build.DISPLAY ).append( '\n' );
		sb.append( "Build.PRODUCT=" ).append( Build.PRODUCT ).append( '\n' );
		sb.append( "Build.DEVICE=" ).append( Build.DEVICE ).append( '\n' );
		sb.append( "Build.BOARD=" ).append( Build.BOARD ).append( '\n' );
		sb.append( "Build.CPU_ABI=" ).append( Build.CPU_ABI ).append( '\n' );
		sb.append( "Build.CPU_ABI2=" ).append( Build.CPU_ABI2 ).append( '\n' );
		sb.append( "Build.MANUFACTURER=" ).append( Build.MANUFACTURER ).append( '\n' );
		sb.append( "Build.BRAND=" ).append( Build.BRAND ).append( '\n' );
		sb.append( "Build.MODEL=" ).append( Build.MODEL ).append( '\n' );
		sb.append( "Build.BOOTLOADER=" ).append( Build.BOOTLOADER ).append( '\n' );
		sb.append( "Build.HARDWARE=" ).append( Build.HARDWARE ).append( '\n' );
		sb.append( "Build.SERIAL=" ).append( Build.SERIAL ).append( '\n' );
		sb.append( "Build.TYPE=" ).append( Build.TYPE ).append( '\n' );
		sb.append( "Build.TAGS=" ).append( Build.TAGS ).append( '\n' );
		sb.append( "Build.FINGERPRINT=" ).append( Build.FINGERPRINT ).append( '\n' );
		sb.append( "Build.TIME=" ).append( Build.TIME ).append( '\n' );
		sb.append( "Build.USER=" ).append( Build.USER ).append( '\n' );
		sb.append( "Build.HOST=" ).append( Build.HOST ).append( '\n' );

		sb.append( "Build.VERSION.RELEASE=" ).append( Build.VERSION.RELEASE ).append( '\n' );
		sb.append( "Build.VERSION.INCREMENTAL=" ).append( Build.VERSION.INCREMENTAL ).append( '\n' );
		sb.append( "Build.VERSION.SDK_INT=" ).append( Build.VERSION.SDK_INT ).append( '\n' );
		sb.append( "Build.VERSION.CODENAME=" ).append( Build.VERSION.CODENAME ).append( '\n' );

		return sb.toString();
	}

	private String exception2String( Throwable e ) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter( out );
		e.printStackTrace( writer );
		writer.flush();
		return "Exception:\n" + new String( out.toByteArray() );
	}

	// encrypts errorReport and return as hex-string
	public String encrypt( String errorReport ) {
		try {
			byte[] rawKey = sharedSecret.getBytes();
			SecretKeySpec skeySpec = new SecretKeySpec( rawKey, "AES" );
			Cipher cipher = Cipher.getInstance( "AES" );
			cipher.init( Cipher.ENCRYPT_MODE, skeySpec );
			return new String( Hex.encodeHex( cipher.doFinal( errorReport.getBytes() ) ) );
		} catch( Exception e ) {
			throw new RuntimeException( "Error encrypting", e );
		}
	}

}
