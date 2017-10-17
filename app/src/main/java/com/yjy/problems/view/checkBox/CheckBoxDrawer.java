package com.yjy.problems.view.checkBox;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.view.animation.DecelerateInterpolator;

public class CheckBoxDrawer {

    private Paint backGroundPaint = new Paint();

    private Paint mMarkerPaint = new Paint();

    private float mCenter = 0;

    private int mCheckedColor = 0;

    private int mUnCheckedColor = 0;

    private float mDistance;

    private float[] mTickLine1;

    private float[] mTickLine2;

    private float[] mCrossLine1;

    private float[] mCrossLine2;

    private float mPlayTime;

    private ObjectAnimator mObjectAnimator;

    private Matrix mMatrix = new Matrix();


    public CheckBoxDrawer(ValueAnimator.AnimatorUpdateListener listener) {
        mMarkerPaint.setColor(Color.WHITE);
        mMarkerPaint.setStyle(Paint.Style.STROKE);
        mMarkerPaint.setStrokeCap(Paint.Cap.ROUND);

        mObjectAnimator = ObjectAnimator.ofFloat(
                this, "PlayTime", 0, 1
        );
        mObjectAnimator.setDuration(300);
        mObjectAnimator.setInterpolator(new DecelerateInterpolator());
        mObjectAnimator.addUpdateListener(listener);
    }

    public void draw(Canvas canvas) {
        canvas.setMatrix(mMatrix);

        drawBackground(canvas);
        drawMarker(canvas);
    }

    private void drawBackground(Canvas canvas) {
        float rInnerCircle = mCenter * mPlayTime;

        backGroundPaint.setColor(mUnCheckedColor);
        canvas.drawCircle(mCenter, mCenter, mCenter, backGroundPaint);

        backGroundPaint.setColor(mCheckedColor);
        backGroundPaint.setAlpha((int) (255 * mPlayTime));
        canvas.drawCircle(mCenter, mCenter, rInnerCircle, backGroundPaint);
    }

    private void drawMarker(Canvas canvas) {

        float pt1[] = new float[2];
        float pt2[] = new float[2];

        getPtInLineByTime(mPlayTime, mCrossLine1[0], mCrossLine1[1], mTickLine1[0], mTickLine1[1], pt1);
        getPtInLineByTime(mPlayTime, mCrossLine1[2], mCrossLine1[3], mTickLine1[2], mTickLine1[3], pt2);
        canvas.drawLine(pt1[0], pt1[1], pt2[0], pt2[1], mMarkerPaint);

        getPtInLineByTime(mPlayTime, mCrossLine2[0], mCrossLine2[1], mTickLine2[0], mTickLine2[1], pt1);
        getPtInLineByTime(mPlayTime, mCrossLine2[2], mCrossLine2[3], mTickLine2[2], mTickLine2[3], pt2);
        canvas.drawLine(pt1[0], pt1[1], pt2[0], pt2[1], mMarkerPaint);
    }

    private void getPtInLineByTime(
            float playTime, float x1, float y1, float x2, float y2, float[] pt) {

        float x = (x2 - x1) * playTime + x1;
        float y = (x - x1) / (x2 - x1) * (y2 - y1) + y1;

        pt[0] = x;
        pt[1] = y;
    }

    public void onSizeChanged(float width, int height) {
        initMatrix(width, height);
        initBackground(width, height);
        initMarker(width, height);
    }

    private void initMatrix(float width, int height) {
        float shorterEdge = Math.min(width, height);
        mMatrix.setTranslate(
                (width - shorterEdge) / 2.0f,
                (height - shorterEdge) / 2.0f);
    }

    private void initBackground(float width, int height) {
        float r = Math.min(width, height);
        mCenter = r / 2.0f;
    }

    private void initMarker(float width, int height) {
        float r = Math.min(width, height);
        mMarkerPaint.setStrokeWidth(r / 8.0f);
        mDistance = r / 5.0f;
        mTickLine1 = new float[]{mDistance * 2.5f, mDistance * 4.0f, mDistance * 3.75f, mDistance * 1.25f};
        mTickLine2 = new float[]{mDistance, mDistance * 3.0f, mDistance * 2.5f, mDistance * 4.0f};
        mCrossLine1 = new float[]{mDistance * 3.5f, mDistance * 3.5f, mDistance * 1.5f, mDistance * 1.5f};
        mCrossLine2 = new float[]{mDistance * 1.5f, mDistance * 3.5f, mDistance * 3.5f, mDistance * 1.5f};
    }

    public void setCheckedColor(int checkedColor) {
        mCheckedColor = checkedColor;
    }

    public void setUnCheckedColor(int unCheckedColor) {
        mUnCheckedColor = unCheckedColor;
    }

    public void toChecked() {
        if (mObjectAnimator.isRunning()) {
            mObjectAnimator.cancel();
        }
        mObjectAnimator.setFloatValues(mPlayTime, 1.0f);
        mObjectAnimator.start();
    }

    public void toUnChecked() {
        if (mObjectAnimator.isRunning()) {
            mObjectAnimator.cancel();
        }
        mObjectAnimator.setFloatValues(getPlayTime(), 0.0f);
        mObjectAnimator.start();
    }

    public float getPlayTime() {
        return mPlayTime;
    }

    public void setPlayTime(float playTime) {
        mPlayTime = playTime;
    }
}
