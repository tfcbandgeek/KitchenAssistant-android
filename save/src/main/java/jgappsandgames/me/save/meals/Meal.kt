package jgappsandgames.me.save.meals

// Java
import java.io.File
import java.util.Calendar

// Android OS
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Point

// JSON
import org.json.JSONArray
import org.json.JSONObject

// Zxing
import com.google.zxing.BarcodeFormat
import com.google.zxing.WriterException
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter

// Save
import jgappsandgames.me.save.recipe.Recipe
import jgappsandgames.me.save.utility.getApplicationFilepath
import jgappsandgames.me.save.utility.loadJSON
import jgappsandgames.me.save.utility.saveJSON

/**
 * Meal
 * Created by Joshua Garner on 4/12/2018.
 */
class Meal(_filename: String? = null, _title: String? = null) {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private const val TITLE = "title"
        private const val NOTES = "notes"
        private const val TAGS = "tags"
        private const val RECIPES = "recipes"
    }

    // Data ----------------------------------------------------------------------------------------
    private var filename: String

    private var title: String
    private var notes: String
    private var tags: ArrayList<String>

    private var recipes: ArrayList<Recipe>

    // Constructors --------------------------------------------------------------------------------
    init {
        when {
            _filename != null -> {
                val data = loadJSON(File(getApplicationFilepath(), _filename))

                filename = _filename
                title = data.optString(TITLE, "")
                notes = data.optString(NOTES, "")

                val t = data.optJSONArray(TAGS)
                tags = ArrayList()
                for (i in 0 until t.length()) tags.add(t.optString(i, ""))

                val r = data.optJSONArray(RECIPES)
                recipes = ArrayList()
                for (i in 0 until r.length()) recipes.add(Recipe(loadJSON(File(getApplicationFilepath(), r.optString(i, "")))))
            }
            _title != null -> {
                filename = Calendar.getInstance().timeInMillis.toString() + _title + ".ka"
                title = _title
                notes = ""
                tags = ArrayList()
                recipes = ArrayList()
            }
            else -> {
                filename = Calendar.getInstance().timeInMillis.toString() + ".ka"
                title = ""
                notes = ""
                tags = ArrayList()
                recipes = ArrayList()
            }
        }
    }

     // Management Methods -------------------------------------------------------------------------
    fun save(): Meal {
         saveJSON(File(getApplicationFilepath(), filename), toJSON())
         return this
     }

    // Getters -------------------------------------------------------------------------------------
    fun getFilename(): String {
        return filename
    }

    fun getTitle(): String {
        return title
    }

    fun getNotes(): String {
        return notes
    }

    fun getTags(): ArrayList<String> {
        return tags
    }

    fun getTagsString(): String {
        val b = StringBuilder()
        for (s in tags) b.append(s)
        return b.toString()
    }

    fun getRecipes(): ArrayList<Recipe> {
        return recipes
    }

    // Setters -------------------------------------------------------------------------------------
    fun setTitle(_title: String): Meal {
        title = _title
        return this
    }

    fun setNotes(_notes: String): Meal {
        notes = _notes
        return this
    }

    fun setTags(_tags: ArrayList<String>): Meal {
        tags = _tags
        return this
    }

    fun setRecipes(_recipes: ArrayList<Recipe>): Meal {
        recipes = _recipes
        return this
    }

    // To Methods ----------------------------------------------------------------------------------
    override fun toString(): String {
        return toHeavyJSON().toString(2)
    }

    fun toJSON(): JSONObject {
        val data = JSONObject()

        data.put(TITLE, title)
        data.put(NOTES, notes)

        val t = JSONArray()
        for (s in tags) t.put(s)
        data.put(TAGS, t)

        val r = JSONArray()
        for (s in recipes) r.put(s.getFilename())
        data.put(RECIPES, r)

        return data
    }

    fun toHeavyJSON(): JSONObject {
        val data = JSONObject()

        data.put(TITLE, title)
        data.put(NOTES, notes)

        val t = JSONArray()
        for (s in tags) t.put(s)
        data.put(TAGS, t)

        val r = JSONArray()
        for (s in recipes) r.put(s.toJSON())
        data.put(RECIPES, r)

        return data
    }

    fun toQR(): Bitmap {
        val writer = QRCodeWriter()
        return try {
            val matrix: BitMatrix = writer.encode(toHeavyJSON().put("TYPE", 3).toString(), BarcodeFormat.QR_CODE, 512, 512)
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