package com.netease.foolman.fontselector;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.netease.foolman.fontselector.utils.Converter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by wangyuebanzi on 2017/11/8.
 */

public class FontSelector extends View {

    private static final int INVALID_POSITION = -1;
    private static final int DEFAULT_GAP_DP = 6;
    private static final int DEFAULT_LINE_WIDTH = 2;
    private static final int DEFAULT_TEXT_SIZE = 16;

    private float defaultGapForSP;
    private float defaultLineWidth;
    private float defaultTextSize;
    private int defaultCircleBitmapResourceId;

    private OnPositionChangedListener mListener;

    private Bitmap mCircleBitmap;
    private int mCircleBitmapWidth;
    private int mCircleBitmapHeight;

    private Paint mTextPaint;
    private Paint mLinePaint;

    private List<String> textsList = new ArrayList<String>();
    private List<Integer> pointsList = new ArrayList<>();

    private @ColorInt
    int mTextColor;
    private @ColorInt
    int mLineColor;

    private @ColorInt
    int mTextChosedColor;

    private int mTextHeight;

    private int itemGap;

    private int mCurrentPosition;
    private int mLastPosition;

    private float mCircleDrawX;


    public FontSelector(Context context) {
        this(context, null);
        getContext();
    }

    public FontSelector(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public FontSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initParam();
    }

    private void initParam() {
        defaultGapForSP = Converter.dp2px(getContext(), DEFAULT_GAP_DP);
        defaultLineWidth = Converter.dp2px(getContext(), DEFAULT_LINE_WIDTH);
        defaultTextSize = Converter.dp2px(getContext(), DEFAULT_TEXT_SIZE);
        defaultCircleBitmapResourceId = R.drawable.news_base_menu_change_font_icon;

        mLineColor = Color.WHITE;
        mTextColor = Color.WHITE;
        mTextChosedColor = Color.RED;

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setStrokeWidth(defaultLineWidth);
        mLinePaint.setColor(mLineColor);

        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(defaultTextSize);
        mTextPaint.setTextAlign(Paint.Align.LEFT);
        mTextPaint.setColor(mTextColor);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        mTextHeight = (int) Math.abs(fontMetrics.bottom - fontMetrics.top);

        mCircleBitmap = BitmapFactory.decodeResource(getResources(), defaultCircleBitmapResourceId);
        mCircleBitmapWidth = mCircleBitmap.getWidth();
        mCircleBitmapHeight = mCircleBitmap.getHeight();

        String[] textStrs = getResources().getStringArray(R.array.font_size);
        textsList.addAll(Arrays.asList(textStrs));

        enablePointPosition();

    }

    private void enablePointPosition() {
        post(new Runnable() {
            @Override
            public void run() {
                confirmPointPosition();
                mCircleDrawX = pointsList.get(mCurrentPosition) - mCircleBitmapWidth / 2;
            }
        });
    }

    private void confirmPointPosition() {
        int size = textsList.size();
        pointsList.clear();
        if (size > 1) {
            int viewWidth = getMeasuredWidth();
            //int firstTextWidth = (int) mTextPaint.measureText(textsList.get(0));
            //int lastTextWidth = (int) mTextPaint.measureText(textsList.get(size-1));

            //int maxSide = Math.max(mCircleBitmapWidth,Math.max(firstTextWidth,lastTextWidth));
            //内置了一个圆圈的padding，分到左右各一半。
            int validWidth = viewWidth - getPaddingRight() - getPaddingLeft() - mCircleBitmapWidth;
            for (int i = 0; i < size; i++) {
                validWidth -= mTextPaint.measureText(textsList.get(i));
            }
            int position = getPaddingLeft() + mCircleBitmapWidth / 2;
            itemGap = validWidth / (textsList.size() - 1);
            for (int i = 0; i < textsList.size(); i++) {
                int textWidth = (int) mTextPaint.measureText(textsList.get(i));
                pointsList.add(position + textWidth / 2);
                position += itemGap + textWidth;
            }
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMeasureMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthMeasureSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMeasureMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightMeasureSize = MeasureSpec.getSize(heightMeasureSpec);

        int defaultHeigthSize = (int) (getPaddingTop() + mTextHeight + defaultGapForSP +
                Math.max(defaultLineWidth, mCircleBitmapHeight) + getPaddingBottom());
        int defaultWidthSize = getPaddingLeft() + getPaddingRight() + mCircleBitmapWidth;

        if (defaultWidthSize < defaultHeigthSize / 0.618) {
            defaultWidthSize = (int) (defaultHeigthSize / 0.618);
        }

        if (heightMeasureMode == MeasureSpec.AT_MOST && widthMeasureMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultWidthSize, defaultHeigthSize);
        } else if (heightMeasureMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(widthMeasureSize, defaultHeigthSize);
        } else if (widthMeasureMode == MeasureSpec.AT_MOST) {
            setMeasuredDimension(defaultWidthSize, heightMeasureSize);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // super.onDraw(canvas);
        if (textsList.size() > 1) {
            confirmPointPosition();
            drawText(canvas);
            drawLine(canvas);
            drawBitmap(canvas);
        }
    }

    private void drawBitmap(Canvas canvas) {
        int bitmapY = (int) (getPaddingTop() + mTextHeight + defaultGapForSP);
        canvas.drawBitmap(mCircleBitmap, mCircleDrawX, bitmapY, null);
    }

    private void drawLine(Canvas canvas) {
        int lineY = (int) (getPaddingTop() + mTextHeight + defaultGapForSP + mCircleBitmapHeight / 2);
        canvas.drawLine(pointsList.get(0), lineY, pointsList.get(pointsList.size() - 1), lineY, mLinePaint);
    }

    private void drawText(Canvas canvas) {

        int textY = getPaddingTop() + mTextHeight;
        for (int i = 0; i < textsList.size(); i++) {
            int tempTextColor = i == mCurrentPosition ? mTextChosedColor : mTextColor;
            mTextPaint.setColor(tempTextColor);
            String text = textsList.get(i);
            if (!TextUtils.isEmpty(text)) {
                float currentTextWidth = mTextPaint.measureText(textsList.get(i));
                canvas.drawText(textsList.get(i), pointsList.get(i) - currentTextWidth / 2, textY, mTextPaint);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mCircleDrawX = event.getX();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float tempX = event.getX();
                float left = pointsList.get(0) - mCircleBitmapWidth / 2;
                float right = pointsList.get(pointsList.size() - 1) - mCircleBitmapWidth / 2;
                mCircleDrawX = Float.compare(tempX, left) < 0 ? left :
                        Float.compare(tempX, right) > 0 ? right : tempX;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                float tempUpX = event.getX();
                mCurrentPosition = adjustCirclePos(tempUpX + mCircleBitmapWidth / 2);
                if (mCurrentPosition != INVALID_POSITION) {
                    mCircleDrawX = pointsList.get(mCurrentPosition) - mCircleBitmapWidth / 2;
                    if (mListener != null && mCurrentPosition != mLastPosition) {
                        mListener.onPositionChanged(mCurrentPosition);
                    }
                    mLastPosition = mCurrentPosition;
                }
                invalidate();
                break;
        }

        return true;
    }

    private int adjustCirclePos(float circleDrawX) {
        for (int i = 0; i < pointsList.size(); i++) {
            int point = pointsList.get(i);
            String text = textsList.get(i);
            float textWidth = mTextPaint.measureText(text);
            if (Math.abs(circleDrawX - point) < (itemGap + textWidth) / 2) {
                return i;
            }
        }
        return INVALID_POSITION;
    }

    public void setOnPositionChangedListener(OnPositionChangedListener listener) {
        this.mListener = listener;
    }

    public interface OnPositionChangedListener {
        void onPositionChanged(int position);
    }

}
