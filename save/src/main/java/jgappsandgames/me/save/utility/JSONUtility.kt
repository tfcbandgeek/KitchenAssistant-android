package jgappsandgames.me.save.utility

// Java
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.io.File
import java.io.IOException
import java.util.Calendar

// JSON
import org.json.JSONException
import org.json.JSONObject


/**
 * JSONUtility
 * Version 1.0.0 (2/27/18)
 *
 * Created by Joshua Garner on 2/26/2018.
 */
// Constants ---------------------------------------------------------------------------------------
private const val ACTIVE = "active"
private const val DATE = "date"

// Standard JSON -----------------------------------------------------------------------------------
fun loadJSON(file: File): JSONObject {
    try {
        val reader = BufferedReader(FileReader(file))
        val builder = StringBuilder()

        while (true) {
            val t = reader.readLine()

            if (t == null) break
            else builder.append(t).append(System.lineSeparator())
        }

        return JSONObject(builder.toString())
    } catch (e: NullPointerException) {
        e.printStackTrace()
    } catch (e: JSONException) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return JSONObject()
}

fun saveJSON(file: File, data: JSONObject) {
    try {
        val writer = BufferedWriter(FileWriter(file))

        writer.write(data.toString())
        writer.flush()
        writer.close()
    } catch (e: IOException) {
        e.printStackTrace()
    } catch (e: NullPointerException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

// JSONCalendar ------------------------------------------------------------------------------------
fun loadCalendar(data: JSONObject): Calendar? {
    if (data.optBoolean(ACTIVE, false)) {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = data.optLong(DATE, 0)
        return calendar
    }

    return null
}

fun saveCalendar(calendar: Calendar?): JSONObject {
    try {
        val data = JSONObject()
        if (calendar == null) {
            data.put(ACTIVE, false)
        } else {
            data.put(ACTIVE, true)
            data.put(DATE, calendar.timeInMillis)
        }

        return data
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return JSONObject()
}