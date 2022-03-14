package com.example.customview.java;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.customview.R;

public class MyView extends FrameLayout {

    private ImageView mTopBg;
    private ImageView mBottomBg;
    private int mTopBgHeight;
    private int mViewWidthSize;
    private int mViewHeightSize;
    private ImageView mImageView1;
    private ImageView mImageView2;
    private TextView mTextView;
    private final int mImageView1Margin = 20;
    private final int mImageView2Margin = 50;
    private final int mTextMargin = 20;

    public MyView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化控件
     */
    private void init(Context context) {
        mTopBg = new ImageView(context);
        mTopBg.setBackgroundColor(getResources().getColor(R.color.purple_200));
        addView(mTopBg);

        mBottomBg = new ImageView(context);
        mBottomBg.setBackgroundColor(getResources().getColor(R.color.purple_500));
        addView(mBottomBg);

        mImageView1 = new ImageView(context);
        LayoutParams layoutParams1 = new LayoutParams(100, 100);
        mImageView1.setLayoutParams(layoutParams1);
        mImageView1.setImageResource(R.mipmap.ic_launcher);
        mImageView1.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mImageView1.setBackgroundColor(getResources().getColor(R.color.black));
        addView(mImageView1);

        mImageView2 = new ImageView(context);
        LayoutParams layoutParams2 = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mImageView2.setLayoutParams(layoutParams2);
        mImageView2.setImageResource(R.mipmap.ic_launcher);
        mImageView2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mImageView2.setBackgroundColor(getResources().getColor(R.color.black));
        addView(mImageView2);

        mTextView = new TextView(context);
        LayoutParams textLayoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        mImageView2.setLayoutParams(textLayoutParams);
        mTextView.setText(R.string.text1);
        addView(mTextView);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mViewWidthSize = MeasureSpec.getSize(widthMeasureSpec);// 屏幕宽度值
        mViewHeightSize = MeasureSpec.getSize(heightMeasureSpec);// 屏幕高度值
        mTopBgHeight = mViewHeightSize / 5 * 2;// 上方背景占据屏幕高度的2/5
        measureChild(mImageView1);
        measureChild(mImageView2);
        // 计算text的宽度值
        int textWidth = mViewWidthSize - mImageView1Margin - mImageView1.getMeasuredWidth() - mTextMargin * 2 - mImageView2.getMeasuredWidth() - mImageView2Margin;
        int textHeight = measureSize(mViewHeightSize - mTopBgHeight, mTextView.getLayoutParams().height);
        mTextView.measure(getExactlySize(textWidth), textHeight);
        setMeasuredDimension(mViewWidthSize, mViewHeightSize);
    }

    private void measureChild(View view) {
        int width = measureSize(mViewWidthSize, view.getLayoutParams().width);
        int height = measureSize(mViewHeightSize, view.getLayoutParams().height);
        view.measure(width, height);
    }

    private int measureSize(int viewSize, int widgetSize) {
        if (widgetSize == LayoutParams.MATCH_PARENT) {
            return getExactlySize(viewSize);
        } else if (widgetSize == LayoutParams.WRAP_CONTENT) {
            return getAtMostSize(viewSize);
        } else {
            return getExactlySize(widgetSize);
        }
    }

    private int getExactlySize(int size) {
        return MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
    }

    private int getAtMostSize(int size) {
        return MeasureSpec.makeMeasureSpec(size, MeasureSpec.AT_MOST);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mTopBg.layout(0, 0, mViewWidthSize, mTopBgHeight);
        mBottomBg.layout(0, mTopBgHeight, mViewWidthSize, mViewHeightSize);
        layout(mImageView1, mImageView1Margin, mTopBgHeight - mImageView1.getMeasuredHeight() / 2);
        layout(mImageView2, mViewWidthSize - mImageView2Margin - mImageView2.getMeasuredWidth(), mTopBgHeight - mImageView2.getMeasuredHeight() / 2);
        layout(mTextView, mImageView1Margin + mImageView1.getMeasuredWidth() + mTextMargin, mTopBgHeight + mTextMargin);
    }

    private void layout(View view, int x, int y) {
        view.layout(x, y, x + view.getMeasuredWidth(), y + view.getMeasuredHeight());
    }
}
