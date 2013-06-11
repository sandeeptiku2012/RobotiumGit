package com.comcast.ui.libv1.widget;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

public class BlobSurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {

	public static final int ANIMATION_DURATION = 100;
	BlobThread mThread;
	Paint mPaint;
	SurfaceHolder mSurfaceHolder;
	int mBlobColor = Color.argb(128, 255, 255, 255);
	boolean mIsTopmost = true;
	ValueAnimator va1;
	Point[] mPoints;

	int mBlobWidth = 50;
	Interpolator mInterpolator = new DecelerateInterpolator(2.2f);
	Bitmap mBlobBitmap;
	Bitmap focussedBitmapOverlay;

	public BlobSurfaceView(Context context) {
		this(context, null);
	}

	public BlobSurfaceView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BlobSurfaceView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		setBackgroundColor(Color.TRANSPARENT);
		if (!isInEditMode()) {
			setZOrderOnTop(mIsTopmost);
		}

		SurfaceHolder holder = getHolder();
		holder.setFormat(PixelFormat.TRANSPARENT);

		holder.addCallback(this);

		mPaint = new Paint();
		mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
		mPaint.setColor(mBlobColor);
		mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_OVER));
		mPaint.setAntiAlias(true);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		this.mSurfaceHolder = holder;
	}

	public void setBlobBitmap(int id) {
		mBlobBitmap = BitmapFactory.decodeResource(getContext().getResources(),
				id);
		mBlobWidth = mBlobBitmap.getWidth();
	}

	public void setFocussedBitmapOverlay(Drawable d) {
		if (d != null) {
			BitmapDrawable bd = (BitmapDrawable) d;
			focussedBitmapOverlay = bd.getBitmap();
		} else {
			focussedBitmapOverlay = null;
		}
	}

	public void animateBlob(final Point[] points) {
		mPoints = points;
		if (mThread == null) {
			mThread = new BlobThread();
			mThread.setInterpolator(mInterpolator);
			mThread.setPriority(Thread.MAX_PRIORITY);
			mThread.start();
		}

		ArrayList<Point> arrPoints = new ArrayList<Point>();
		for (Point point : points) {
			arrPoints.add(point);
		}

		mThread.addPoints(arrPoints);
	}

	public void setInterpolatorForDestination(Interpolator interpolator) {
		mInterpolator = interpolator;
	}

	private void animateBlob(final int x1, final int y1, final int x2,
			final int y2, Interpolator interpolator) {
		PropertyValuesHolder pvhX, pvhY;
		pvhX = PropertyValuesHolder.ofInt("x", x1, x2);
		pvhY = PropertyValuesHolder.ofInt("y", y1, y2);

		va1 = new ValueAnimator();
		va1.setInterpolator(mInterpolator);
		va1.setValues(pvhX, pvhY);
		va1.setDuration(ANIMATION_DURATION);

		va1.addUpdateListener(new AnimatorUpdateListener() {

			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int x = (Integer) animation.getAnimatedValue("x");
				int y = (Integer) animation.getAnimatedValue("y");
				if (mSurfaceHolder != null) {
					Canvas canvas = mSurfaceHolder.lockCanvas();
					try {
						synchronized (mSurfaceHolder) {
							canvas.drawColor(0, PorterDuff.Mode.CLEAR);
							canvas.drawBitmap(mBlobBitmap, x - mBlobWidth / 2,
									y - mBlobWidth / 2, mPaint);
						}
					} catch (Exception e) {
					} finally {
						mSurfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}

		});

		va1.addListener(new AnimatorListener() {

			@Override
			public void onAnimationCancel(Animator animation) {
			}

			@Override
			public void onAnimationEnd(Animator vanimation) {
				if (mSurfaceHolder != null && focussedBitmapOverlay != null) {
					ValueAnimator animation = (ValueAnimator) vanimation;
					int x = (Integer) animation.getAnimatedValue("x");
					int y = (Integer) animation.getAnimatedValue("y");

					Canvas canvas = mSurfaceHolder.lockCanvas();
					int focussedBitmapOverlayWidth = focussedBitmapOverlay
							.getWidth();
					try {
						synchronized (mSurfaceHolder) {
							canvas.drawColor(0, PorterDuff.Mode.CLEAR);
							canvas.drawBitmap(mBlobBitmap, x - mBlobWidth / 2,
									y - mBlobWidth / 2, mPaint);
							canvas.drawBitmap(focussedBitmapOverlay, x
									- focussedBitmapOverlayWidth / 2, y
									- focussedBitmapOverlayWidth / 2, mPaint);
						}
					} catch (Exception e) {
					} finally {
						mSurfaceHolder.unlockCanvasAndPost(canvas);
					}
				}
			}

			@Override
			public void onAnimationRepeat(Animator animation) {
			}

			@Override
			public void onAnimationStart(Animator animation) {
			}
		});
		va1.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		try {
			if (va1.isRunning()) {
				va1.cancel();
				va1.removeAllListeners();
			}

			mThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		mThread = null;
	}

	class BlobThread extends Thread {
		int x1, y1, x2, y2;
		ConcurrentLinkedQueue<Point> pendingList = new ConcurrentLinkedQueue<Point>();
		Interpolator interpolator;
		int index = 0;
		Handler mHandler;

		public BlobThread() {
			x1 = -1;
			y1 = -1;
			mHandler = new Handler(Looper.getMainLooper()) {
				@Override
				public void handleMessage(Message msg) {
					super.handleMessage(msg);
				}
			};
		}

		public void addPoints(ArrayList<Point> newPoints) {
			// Clear all previous points, we need not to animate those points as
			// the final position will be determined based on the last coordinate sent now
			pendingList.clear();
			pendingList.addAll(newPoints);
		}

		public void setInterpolator(Interpolator interpolator) {
			this.interpolator = interpolator;
		}

		@Override
		public void run() {
			Point p;
			while (true) {
				p = pendingList.poll();
				if (p != null) {
					if (x1 <= 0 && y1 <= 0) {
						x1 = p.x;
						y1 = p.y;
					} else {
						x2 = p.x;
						y2 = p.y;
						Runnable runnable = new Runnable() {
							@Override
							public void run() {
								Interpolator ip = null;
								if (pendingList.size() == 1) {
									ip = interpolator;
								}

								final int mx1 = x1, mx2 = x2, my1 = y1, my2 = y2;
								animateBlob(mx1, my1, mx2, my2, ip);
							}
						};

						mHandler.post(runnable);
						try {
							// Let the animation get completed :: keeping +50ms
							// time buffer
							Thread.sleep(ANIMATION_DURATION + 50);
						} catch (Exception e) {
						}

						x1 = x2;
						y1 = y2;
					}
				}
			}
		}
	}
}
