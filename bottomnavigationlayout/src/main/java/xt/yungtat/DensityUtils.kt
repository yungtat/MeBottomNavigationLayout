package xt.yungtat

import android.content.Context
import android.util.TypedValue

/**
 * Created by F.X.Tong on 2017/8/24.
 * dp,sp,xp转换类
 */

object DensityUtils {

    fun px2dp(var0: Context, var1: Float): Int {
        val var2 = var0.resources.displayMetrics.density
        return (var1 / var2 + 0.5f).toInt()
    }

    fun px2sp(var0: Context, var1: Float): Int {
        val var2 = var0.resources.displayMetrics.scaledDensity
        return (var1 / var2 + 0.5f).toInt()
    }

    fun dp2px(var0: Context, var1: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, var1, var0.resources.displayMetrics).toInt()

    }

    fun sp2px(var0: Context, var1: Float): Int {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, var1, var0.resources.displayMetrics).toInt()
    }
}
