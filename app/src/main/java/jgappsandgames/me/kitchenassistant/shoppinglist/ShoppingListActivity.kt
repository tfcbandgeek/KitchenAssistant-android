package jgappsandgames.me.kitchenassistant.shoppinglist

// Java
import java.io.File

// Android
import android.app.Activity
import android.content.Intent
import android.os.Bundle

// Views
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView

// JSON
import org.json.JSONObject

// App
import jgappsandgames.me.kitchenassistant.R
import jgappsandgames.me.kitchenassistant.ingredients.SIngredientActivity
import jgappsandgames.me.kitchenassistant.qr.QRActivity

// KotlinX
import kotlinx.android.synthetic.main.activity_shopping_list.add_item
import kotlinx.android.synthetic.main.activity_shopping_list.list
import kotlinx.android.synthetic.main.activity_shopping_list.notes
import kotlinx.android.synthetic.main.activity_shopping_list.title_

// Save
import jgappsandgames.me.save.ingredient.Ingredient
import jgappsandgames.me.save.ingredient.Quantity
import jgappsandgames.me.save.shoppinglist.ShoppingList
import jgappsandgames.me.save.utility.getApplicationFilepath
import jgappsandgames.me.save.utility.loadJSON

/**
 * ShoppingListActivity
 * Created by Joshua Garner on 3/14/2018.
 */
class ShoppingListActivity: Activity() {
    companion object {
        // Constants -------------------------------------------------------------------------------
        const val LIST = "list"
    }

    // Data ----------------------------------------------------------------------------------------
    var shopping_list: ShoppingList? = null
    var l: Int = 0

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_shopping_list)
        shopping_list = ShoppingList(loadJSON(File(getApplicationFilepath(), intent.getStringExtra(LIST))))

        title_.setText(shopping_list!!.getName())
        notes.setText(shopping_list!!.getNotes())

        title_.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                shopping_list!!.setName(title_.text.toString()).save()
            }
        })

        notes.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                shopping_list!!.setNotes(notes.text.toString()).save()
            }
        })

        add_item.setOnClickListener {
            startActivityForResult(Intent(this, SIngredientActivity::class.java), 1)
        }
    }

    override fun onResume() {
        super.onResume()

        list.adapter = ItemAdapter(this, shopping_list!!)
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> {
                if (resultCode == 0) {
                    shopping_list!!.getList().add(ShoppingList.InternalIngredient(
                            Ingredient(JSONObject(data!!.getStringExtra(SIngredientActivity.INGREDIENT))),
                            Quantity(JSONObject(data.getStringExtra(SIngredientActivity.QUANITY))),
                            data.getStringExtra(SIngredientActivity.NOTES),
                            data.getBooleanExtra(SIngredientActivity.HAVE, false)))

                    shopping_list!!.save()
                    onResume()
                }
            }

            2 -> {
                if (resultCode == 0) {
                    shopping_list!!.getList()[l] = ShoppingList.InternalIngredient(
                            Ingredient(JSONObject(data!!.getStringExtra(SIngredientActivity.INGREDIENT))),
                            Quantity(JSONObject(data.getStringExtra(SIngredientActivity.QUANITY))),
                            data.getStringExtra(SIngredientActivity.NOTES),
                            data.getBooleanExtra(SIngredientActivity.HAVE, false))

                    shopping_list!!.save()
                    onResume()
                }
            }
        }
    }

    // Menu Methods --------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.shopping_list_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.to_qr -> {
                QRActivity.TITLE = shopping_list!!.getName()
                QRActivity.DATA = shopping_list!!.toQRCode()
                startActivity(Intent(this, QRActivity::class.java))
                return true
            }
        }

        return false
    }

    // Internal Classes ----------------------------------------------------------------------------
    private class ItemAdapter(val context: ShoppingListActivity, val shoppingList: ShoppingList): BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = LayoutInflater.from(context).inflate(R.layout.list_shopping_item, parent, false)
            val l = getItem(position)

            view.findViewById<TextView>(R.id.title).text = l.ingredient.getItem()
            view.findViewById<TextView>(R.id.categories).text = l.ingredient.getCategoryText()
            view.findViewById<TextView>(R.id.notes).text = l.notes
            view.findViewById<CheckBox>(R.id.status_).isChecked = l.status

            view.findViewById<Button>(R.id.edit_button).setOnClickListener {
                context.l = position
                context.startActivityForResult(
                        Intent(context, SIngredientActivity::class.java)
                                .putExtra(SIngredientActivity.INGREDIENT, l.ingredient.toJSON().toString())
                                .putExtra(SIngredientActivity.QUANITY, l.amount.toJSON().toString())
                                .putExtra(SIngredientActivity.NOTES, l.notes)
                                .putExtra(SIngredientActivity.HAVE, l.status), 2)
            }

            view.findViewById<CheckBox>(R.id.status_).setOnCheckedChangeListener { _, isChecked -> l.status = isChecked }

            return view
        }

        override fun getItem(position: Int): ShoppingList.InternalIngredient {
            return shoppingList.getList()[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return shoppingList.getList().size
        }

    }
}