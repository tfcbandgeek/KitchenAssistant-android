package jgappsandgames.me.save.meals

import jgappsandgames.me.save.utility.getApplicationFilepath
import jgappsandgames.me.save.utility.loadJSON
import jgappsandgames.me.save.utility.saveJSON
import org.json.JSONArray
import org.json.JSONObject
import java.io.File

/**
 * MealManager
 * Created by Joshua Garner on 4/14/2018.
 */

// Constants ---------------------------------------------------------------------------------------
const val FILENAME = "meal.manager"
private const val TAGS = "tags"
private const val DAYS = "days"
private const val MEALS = "meals"

// Data --------------------------------------------------------------------------------------------
var tags = ArrayList<String>()
var days = ArrayList<String>()
var meals = ArrayList<String>()

// Management Methods ------------------------------------------------------------------------------
fun createMealManager() {
    tags = ArrayList()
    days = ArrayList()
    meals = ArrayList()
}

fun loadMealManager() {
    val data = loadJSON(File(getApplicationFilepath(), FILENAME))

    val t = data.optJSONArray(TAGS)
    tags = ArrayList()
    for (i in 0 until t.length()) tags.add(t.optString(i, ""))

    val d = data.optJSONArray(DAYS)
    days = ArrayList()
    for (i in 0 until d.length()) days.add(d.optString(i, ""))

    val m = data.optJSONArray(MEALS)
    meals = ArrayList()
    for (i in 0 until m.length()) meals.add(m.optString(i))
}

fun saveMealManager() {
    val data = JSONObject()

    val t = JSONArray()
    for (s in tags) t.put(s)
    data.put(TAGS, t)

    val d = JSONArray()
    for (s in days) d.put(s)
    data.put(DAYS, d)

    val m = JSONArray()
    for (s in meals) m.put(s)
    data.put(MEALS, m)

    saveJSON(File(getApplicationFilepath(), FILENAME), data)
}