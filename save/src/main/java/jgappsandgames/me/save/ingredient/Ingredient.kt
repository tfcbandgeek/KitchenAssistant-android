package jgappsandgames.me.save.ingredient

// JSON
import org.json.JSONObject

// Save
import jgappsandgames.me.save.utility.TESTING_A
import jgappsandgames.me.save.utility.TESTING_D

/**
 * Ingredient
 * Created by Joshua Garner on 3/1/2018.
 */
class Ingredient(_version: Int?, _meta: JSONObject?, _category: String?, _item: String?, defaultMeasure: Quanity.Unit?) {
    // Constants -----------------------------------------------------------------------------------
    companion object {
        private const val VERSION = "version"
        private const val META = "a"

        private const val CATEGORY = "b"
        private const val ITEM = "c"
        private const val DEFAULT_MEASURE = "d"
    }

    // Data ----------------------------------------------------------------------------------------
    private var version: Int = TESTING_D
    private var meta: JSONObject = JSONObject()

    private var category: String = _category ?: "NONE"
    private var item: String = _item ?: ""
    private var default_measure: Quanity.Unit? = defaultMeasure

    // Constructors --------------------------------------------------------------------------------
    constructor(): this(null, null, null, null, null)
    constructor(data: JSONObject): this(data.optInt(VERSION, TESTING_A), data.optJSONObject(META), data.optString(CATEGORY), data.optString(ITEM, "Error"), Quanity.Unit.fromInt(data.optInt(DEFAULT_MEASURE, 0)))

    // Getters -------------------------------------------------------------------------------------
    fun getMeta(): JSONObject {
        return meta
    }

    fun getCategory(): String {
        return category
    }

    fun getCategoryText(): String {
        return category
    }

    fun getItem(): String {
        return item
    }

    fun getMeasure(): Quanity.Unit? {
        return default_measure
    }

    // Setters -------------------------------------------------------------------------------------
    fun setCategory(_category: String): Ingredient {
        category = _category
        return this
    }

    fun setItem(_item: String): Ingredient {
        item = _item
        return this
    }

    fun setDefaultMeasure(defaultMeasure: Quanity.Unit?): Ingredient {
        default_measure = defaultMeasure
        return this
    }

    // To Methods ----------------------------------------------------------------------------------
    override fun toString(): String {
        return toJSON().toString()
    }

    fun toJSON(): JSONObject {
        val data = JSONObject()

        data.put(VERSION, TESTING_D)
        data.put(META, meta)

        data.put(CATEGORY, category)
        data.put(ITEM, item)
        data.put(DEFAULT_MEASURE, Quanity.Unit.toInt(default_measure ?: Quanity.Unit.NONE))

        return data
    }
}