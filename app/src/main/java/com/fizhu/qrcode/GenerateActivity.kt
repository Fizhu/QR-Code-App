package com.fizhu.qrcode

import android.graphics.Bitmap
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.fizhu.qrcode.databinding.ActivityGenerateBinding
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.MultiFormatWriter
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import java.util.*


class GenerateActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGenerateBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGenerateBinding.inflate(layoutInflater)
        setContentView(binding.root)
        onInit()
    }

    private fun onInit() {
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.btnGenerate.setOnClickListener { validation() }
    }

    private fun validation() {
        val text = binding.et.text.toString()
        if (text.isEmpty() || text == "") {
            Toast.makeText(this, "Text cannot be empty", Toast.LENGTH_SHORT).show()
        } else {
            setImage(encodeAsBitmap(text))
        }
    }

    private fun setImage(bitmap: Bitmap?) {
        Glide.with(this)
            .load(bitmap)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(binding.iv)
    }

    @Throws(WriterException::class)
    private fun encodeAsBitmap(contents: String?): Bitmap? {
        val contentsToEncode = contents ?: return null
        var hints: MutableMap<EncodeHintType?, Any?>? = null
        val encoding = guessAppropriateEncoding(contentsToEncode)
        if (encoding != null) {
            hints = EnumMap<EncodeHintType, Any>(EncodeHintType::class.java)
            hints[EncodeHintType.CHARACTER_SET] = encoding
        }
        val writer = MultiFormatWriter()
        val result: BitMatrix
        result = try {
            writer.encode(contentsToEncode, BarcodeFormat.QR_CODE, 400, 400, hints)
        } catch (iae: IllegalArgumentException) {
            // Unsupported format
            return null
        }
        val width = result.width
        val height = result.height
        val pixels = IntArray(width * height)
        for (y in 0 until height) {
            val offset = y * width
            for (x in 0 until width) {
                pixels[offset + x] = if (result[x, y]) BLACK else WHITE
            }
        }
        val bitmap = Bitmap.createBitmap(
            width, height,
            Bitmap.Config.ARGB_8888
        )
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    }

    private fun guessAppropriateEncoding(contents: CharSequence): String? {
        // Very crude at the moment
        for (element in contents) {
            if (element.toInt() > 0xFF) {
                return "UTF-8"
            }
        }
        return null
    }

    companion object {
        private const val WHITE = -0x1
        private const val BLACK = -0x1000000
    }
}