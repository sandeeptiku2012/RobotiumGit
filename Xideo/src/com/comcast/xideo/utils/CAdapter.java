package com.comcast.xideo.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.apache.http.conn.util.InetAddressUtils;

import com.comcast.xideo.R;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CAdapter extends ArrayAdapter<String> {

	public static class Utils {

		// The helper class used to get the IP Address

		public static String getIPAddress(boolean useIPv4) {
			try {
				List<NetworkInterface> interfaces = Collections
						.list(NetworkInterface.getNetworkInterfaces());
				for (NetworkInterface intf : interfaces) {
					List<InetAddress> addrs = Collections.list(intf
							.getInetAddresses());
					for (InetAddress addr : addrs) {
						if (!addr.isLoopbackAddress()) {
							String sAddr = addr.getHostAddress().toUpperCase();
							boolean isIPv4 = InetAddressUtils
									.isIPv4Address(sAddr);
							if (useIPv4) {
								if (isIPv4)
									return sAddr;
							} else {
								if (!isIPv4) {
									int delim = sAddr.indexOf('%'); // drop ip6
																	// port
																	// suffix
									return delim < 0 ? sAddr : sAddr.substring(
											0, delim);
								}
							}
						}
					}
				}
			} catch (Exception ex) {
			} // for now eat exceptions
			return "";
		}

	}

	private String wifiip, ethernetip, addressip;
	private ArrayList<String> ipadds;
	private String notavaliable = "Unavaliable";
	private final Context con;
	private final String ver;
	private String[] settings_to_display;
	TextView setting_value;

	public CAdapter(Context context, String[] set_values, String version) {
		super(context, R.layout.rowlayout, set_values);
		this.settings_to_display = set_values;
		this.con = context;
		this.ver = version;

		getthewifiip();

		// TODO Auto-generated constructor stub
	}

	private void getthewifiip() {
		WifiManager wifiman = (WifiManager) con
				.getSystemService(Context.WIFI_SERVICE);
		WifiInfo info = wifiman.getConnectionInfo();

		int myIp = info.getIpAddress();
		int intMyIp3 = myIp / 0x1000000;
		int intMyIp3mod = myIp % 0x1000000;

		int intMyIp2 = intMyIp3mod / 0x10000;
		int intMyIp2mod = intMyIp3mod % 0x10000;

		int intMyIp1 = intMyIp2mod / 0x100;
		int intMyIp0 = intMyIp2mod % 0x100;

		Log.d("WIFI IP IS ",
				String.valueOf(intMyIp0) + "." + String.valueOf(intMyIp1) + "."
						+ String.valueOf(intMyIp2) + "."
						+ String.valueOf(intMyIp3));

		wifiip = String.valueOf(intMyIp0) + "." + String.valueOf(intMyIp1)
				+ "." + String.valueOf(intMyIp2) + "."
				+ String.valueOf(intMyIp3);

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) con
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View rowview = inflater.inflate(R.layout.rowlayout, parent, false);
		TextView setting_name = (TextView) rowview.findViewById(R.id.textView1);
		setting_value = (TextView) rowview.findViewById(R.id.textView2);
		setting_name.setText(settings_to_display[position]);

		String s = settings_to_display[position];

		if (s.equals("Bluetooth MAC Address")) {
			BluetoothAdapter bluetoothadapter = BluetoothAdapter
					.getDefaultAdapter();

			if ((bluetoothadapter != null) && (bluetoothadapter.isEnabled())) {
				String mac = bluetoothadapter.getAddress();
				Log.d("DD", "BLUETOOTH ADDRESS IS --->" + mac);
				if (mac != null) {
					setting_value.setText(mac);
				}
			} else {
				setting_value.setText(notavaliable);
			}

		}

		if (s.equals("Kernel Version")) {
			String kernelversion = System.getProperty("os.version").toString();

			setting_value.setText(System.getProperty("os.version").toString());

		}

		/*
		 * if(s.equals("Wifi Mac Address")) { WifiManager wifiman=(WifiManager)
		 * con.getSystemService(Context.WIFI_SERVICE); WifiInfo
		 * info=wifiman.getConnectionInfo(); String
		 * wifimac=info.getMacAddress(); if(wifimac==null) {
		 * setting_value.setText( notavaliable); } else { setting_value.setText(
		 * info.getMacAddress()); }
		 * 
		 * }
		 */

		if (s.equals("Build Number")) {
			String builnumber = android.os.Build.DISPLAY;

			setting_value.setText(android.os.Build.DISPLAY);

		}

		if (s.equals("Android Version")) {
			String androidversion = android.os.Build.VERSION.RELEASE;

			setting_value.setText(android.os.Build.VERSION.RELEASE);

		}

		if (s.equals("Wifi IP Address")) {
			Log.d("GG", "The ip address is" + wifiip);
			if (wifiip.equals("0.0.0.0")) {
				setting_value.setText(notavaliable);
			} else {
				setting_value.setText(wifiip);
			}
		}

		if (s.equals("Xidio App Version")) {

			setting_value.setText(ver);
		}

		if (s.equals("Ethernet IP")) {
			if (wifiip.equals("0.0.0.0")) {
				String ethenetIP = Utils.getIPAddress(true);
				setting_value.setText(ethenetIP);
			} else
				setting_value.setText(notavaliable);
		}
		// TODO Auto-generated method stub
		return rowview;
	}

}
