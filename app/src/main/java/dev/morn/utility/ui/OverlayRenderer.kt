package dev.morn.utility.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.View
import android.view.WindowManager

class OverlayRenderer(private val context: Context) {

    private var windowManager: WindowManager? = null
    private var overlayView: View? = null
    private var showing = false

    fun isShowing() = showing

    fun show() {
        if (showing) return

        windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

        overlayView = object : View(context) {
            private val paint = Paint().apply {
                color = Color.GREEN
                style = Paint.Style.STROKE
                strokeWidth = 3f
                isAntiAlias = true
            }

            override fun onDraw(canvas: Canvas) {
                super.onDraw(canvas)
                // Здесь будет отрисовка боксов
            }
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        )
        params.gravity = Gravity.TOP or Gravity.START

        windowManager?.addView(overlayView, params)
        showing = true
    }

    fun hide() {
        if (!showing) return
        windowManager?.removeView(overlayView)
        overlayView = null
        showing = false
    }
}
