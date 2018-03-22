package jgappsandgames.me.kitchenassistant.recipes

// Java
import java.io.File

// Android
import android.content.Context
import android.content.Intent

// Views
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView

// App
import jgappsandgames.me.kitchenassistant.R

// Save
import jgappsandgames.me.save.recipe.Recipe
import jgappsandgames.me.save.recipe.recipes
import jgappsandgames.me.save.recipe.saveRecipes
import jgappsandgames.me.save.utility.getApplicationFilepath
import jgappsandgames.me.save.utility.loadJSON

/**
 * RecipeAdapter
 * Created by Joshua Garner on 3/12/2018.
 */
class RecipeAdapter(private val context: Context, private val listener: RecipeListener): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val r = getItem(position)
        val view = LayoutInflater.from(context).inflate(R.layout.list_recipe, parent, false)

        view.findViewById<TextView>(R.id.name).text = r.getTitle()
        view.findViewById<TextView>(R.id.notes).text = r.getNote()

        view.findViewById<Button>(R.id.edit_button).setOnClickListener {
            context.startActivity(Intent(context, RecipeActivity::class.java).putExtra(RecipeActivity.RECIPE, r.getFilename()))
            listener.onChange()
        }

        view.findViewById<Button>(R.id.edit_button).setOnLongClickListener {
            recipes.removeAt(position)
            saveRecipes()
            listener.onChange()
            return@setOnLongClickListener true
        }

        return view
    }

    override fun getItem(position: Int): Recipe {
        return Recipe(loadJSON(File(getApplicationFilepath(), recipes[position])))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return recipes.size
    }

    // Listener ------------------------------------------------------------------------------------
    interface RecipeListener {
        fun onChange()
    }
}