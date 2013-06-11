package com.goldengate.ui;

import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

public class StaticNoiseView extends View {
	private static final int NUMBER_OF_TILES = 20;
	private static final int TILE_WIDTH = 64;
	private static final int TILE_HEIGHT = 64;
	private static int[] colors = new int[ NUMBER_OF_TILES * TILE_WIDTH * TILE_HEIGHT ];

	private Canvas offScreenCanvas;
	private Bitmap offScreenBitmap;

	static Random random = new Random();
	static {
		for( int i = 0; i < colors.length; i++ ) {
			int intensity = random.nextInt() & 0x7f;
			colors[ i ] = intensity << 16 | intensity << 8 | intensity;
		}
	}

	public StaticNoiseView( Context context ) {
		super( context );
	}

	public StaticNoiseView( Context context, AttributeSet attributes ) {
		super( context, attributes );
	}

	@Override
	protected void onSizeChanged( int width, int height, int oldWidth, int oldHeight ) {
		destroy();
		offScreenBitmap = Bitmap.createBitmap( width, height, Bitmap.Config.ARGB_8888 );
		offScreenCanvas = new Canvas( offScreenBitmap );
	}

	public void destroy() {
		if( offScreenBitmap != null ) {
			offScreenBitmap.recycle();
		}
	}

	@Override
	protected void onDraw( Canvas canvas ) {
		Rect rect = offScreenCanvas.getClipBounds();
		for( int y = rect.top; y < rect.bottom; y += TILE_HEIGHT ) {
			for( int x = rect.left; x < rect.right; x += TILE_WIDTH ) {
				int offset = random.nextInt( (NUMBER_OF_TILES - 1) * TILE_WIDTH * TILE_HEIGHT );
				offScreenCanvas.drawBitmap( colors, offset, TILE_WIDTH, x, y, TILE_WIDTH, TILE_HEIGHT, false, null );
			}
		}
		canvas.drawBitmap( offScreenBitmap, 0, 0, null );
		invalidate();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		destroy();
	}
}
