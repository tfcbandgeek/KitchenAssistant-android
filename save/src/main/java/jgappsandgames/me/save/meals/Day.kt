package jgappsandgames.me.save.meals

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import jgappsandgames.me.save.utility.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.Calendar

/**
 * Day
 * Created by Joshua Garner on 4/14/2018.
 */
class Day(_filename: String? = null, _calendar: Calendar? = null) {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private const val DAY = "day"
        private const val MEALS = "meals"
    }

    // Data ----------------------------------------------------------------------------------------
    private var filename: String
    private var calendar: Calendar
    private var meals: ArrayList<Meal>

    // Construcotrs --------------------------------------------------------------------------------
    init {
        if (_filename != null) {
            filename = _filename

            val data = loadJSON(File(getApplicationFilepath(), _filename))
            calendar = loadCalendar(data.optJSONObject(DAY)) ?: Calendar.getInstance()

            val m = data.optJSONArray(MEALS)
            meals = ArrayList()
            for (i in 0 until m.length()) meals.add(Meal(_filename = m.optString(i, "")))
        }

        else if (_calendar != null) {
            filename = Calendar.getInstance().timeInMillis.toString() + ".ka"
            calendar = _calendar
            meals = ArrayList()
        }

        else {
            filename = Calendar.getInstance().timeInMillis.toString() + ".ka"
            calendar = Calendar.getInstance()
            meals = ArrayList()
        }
    }

    // Management Methods --------------------------------------------------------------------------
    fun save(): Day {
        saveJSON(File(getApplicationFilepath(), filename), toJSON())
        return this
    }

    // Getters -------------------------------------------------------------------------------------
    fun getDay(): Calendar {
        return calendar
    }

    fun getMeals(): ArrayList<Meal> {
        return meals
    }

    // Setters -------------------------------------------------------------------------------------
    fun setDay(_calendar: Calendar): Day {
        calendar = _calendar
        return this
    }

    fun setMeals(_meals: ArrayList<Meal>): Day {
        meals = _meals
        return this
    }

    // To Methods ----------------------------------------------------------------------------------
    override fun toString(): String {
        return toHeavyJSON().toString(2)
    }

    fun toJSON(): JSONObject {
        val data = JSONObject()

        data.put(DAY, saveCalendar(calendar))

        val m = JSONArray()
        for (s in meals) m.put(s.getFilename())
        data.put(MEALS, m)

        return data
    }

    fun toHeavyJSON(): JSONObject {
        val data = JSONObject()

        data.put(DAY, saveCalendar(calendar))

        val m = JSONArray()
        for (s in meals) m.put(s.toHeavyJSON())
        data.put(MEALS, m)

        return data
    }

    fun toQR(): Bitmap {
        val writer = QRCodeWriter()
        return try {
            val matrix: BitMatrix = writer.encode(toHeavyJSON().put("TYPE", 3).toString(), BarcodeFormat.QR_CODE, 1024, 1024)
            val size = Point(matrix.width, matrix.height)
            val bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.RGB_565)

            for (x in 0 until size.x) {
                for (y in 0 until size.y) {
                    bitmap.setPixel(x, y, if (matrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }

            bitmap
        } catch (e: WriterException) {
            Bitmap.createBitmap(0,0, Bitmap.Config.RGB_565)
        }
    }
}