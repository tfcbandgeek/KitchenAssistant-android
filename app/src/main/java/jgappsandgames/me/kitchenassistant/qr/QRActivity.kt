package jgappsandgames.me.kitchenassistant.qr

// Java
import java.io.File
import java.io.FileOutputStream

// Android
import android.app.Activity
import android.content.ClipData
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.content.FileProvider

// Anko
import org.jetbrains.anko.imageBitmap
import org.jetbrains.anko.clipboardManager

// App
import jgappsandgames.me.kitchenassistant.R
import jgappsandgames.me.save.utility.getCacheDirectory

// KotlinX
import kotlinx.android.synthetic.main.activity_qr.copy
import kotlinx.android.synthetic.main.activity_qr.save_to_gallery
import kotlinx.android.synthetic.main.activity_qr.qr_image


/**
 * QRActivity
 * Created by Joshua Garner on 3/18/2018.
 */
class QRActivity: Activity() {
    companion object {
        var TITLE = "QR Code"
        var DATA: Bitmap? = null
        var COPY: String? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)

        title = TITLE
        if (DATA != null) qr_image.imageBitmap = DATA

        copy.setOnClickListener {
            if (COPY != null) {
                clipboardManager.primaryClip = ClipData.newPlainText(TITLE, COPY)
            }
        }

        save_to_gallery.setOnClickListener {
            if (DATA != null) {
                val file = File(getCacheDirectory(), "image.png")
                if (file.exists()) file.delete()

                val stream = FileOutputStream(getCacheDirectory().absolutePath + "image.png")
                DATA?.compress(Bitmap.CompressFormat.PNG, 100, stream)
                stream.close()

                val uri = FileProvider.getUriForFile(this, "jgappsnadgames.me.kitchenassistant", File(getCacheDirectory(), "image.png"))

                val intent = Intent()
                        .setAction(Intent.ACTION_SEND)
                        .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        .setDataAndType(uri, contentResolver.getType(uri))
                        .putExtra(Intent.EXTRA_STREAM, uri)

                startActivity(Intent.createChooser(intent, "Choose an App"))
            }
        }
    }
}