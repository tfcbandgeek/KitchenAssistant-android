package jgappsandgames.me.save.recipe

// Java
import java.io.File
import java.util.ArrayList
import java.util.Calendar

// JSON
import org.json.JSONArray
import org.json.JSONObject

// Save
import jgappsandgames.me.save.utility.TESTING_A
import jgappsandgames.me.save.utility.TESTING_D
import jgappsandgames.me.save.utility.getApplicationFilepath
import jgappsandgames.me.save.utility.loadJSON
import jgappsandgames.me.save.utility.saveJSON

/**
 * RecipeManager
 * Created by Joshua Garner on 3/1/2018.
 */
// Constants ---------------------------------------------------------------------------------------
const val FILENAME: String = "recipe.manager"

private const val VERSION: String = "version"
private const val META: String = "a"

private const val RECIPES: String = "b"

// Static Methods ----------------------------------------------------------------------------------
private fun toRecipes(array: JSONArray?): ArrayList<String> {
    val r = ArrayList<String>()
    if (array != null) for (i in 0 until array.length()) r.add(array.optString(i, ""))
    return r
}

private fun fromRecipe(recipes: ArrayList<String>?): JSONArray {
    val r = JSONArray()
    if (recipes != null) for (recipe in recipes) r.put(recipe)
    return r
}

// Data --------------------------------------------------------------------------------------------
var version: Int = TESTING_D
var meta: JSONObject = JSONObject()
var recipes: ArrayList<String> = ArrayList()

// Management Methods ------------------------------------------------------------------------------
fun createRecipes() {
    version = TESTING_D
    meta = JSONObject()
    recipes = ArrayList(5)
}

fun loadRecipes() {
    val data = loadJSON(File(getApplicationFilepath(), FILENAME))

    version = data.optInt(VERSION, TESTING_A)
    meta = data.optJSONObject(META)
    recipes = toRecipes(data.optJSONArray(RECIPES))
}

fun saveRecipes() {
    val data = JSONObject()
    data.put(VERSION, TESTING_D)
    data.put(META, meta)
    data.put(RECIPES, fromRecipe(recipes))
    saveJSON(File(getApplicationFilepath(), FILENAME), data)
}

// Recipe Methods ----------------------------------------------------------------------------------
fun createRecipe(title: String): Recipe {
    val r = Recipe(title + Calendar.getInstance().timeInMillis.toString() + ".kaj", title).save()
    recipes.add(r.getFilename())
    saveRecipes()
    return r
}