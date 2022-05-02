package com.example.cautiondoyouremember.Utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet

class RectOverlay internal constructor(overlay: GraphicOverlay,
private val bound:Rect?) : GraphicOverlay.Graphic(overlay){

    private val rectColor : Int = Color.GREEN

    private val strokeWidth : Float = 4.0f
    private var rectPaint : Paint = Paint()
    private lateinit var graphicOverlay: GraphicOverlay
    private lateinit var rect:Rect

    init {
        rectPaint.color = rectColor
        rectPaint.strokeWidth = strokeWidth
        rectPaint.style = Paint.Style.STROKE

        postInvalidate()
    }

    override fun draw(canvas: Canvas?) {

        val rect = RectF(bound)
        rect.left = translateX(rect.left)
        rect.right = translateX(rect.right)
        rect.top = translateY(rect.top)
        rect.bottom = translateY(rect.bottom)

        canvas?.drawRect(rect,rectPaint)
    }


}