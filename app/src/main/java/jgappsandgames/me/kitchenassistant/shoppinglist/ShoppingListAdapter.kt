package jgappsandgames.me.kitchenassistant.shoppinglist

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
import jgappsandgames.me.save.shoppinglist.ShoppingList
import jgappsandgames.me.save.shoppinglist.saveShopping
import jgappsandgames.me.save.shoppinglist.shopping_lists
import jgappsandgames.me.save.utility.getApplicationFilepath
import jgappsandgames.me.save.utility.loadJSON

/**
 * ShoppingListAdapter
 * Created by Joshua Garner on 3/14/2018.
 */
class ShoppingListAdapter(private val context: Context, private val listener: ShoppingListListener): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.list_shopping_list, parent, false)
        val sl = getItem(position)

        view.findViewById<TextView>(R.id.title).text = sl.getName()
        view.findViewById<TextView>(R.id.notes).text = sl.getNotes()

        view.findViewById<Button>(R.id.edit_button).setOnClickListener {
            context.startActivity(Intent(context, ShoppingListActivity::class.java).putExtra(ShoppingListActivity.LIST, sl.getFilename()))
            listener.onChange()
        }

        view.findViewById<Button>(R.id.edit_button).setOnLongClickListener {
            shopping_lists.removeAt(position)
            saveShopping()
            listener.onChange()
            return@setOnLongClickListener true
        }

        return view
    }

    override fun getItem(position: Int): ShoppingList {
        return ShoppingList(loadJSON(File(getApplicationFilepath(), shopping_lists[position])))
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return shopping_lists.size
    }

    // Interfaces ----------------------------------------------------------------------------------
    interface ShoppingListListener {
        fun onChange()
    }
}