package com.example.customview.kotlin

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout

class MyView : FrameLayout{
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

}