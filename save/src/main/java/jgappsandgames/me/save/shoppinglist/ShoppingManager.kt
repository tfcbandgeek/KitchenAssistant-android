package jgappsandgames.me.save.shoppinglist

// Java
import java.io.File
import java.util.Calendar

// JSON
import org.json.JSONArray
import org.json.JSONObject

// Save
import jgappsandgames.me.save.utility.TESTING_B
import jgappsandgames.me.save.utility.TESTING_D
import jgappsandgames.me.save.utility.getApplicationFilepath
import jgappsandgames.me.save.utility.loadJSON
import jgappsandgames.me.save.utility.saveJSON

/**
 * ShoppingManager
 * Created by Joshua Garner on 3/14/2018.
 */

// Constants ---------------------------------------------------------------------------------------
const val FILENAME = "shoppinglist.manager"

const val VERSION = "version"
const val META = "a"
const val SHOPPING_LIST = "b"

// Static Methods ----------------------------------------------------------------------------------
fun toList(array: JSONArray): ArrayList<String> {
    val r = ArrayList<String>()
    for (i in 0 until array.length()) r.add(array.optString(i, ""))
    return r
}

fun fromList(array: ArrayList<String>): JSONArray {
    val r = JSONArray()
    for (int in array) r.put(int)
    return r
}

// Data --------------------------------------------------------------------------------------------
var version: Int = TESTING_D
var meta: JSONObject = JSONObject()
var shopping_lists: ArrayList<String> = ArrayList()

// Management Methods ------------------------------------------------------------------------------
fun createShopping() {
    version = TESTING_D
    meta = JSONObject()
    shopping_lists = ArrayList()
}

fun loadShopping() {
    val file = File(getApplicationFilepath(), FILENAME)

    if (!file.exists()) {
        createShopping()
        return
    }

    val data = loadJSON(file)

    version = data.optInt(VERSION, TESTING_B)
    meta = data.optJSONObject(META)
    shopping_lists = toList(data.optJSONArray(SHOPPING_LIST))
}

fun saveShopping() {
    val data = JSONObject()

    data.put(VERSION, TESTING_D)
    data.put(META, meta)
    data.put(SHOPPING_LIST, fromList(shopping_lists))

    saveJSON(File(getApplicationFilepath(), FILENAME), data)
}

// Shopping List Methods ---------------------------------------------------------------------------
fun createShoppingList(): ShoppingList {
    val sl = ShoppingList(Calendar.getInstance().timeInMillis.toString() + ".kaj", null, null, null, null, null, null).save()
    shopping_lists.add(sl.getFilename())
    saveShopping()

    return sl
}

fun createShoppingList(title: String): ShoppingList {
    val sl = ShoppingList(title + Calendar.getInstance().timeInMillis.toString() + ".kaj", null, null, null, null, null, null).save()
    shopping_lists.add(sl.getFilename())
    saveShopping()

    return sl
}