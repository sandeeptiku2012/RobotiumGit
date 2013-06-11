package com.comcast.xideo;

import java.util.Random;

import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.inputmethod.BaseInputConnection;

public class DelayedKeyPressThread extends Thread {
	int delay = 2000;
	Random rnd = new Random();
	BaseInputConnection bic;

	public DelayedKeyPressThread(Activity ac) {
		bic = new BaseInputConnection( ac.getWindow().getDecorView(), false );
	}

	public void run() {
		try {
			Thread.sleep( 2000 );
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		while (true) {
			int keycode = 0;
			int r = rnd.nextInt( 100 ) % 4;

			switch (r) {
				case 1:
					keycode = KeyEvent.KEYCODE_DPAD_LEFT; // 21
					break;
				case 2:
					keycode = KeyEvent.KEYCODE_DPAD_UP; // 19
					break;
				case 3:
					keycode = KeyEvent.KEYCODE_DPAD_DOWN; // 20
					break;
				default:
					keycode = KeyEvent.KEYCODE_DPAD_RIGHT;// 22
					break;
			}

			Log.d( this.getClass().getName(), "key:" + r );

			KeyEvent event2 = new KeyEvent( 0, 0, KeyEvent.ACTION_DOWN, keycode, 0, KeyEvent.META_SYM_ON, 0, 0, KeyEvent.FLAG_VIRTUAL_HARD_KEY );

			bic.sendKeyEvent( event2 );

			try {
				Thread.sleep( delay );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
