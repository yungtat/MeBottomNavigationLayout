package xt.yungtat

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet

/**
 * 圆角矩形
 */
class MsgView : AppCompatTextView {

    private val gd_background = GradientDrawable()
    private var backgroundColor: Int = 0
    private var cornerRadius: Int = 0
    private var strokeWidth: Int = 0
    private var strokeColor: Int = 0


    constructor(context: Context, backgroundColor: Int, cornerRadius: Int, strokeWidth: Int, strokeColor: Int) : super(context) {
        this.backgroundColor = backgroundColor
        this.cornerRadius = cornerRadius
        this.strokeWidth = strokeWidth
        this.strokeColor = strokeColor
    }

    @JvmOverloads
    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr)


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        setCornerRadius(height / 2)
    }


    override fun setBackgroundColor(backgroundColor: Int) {
        this.backgroundColor = backgroundColor
        setBgSelector()
    }

    fun setCornerRadius(cornerRadius: Int) {
        this.cornerRadius = DensityUtils.dp2px(context, cornerRadius.toFloat())
        setBgSelector()
    }

    fun setStrokeWidth(strokeWidth: Int) {
        this.strokeWidth = DensityUtils.dp2px(context, strokeWidth.toFloat())
        setBgSelector()
    }

    fun setStrokeColor(strokeColor: Int) {
        this.strokeColor = strokeColor
        setBgSelector()
    }


    fun getBackgroundColor(): Int {
        return backgroundColor
    }

    fun getCornerRadius(): Int {
        return cornerRadius
    }

    fun getStrokeWidth(): Int {
        return strokeWidth
    }

    fun getStrokeColor(): Int {
        return strokeColor
    }


    private fun setDrawable(gd: GradientDrawable, color: Int, strokeColor: Int) {
        gd.setColor(color)
        gd.cornerRadius = cornerRadius.toFloat()
        gd.setStroke(strokeWidth, strokeColor)
    }

    fun setBgSelector() {
        val bg = StateListDrawable()

        setDrawable(gd_background, backgroundColor, strokeColor)
        bg.addState(intArrayOf(-android.R.attr.state_pressed), gd_background)
        setBackgroundDrawable(bg)

    }
}
