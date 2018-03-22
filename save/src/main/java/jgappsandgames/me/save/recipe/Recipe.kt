package jgappsandgames.me.save.recipe

// Java
import java.io.File
import java.util.ArrayList
import java.util.Calendar

// Android
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
import jgappsandgames.me.save.ingredient.Ingredient
import jgappsandgames.me.save.ingredient.Quantity
import jgappsandgames.me.save.utility.TESTING_A
import jgappsandgames.me.save.utility.TESTING_D
import jgappsandgames.me.save.utility.getApplicationFilepath
import jgappsandgames.me.save.utility.saveJSON

/**
 * Recipe
 * Created by Joshua Garner on 3/1/2018.
 */
class Recipe(_filename: String?, _version: Int?, _meta: JSONObject?, _title: String?, _note: String?, _tags: ArrayList<String>?, _tags_j: JSONArray?, _ingredients: ArrayList<InternalIngredient>?, _ingredients_j: JSONArray?, _steps: ArrayList<Step>?, _steps_j: JSONArray?) {
    companion object {
        // Constants -------------------------------------------------------------------------------
        private const val FILENAME = "j"
        private const val VERSION = "version"
        private const val META = "a"
        private const val TITLE = "b"
        private const val NOTE = "c"
        private const val TAGS = "j"
        private const val INGREDIENTS = "d"
        private const val STEPS = "e"
        private const val INGREDIENT = "f"
        private const val AMOUNT = "g"
        private const val POSITION = "h"
        private const val TEXT = "i"

        // Static Methods --------------------------------------------------------------------------
        private fun toTags(json: JSONArray?): ArrayList<String> {
            if (json == null) return ArrayList()
            val data = ArrayList<String>()
            for (i in 0 until json.length()) data.add(json.optString(i, ""))
            return data
        }

        private fun fromTags(tags: ArrayList<String>?): JSONArray {
            if (tags == null) return JSONArray()
            val data = JSONArray()
            for (tag in tags) data.put(tag)
            return data
        }

        private fun toIngredients(json: JSONArray?): ArrayList<InternalIngredient> {
            val r = ArrayList<InternalIngredient>()

            if (json != null) {
                for (i in 0 until json.length()) {
                    val t = json.getJSONObject(i)
                    r.add(InternalIngredient(Ingredient(t.optJSONObject(INGREDIENT)), Quantity(t.optJSONObject(AMOUNT))))
                }
            }

            return r
        }

        private fun fromIngredients(ingredients: ArrayList<InternalIngredient>?): JSONArray {
            val r = JSONArray()

            if (ingredients != null) {
                for (ingredient in ingredients) {
                    r.put(JSONObject().put(INGREDIENT, ingredient.ingredient.toJSON()).put(AMOUNT, ingredient.amount.toJSON()))
                }
            }

            return r
        }

        private fun toSteps(json: JSONArray?): ArrayList<Step> {
            val r = ArrayList<Step>()

            if (json != null) {
                for (i in 0 until json.length()) {
                    val t = json.optJSONObject(i)
                    r.add(Step(t.optInt(POSITION, 0), t.optString(TEXT)))
                }
            }

            return r
        }

        private fun fromSteps(steps: ArrayList<Step>?): JSONArray {
            val r = JSONArray()

            if (steps != null) {
                for (step in steps) {
                    r.put(JSONObject().put(POSITION, step.position).put(TEXT, step.text))
                }
            }

            return r
        }
    }

    // Data ----------------------------------------------------------------------------------------
    private var filename: String = _filename ?: (_title ?: (Calendar.getInstance().timeInMillis.toString() + ".ka"))
    private var version: Int = _version ?: TESTING_A
    private var meta: JSONObject = _meta ?: JSONObject()
    private var title: String = _title ?: ""
    private var note: String = _note ?: ""
    private var tags: ArrayList<String> = _tags ?: toTags(_ingredients_j)
    private var ingredients: ArrayList<InternalIngredient> = _ingredients ?: toIngredients(_ingredients_j)
    private var steps: ArrayList<Step> = _steps ?: toSteps(_steps_j)

    // Constructors --------------------------------------------------------------------------------
    constructor(_filename: String, _title: String):
            this(_filename, TESTING_D, null, _title, null, null, null, null, null, null, null)
    constructor(data: JSONObject):
            this(data.optString(FILENAME, ""), data.optInt(VERSION, TESTING_A), data.optJSONObject(META), data.optString(TITLE, ""), data.optString(NOTE, ""), null, data.optJSONArray(TAGS), null, data.optJSONArray(INGREDIENTS), null, data.optJSONArray(STEPS))

    // Management Methods --------------------------------------------------------------------------
    fun save(): Recipe {
        saveJSON(File(getApplicationFilepath(), filename), toJSON())
        return this
    }

    fun copyTo(filename: String): Recipe {
        val n = Recipe(filename, version, meta, title, note, tags, null, ingredients, null, steps, null)
        n.save()
        return n
    }

    // Getters -------------------------------------------------------------------------------------
    fun getFilename(): String {
        return filename
    }

    fun getMeta(): JSONObject {
        return meta
    }

    fun getTitle(): String {
        return title
    }

    fun getNote(): String {
        return note
    }

    fun getTags(): ArrayList<String> {
        return tags
    }

    fun getIngredients(): ArrayList<InternalIngredient> {
        return ingredients
    }

    fun getSteps(): ArrayList<Step> {
        return steps
    }

    // Setters -------------------------------------------------------------------------------------
    fun setTitle(_title: String): Recipe {
        title = _title
        return this
    }

    fun setNote(_note: String): Recipe {
        note = _note
        return this
    }

    fun setTags(_tags: ArrayList<String>): Recipe {
        tags = _tags
        return this
    }

    fun setIngredients(_ingredients: ArrayList<InternalIngredient>): Recipe {
        ingredients = _ingredients
        return this
    }

    fun setSteps(_steps: ArrayList<Step>): Recipe {
        steps = _steps
        return this
    }

    // To Methods ----------------------------------------------------------------------------------
    fun toJSON(): JSONObject {
        val data = JSONObject()

        data.put(FILENAME, filename)
                .put(VERSION, TESTING_D)
                .put(META, meta)
                .put(TITLE, title)
                .put(NOTE, note)
                .put(TAGS, fromTags(tags))
                .put(INGREDIENTS, fromIngredients(ingredients))
                .put(STEPS, fromSteps(steps))

        return data
    }

    override fun toString(): String {
        return toJSON().toString()
    }

    fun toQRCode(): Bitmap {
        val writer = QRCodeWriter()
        return try {
            val matrix: BitMatrix = writer.encode(toJSON().put("TYPE", 1).toString(), BarcodeFormat.QR_CODE, 512, 512)
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

    // Internal Classes ----------------------------------------------------------------------------
    class InternalIngredient(var ingredient: Ingredient, var amount: Quantity)
    class Step(var position: Int, var text: String)
}