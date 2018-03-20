package jgappsandgames.me.save.shoppinglist

// Java
import java.io.File

// Android
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point

// Zxing
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

// JSON
import org.json.JSONArray
import org.json.JSONObject

// Save
import jgappsandgames.me.save.ingredient.Ingredient
import jgappsandgames.me.save.ingredient.Quanity
import jgappsandgames.me.save.utility.TESTING_B
import jgappsandgames.me.save.utility.TESTING_D
import jgappsandgames.me.save.utility.getApplicationFilepath
import jgappsandgames.me.save.utility.saveJSON

/**
 * Shopping List
 * Created by Joshua Garner on 3/14/2018.
 */
class ShoppingList(_filename: String?, _version: Int?, _meta: JSONObject?, _name: String?, _note: String?, _list: ArrayList<InternalIngredient>?, _list_j: JSONArray?) {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private const val FILENAME = "a"
        private const val VERSION = "version"
        private const val META = "b"

        private const val NAME = "c"
        private const val NOTES = "d"
        private const val LIST = "e"

        // Static Methods --------------------------------------------------------------------------
        fun toList(array: JSONArray?): ArrayList<InternalIngredient>? {
            if (array == null) return null
            val r = ArrayList<InternalIngredient>()

            for (i in 0 until array.length()) r.add(InternalIngredient(array.optJSONObject(i)))

            return r
        }

        fun fromList(array: ArrayList<InternalIngredient>?): JSONArray? {
            if (array == null) return null
            val r = JSONArray()

            for (l in array) r.put(l.toJSON())

            return r
        }
    }

    // Data ----------------------------------------------------------------------------------------
    private var filename: String = _filename ?: ""
    private var version: Int = _version ?: TESTING_D
    private var meta: JSONObject = _meta ?: JSONObject()

    private var name: String = _name ?: ""
    private var notes: String = _note ?: ""
    private var list: ArrayList<InternalIngredient> = _list ?: (toList(_list_j) ?: ArrayList())

    // Constructors --------------------------------------------------------------------------------
    constructor(data: JSONObject): this(data.optString(FILENAME, ""), data.optInt(VERSION, TESTING_B), data.optJSONObject(META), data.optString(NAME, ""), data.optString(NOTES, ""), null, data.optJSONArray(LIST))

    // Management Methods --------------------------------------------------------------------------
    fun save(): ShoppingList {
        saveJSON(File(getApplicationFilepath(), filename), toJSON())
        return this
    }

    // Getters -------------------------------------------------------------------------------------
    fun getFilename(): String {
        return filename
    }

    fun getMeta(): JSONObject {
        return meta
    }

    fun getName(): String {
        return name
    }

    fun getNotes(): String {
        return notes
    }

    fun getList(): ArrayList<InternalIngredient> {
        return list
    }

    // Setters -------------------------------------------------------------------------------------
    fun setFilename(_filename: String?): ShoppingList {
        filename = _filename ?: ""
        return this
    }

    fun getMeta(_meta: JSONObject?): ShoppingList {
        meta = _meta ?: JSONObject()
        return this
    }

    fun setName(_name: String?): ShoppingList {
        name = _name ?: ""
        return this
    }

    fun setNotes(_note: String?): ShoppingList {
        notes = _note ?: ""
        return this
    }

    fun setList(_list: ArrayList<InternalIngredient>?): ShoppingList {
        list = _list ?: ArrayList()
        return this
    }

    // To Methods ----------------------------------------------------------------------------------
    override fun toString(): String {
        return toJSON().toString()
    }

    fun toJSON(): JSONObject {
        val data = JSONObject()

        data.put(FILENAME, filename)
        data.put(VERSION, version)
        data.put(META, meta)

        data.put(NAME, name)
        data.put(NOTES, notes)
        data.put(LIST, fromList(list))

        return data
    }

    fun toQRCode(): Bitmap {
        val writer = QRCodeWriter()
        try {
            val matrix: BitMatrix = writer.encode(toJSON().put("TYPE", 2).toString(), BarcodeFormat.QR_CODE, 512, 512)
            val size = Point(matrix.width, matrix.height)
            val bitmap = Bitmap.createBitmap(size.x, size.y, Bitmap.Config.RGB_565)

            for (x in 0 until size.x) {
                for (y in 0 until size.y) {
                    bitmap.setPixel(x, y, if (matrix.get(x, y)) Color.BLACK else Color.WHITE)
                }
            }

            return bitmap
        } catch (e: WriterException) {
            return Bitmap.createBitmap(0,0, Bitmap.Config.RGB_565)
        }
    }

    // Internal Classes ----------------------------------------------------------------------------
    class InternalIngredient(var ingredient: Ingredient, var amount: Quanity, var notes: String, var status: Boolean) {
        companion object {
            private const val INGREDIENT = "a"
            private const val AMOUNT = "b"
            private const val NOTES = "c"
            private const val STATUS = "d"
        }

        constructor(data: JSONObject): this(Ingredient(data.optJSONObject(INGREDIENT)), Quanity(data.optJSONObject(AMOUNT)), data.optString(NOTES, ""), data.optBoolean(STATUS, false))

        fun toJSON(): JSONObject {
            val data = JSONObject()

            data.put(INGREDIENT, ingredient.toJSON())
            data.put(AMOUNT, amount.toJSON())
            data.put(NOTES, notes)
            data.put(STATUS, status)

            return data
        }
    }
}