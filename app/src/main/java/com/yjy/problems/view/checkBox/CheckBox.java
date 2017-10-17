package com.yjy.problems.view.checkBox;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.yjy.problems.R;

public class CheckBox extends View {

    public static final int DEFAULT_CHECKED_COLOR = Color.BLACK;

    public static final int DEFAULT_UNCHECKED_COLOR = 0x20000000;

    private static final int DEFAULT = 30;

    private final CheckBoxDrawer mCheckBoxDrawer;

    private boolean isChecked = false;

    private OnClickListener mOnClickListener;

    private GestureDetector mGestureDetector;

    public CheckBox(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray =
                context.obtainStyledAttributes(attrs, R.styleable.CheckBox);
        int checkedColor =
                typedArray.getColor(R.styleable.CheckBox_checkedColor, DEFAULT_CHECKED_COLOR);
        int unCheckedColor =
                typedArray.getColor(R.styleable.CheckBox_unCheckedColor, DEFAULT_UNCHECKED_COLOR);

        typedArray.recycle();

        mCheckBoxDrawer = new CheckBoxDrawer(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                invalidate();
            }
        });
        mCheckBoxDrawer.setCheckedColor(checkedColor);
        mCheckBoxDrawer.setUnCheckedColor(unCheckedColor);

        mGestureDetector = new GestureDetector(context, getOnGestureListener());
    }

    @NonNull
    private GestureDetector.OnGestureListener getOnGestureListener() {
        return new GestureDetector
                .SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                setChecked(!isChecked());

                if (mOnClickListener != null) {
                    mOnClickListener.onClick(CheckBox.this);
                }
                return false;
            }

            @Override
            public boolean onDown(MotionEvent e) {
                return true;
            }
        };
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;

        if (isChecked()) {
            mCheckBoxDrawer.toChecked();
        } else {
            mCheckBoxDrawer.toUnChecked();
        }

    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        mOnClickListener = l;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCheckBoxDrawer.draw(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mCheckBoxDrawer.onSizeChanged(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getSize(widthMeasureSpec);
        int height = getSize(heightMeasureSpec);

        setMeasuredDimension(width, height);
    }

    private int getSize(int specSize) {
        int mode = MeasureSpec.getMode(specSize);
        int size = MeasureSpec.getSize(specSize);

        switch (mode) {
            case MeasureSpec.UNSPECIFIED:
                return DEFAULT;
            case MeasureSpec.AT_MOST:
                return DEFAULT < size ? DEFAULT : size;
            case MeasureSpec.EXACTLY:
                return size;
        }
        return size;
    }

}
