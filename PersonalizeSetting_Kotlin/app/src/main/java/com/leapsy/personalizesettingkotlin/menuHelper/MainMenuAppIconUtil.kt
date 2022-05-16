package com.leapsy.personalizesettingkotlin.menuHelper

import android.view.ViewGroup
import android.widget.RelativeLayout
import java.util.ArrayList
import kotlin.math.cos
import kotlin.math.sin

/**
 * 主菜單 甜甜圈menu util
 */
class MainMenuAppIconUtil {
    companion object {

        /**
         * 指定APP數量後，並取得各APP之PositionX與PositionY值
         * @param aBigCircleRadius 大圓之半徑
         * @param aNumberOfIcons APP的數量
         * @return
         */
        fun getLauncherIconCoordinates(aBigCircleRadius : Double, aNumberOfIcons : Int) : ArrayList<DoubleArray> {
            var arrayOfXYs : ArrayList<DoubleArray> = ArrayList<DoubleArray>()
            for (i in 0 until aNumberOfIcons) {
                val xy: DoubleArray = convertDegreesRToXY((360 * i / aNumberOfIcons).toDouble(), aBigCircleRadius)
                arrayOfXYs.add(xy)
            }
            return arrayOfXYs
        }

        private fun convertDegreesRToXY(aDegrees : Double, aRadius : Double) : DoubleArray {
            val tempDegrees = aDegrees - 90
            val angle = Math.PI * tempDegrees / 180
            val x = cos(angle) * aRadius
            val y = sin(angle) * aRadius
            return doubleArrayOf(x, y)
        }


        /**
         * 主菜單App Layout的參數
         * @param aWidth Icon width
         * @param aHeight Icon Height
         * @return
         */
        fun relativeLayoutParams(aWidth : Int, aHeight : Int) : ViewGroup.LayoutParams {
            val params = RelativeLayout.LayoutParams(aWidth, aHeight)
            params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE)
            params.addRule(RelativeLayout.CENTER_VERTICAL, RelativeLayout.TRUE)
            return params
        }
    }
}