package jgappsandgames.me.kitchenassistant.qr

// Android
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle

// Anko
import org.jetbrains.anko.imageBitmap

// App
import jgappsandgames.me.kitchenassistant.R

// KotlinX
import kotlinx.android.synthetic.main.activity_qr.qr_image

/**
 * QRActivity
 * Created by Joshua Garner on 3/18/2018.
 */
class QRActivity: Activity() {
    companion object {
        var TITLE = "QR Code"
        var DATA: Bitmap? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        title = TITLE
        if (DATA != null) qr_image.imageBitmap = DATA
    }
}