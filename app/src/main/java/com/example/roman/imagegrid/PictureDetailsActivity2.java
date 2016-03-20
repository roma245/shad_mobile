package com.example.roman.imagegrid;

/**
 * Created by Roman on 13.03.2016.
 */

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.widget.ImageView;

public class PictureDetailsActivity2 extends Activity {

    private static final String PACKAGE_NAME = "com.example.android.imagegrid";
    private ImageView imageView;

    private ScaleGestureDetector scaleDetector;
    private float scaleFactor = 1.f;
    private float lastTouchX;
    private float lastTouchY;
    private int activePointerId;
    private float totalDiffX;
    private float totalDiffY;
    private static final int INVALID_POINTER_ID = -1;

    //variable for counting two successive up-down events
    private int clickCount = 0;
    //variable for storing the time of first click
    private long startTime;
    //variable for calculating the total time
    private long duration;
    //constant for defining the time duration between the click that can be considered as double-tap
    private static final int MAX_DURATION = 500;

    private boolean zoomStatus = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_info2);

        imageView = (ImageView) findViewById(R.id.imageView2);

        Bundle bundle = getIntent().getExtras();
        Bitmap bitmap = BitmapUtils.getBitmap(getResources(),
                bundle.getInt(PACKAGE_NAME + ".resourceId"));

        imageView.setImageBitmap(bitmap);

        setupZoom();
    }


    private void setupZoom() {
        scaleDetector = new ScaleGestureDetector(this, new ScaleListener());
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        scaleDetector.onTouchEvent(ev);

        if (ev.getPointerCount() > 1) {
            return true;
        }

       // TODO: for correct coordinates has to be scaled to min{screen.width, screen.height}
       // final float scale = getResources().getDisplayMetrics().density;

        int centerXOnImage=imageView.getWidth()/2;
        int centerYOnImage=imageView.getHeight()/2;
        int centerXOfImageOnScreen=imageView.getLeft()+centerXOnImage;
        int centerYOfImageOnScreen=imageView.getTop()+centerYOnImage;

        Rect rect = new Rect(
                Math.round((imageView.getLeft()-centerXOfImageOnScreen)*scaleFactor+centerXOfImageOnScreen),
                Math.round((imageView.getTop()-centerYOfImageOnScreen)*scaleFactor+centerYOfImageOnScreen),
                Math.round((imageView.getRight()-centerXOfImageOnScreen)*scaleFactor+centerXOfImageOnScreen),
                Math.round((imageView.getBottom()-centerYOfImageOnScreen)*scaleFactor)+centerYOfImageOnScreen);

        if (!rect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
            this.finish();
            return true;
        }

        final int action = MotionEventCompat.getActionMasked(ev);

        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Remember where we started (for dragging)
                lastTouchX = x;
                lastTouchY = y;
                // Save the ID of this pointer (for dragging)
                activePointerId = MotionEventCompat.getPointerId(ev, 0);

                startTime = System.currentTimeMillis();
                clickCount++;

                break;
            }

            case MotionEvent.ACTION_MOVE: {
                // Find the index of the active pointer and fetch its position
                final int pointerIndex =
                        MotionEventCompat.findPointerIndex(ev, activePointerId);

                final float x = MotionEventCompat.getX(ev, pointerIndex);
                final float y = MotionEventCompat.getY(ev, pointerIndex);

                // Calculate the distance moved
                final float dx = x - lastTouchX;
                final float dy = y - lastTouchY;

                totalDiffX += dx;
                totalDiffY += dy;

                imageView.setTranslationX(totalDiffX);
                imageView.setTranslationY(totalDiffY);

                // Remember this touch position for the next move event
                lastTouchX = x;
                lastTouchY = y;

                break;
            }

            case MotionEvent.ACTION_UP: {
                activePointerId = INVALID_POINTER_ID;

                long time = System.currentTimeMillis() - startTime;
                duration=  duration + time;
                if(clickCount == 2) {
                    if (duration <= MAX_DURATION) {
                        if (!zoomStatus) {
                            scaleFactor = 2;
                            zoomStatus = true;
                        } else {
                            scaleFactor = 1;
                            zoomStatus = false;
                        }
                        imageView.setScaleX(scaleFactor);
                        imageView.setScaleY(scaleFactor);
                    }
                    clickCount = 0;
                    duration = 0;
                }

                break;
            }

            case MotionEvent.ACTION_CANCEL: {
                activePointerId = INVALID_POINTER_ID;
                break;
            }

            case MotionEvent.ACTION_POINTER_UP: {

                final int pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = MotionEventCompat.getPointerId(ev, pointerIndex);

                if (pointerId == activePointerId) {
                    // This was our active pointer going up. Choose a new
                    // active pointer and adjust accordingly.
                    final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
                    lastTouchX = MotionEventCompat.getX(ev, newPointerIndex);
                    lastTouchY = MotionEventCompat.getY(ev, newPointerIndex);
                    activePointerId = MotionEventCompat.getPointerId(ev, newPointerIndex);
                }
                break;
            }
        }
        return true;
    }

    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scaleFactor *= detector.getScaleFactor();
            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 5.0f));

            imageView.setScaleX(scaleFactor);
            imageView.setScaleY(scaleFactor);
            return true;
        }
    }

}
