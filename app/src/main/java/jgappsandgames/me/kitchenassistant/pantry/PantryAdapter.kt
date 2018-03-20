package jgappsandgames.me.kitchenassistant.pantry

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import jgappsandgames.me.kitchenassistant.R
import jgappsandgames.me.save.ingredient.Ingredient
import jgappsandgames.me.save.pantry.getIngredientSelectorList
import jgappsandgames.me.save.pantry.ingredients
import jgappsandgames.me.save.pantry.pantry
import org.jetbrains.anko.alert
import org.jetbrains.anko.customView
import org.jetbrains.anko.editText
import org.jetbrains.anko.selector

/**
 * PantryAdapter
 * Created by Joshua Garner on 3/19/2018.
 */
class PantryAdapter(val context: Context, val listener: PantryListener): BaseAdapter() {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.list_pantry, parent, false)

        view.findViewById<TextView>(R.id.title_).text = getItem(position).getItem()
        view.findViewById<TextView>(R.id.category_).text =getItem(position).getCategoryText()

        view.findViewById<TextView>(R.id.title_).setOnClickListener {
            context.selector("Ingredient", getIngredientSelectorList(), { _, i ->
                changeIngredient(position, i)
            })
        }
        view.findViewById<TextView>(R.id.title_).setOnLongClickListener {
            context.alert {
                var s = ""
                title = "New Ingredient"

                customView {
                    editText {
                        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        hint = "New Ingredient"
                        addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(s: Editable?) {}
                            override fun beforeTextChanged(c: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(c: CharSequence?, start: Int, before: Int, count: Int) {
                                s = this@editText.text.toString()
                            }
                        })
                    }
                }

                positiveButton("Okay", {
                    changeIngredient(position, s)

                    view.findViewById<TextView>(R.id.title_).text = getItem(position).getItem()
                    view.findViewById<TextView>(R.id.category_).text =getItem(position).getCategoryText()
                })

                negativeButton("Cancel", {})
            }.show()

            return@setOnLongClickListener true
        }
        view.findViewById<TextView>(R.id.category_).setOnClickListener {
            context.selector("Ingredient", getIngredientSelectorList(), { _, i ->
                changeIngredient(position, i)

                view.findViewById<TextView>(R.id.title_).text = getItem(position).getItem()
                view.findViewById<TextView>(R.id.category_).text =getItem(position).getCategoryText()
            })
        }
        view.findViewById<TextView>(R.id.category_).setOnLongClickListener {
            context.alert {
                var s = ""
                title = "New Ingredient"

                customView {
                    editText {
                        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        hint = "New Ingredient"
                        addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(s: Editable?) {}
                            override fun beforeTextChanged(c: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(c: CharSequence?, start: Int, before: Int, count: Int) {
                                s = this@editText.text.toString()
                            }
                        })
                    }
                }

                positiveButton("Okay", {
                    changeIngredient(position, s)

                    view.findViewById<TextView>(R.id.title_).text = getItem(position).getItem()
                    view.findViewById<TextView>(R.id.category_).text =getItem(position).getCategoryText()
                })

                negativeButton("Cancel", {})
            }.show()

            return@setOnLongClickListener true
        }
        view.findViewById<Button>(R.id.remove_).setOnClickListener {
            pantry.removeAt(position)
            listener.onChange()
        }

        return view
    }

    override fun getItem(position: Int): Ingredient {
        return pantry[position]
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return pantry.size
    }

    // Private Methods -----------------------------------------------------------------------------
    private fun changeIngredient(position: Int, n_position: Int) {
        pantry[position] = ingredients[n_position]
    }

    private fun changeIngredient(position: Int, text: String) {
        val a = Ingredient(null, null, null, text, null)
        pantry[position] = a
        ingredients.add(a)
    }

    // Internal Classes ----------------------------------------------------------------------------
    interface PantryListener {
        fun onChange()
    }
}