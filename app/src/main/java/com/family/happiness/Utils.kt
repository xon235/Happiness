package com.family.happiness

import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.util.TypedValue
import com.google.zxing.BarcodeFormat
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix

object Utils {

    // https://stackoverflow.com/questions/28232116/android-using-zxing-generate-qr-code
    @Throws(WriterException::class)
    fun encodeAsBitmap(str: String?): Bitmap? {
        val displayMetrics = DisplayMetrics()
        val result: BitMatrix = try {
            MultiFormatWriter().encode(
                str,
                BarcodeFormat.QR_CODE, 960, 960, null
            )
        } catch (iae: IllegalArgumentException) {
            // Unsupported format
            return null
        }
        val w = result.width
        val h = result.height
        val pixels = IntArray(w * h)
        for (y in 0 until h) {
            val offset = y * w
            for (x in 0 until w) {
                pixels[offset + x] = (if (result[x, y]) 0xFF000000 else 0xFFFFFFFF).toInt()
            }
        }
        val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h)
        return bitmap
    }

    fun dpToPx(context: Context, dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics)
    }
}