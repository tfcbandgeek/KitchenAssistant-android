package jgappsandgames.me.save.pantry

// Java
import java.io.File

// JSON
import org.json.JSONArray
import org.json.JSONObject

// Save
import jgappsandgames.me.save.ingredient.Ingredient
import jgappsandgames.me.save.recipe.Recipe
import jgappsandgames.me.save.shoppinglist.ShoppingList
import jgappsandgames.me.save.utility.getApplicationFilepath
import jgappsandgames.me.save.utility.loadJSON
import jgappsandgames.me.save.utility.saveJSON
import jgappsandgames.me.save.utility.TESTING_A
import jgappsandgames.me.save.utility.TESTING_E

/**
 * PantryManager
 * Created by Joshua Garner on 3/12/2018.
 */
// Constants ---------------------------------------------------------------------------------------
const val FILENAME = "pantry.manager"

const val VERSION = "version"
const val META = "a"
const val INGREDIENTS = "b"
const val PANTRY = "c"
const val CATEGORIES = "d"

// Data --------------------------------------------------------------------------------------------
var version: Int = TESTING_E
var meta: JSONObject = JSONObject()

var ingredients: ArrayList<Ingredient> = ArrayList()
var pantry: ArrayList<Ingredient> = ArrayList()
var categories: ArrayList<String> = ArrayList()

// Management Methods ------------------------------------------------------------------------------
fun createPantry() {
    version = TESTING_E
    meta = JSONObject()
    ingredients = ArrayList()
    pantry = ArrayList()
    categories = ArrayList()
}

fun loadPantry() {
    val data = loadJSON(File(getApplicationFilepath(), FILENAME))

    version = data.optInt(VERSION, TESTING_A)
    meta = data.optJSONObject(META)
    ingredients = toIngredients(data.optJSONArray(INGREDIENTS))

    // Testing D
    if (version >= TESTING_E) {
        pantry = toIngredients(data.optJSONArray(PANTRY))

        try {
            val a = data.getJSONArray(CATEGORIES)

            categories = ArrayList()
            for (i in 0 until a.length()) categories.add(a.optString(i))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}

fun savePantry() {
    val data = JSONObject()

    data.put(VERSION, TESTING_E)
    data.put(META, meta)
    data.put(INGREDIENTS, fromIngredients(ingredients))

    // Testing D
    data.put(PANTRY, fromIngredients(pantry))
    val a = JSONArray()
    for (s in categories) a.put(s)
    data.put(CATEGORIES, a)

    saveJSON(File(getApplicationFilepath(), FILENAME), data)
}

// Private Methods ---------------------------------------------------------------------------------
private fun toIngredients(json: JSONArray): ArrayList<Ingredient> {
    val r = ArrayList<Ingredient>()
    if (json.length() != 0) for (i in 0 until json.length()) r.add(Ingredient(json.optJSONObject(i)))
    return r
}

private fun fromIngredients(array: ArrayList<Ingredient>): JSONArray {
    val r = JSONArray()
    if (array.size != 0) for (ingredient in array) r.put(ingredient.toJSON())
    return r
}

// Ingredient Messages -----------------------------------------------------------------------------
fun getIngredientSelectorList(): List<String> {
    val r = ArrayList<String>()
    for (ingredient in ingredients) r.add(ingredient.getItem())
    return r
}

fun importIngredients(recipe: Recipe) {
    for (ingredient in recipe.getIngredients()) {
        var t = false
        for (i in ingredients) if (ingredient.ingredient.getItem() == i.getItem()) t = true
        if (!t) {
            ingredients.add(ingredient.ingredient)
            if (!categories.contains(ingredient.ingredient.getCategory())) categories.add(ingredient.ingredient.getCategory())
        }
    }

    savePantry()
}

fun importIngredients(shoppingList: ShoppingList) {
    for (ingredient in shoppingList.getList()) {
        var t = false
        for (i in ingredients) if (ingredient.ingredient.getItem() == i.getItem()) t = true
        if (!t) {
            ingredients.add(ingredient.ingredient)
            if (!categories.contains(ingredient.ingredient.getCategory())) categories.add(ingredient.ingredient.getCategory())
        }
    }

    savePantry()
}