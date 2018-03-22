package jgappsandgames.me.kitchenassistant.ingredients

// Android
import android.app.Activity
import android.content.Intent
import android.os.Bundle

// Anko
import org.jetbrains.anko.alert
import org.jetbrains.anko.customView
import org.jetbrains.anko.editText
import org.jetbrains.anko.selector

// JSON
import org.json.JSONObject

// View
import android.text.Editable
import android.text.TextWatcher
import android.view.ViewGroup

// App
import jgappsandgames.me.kitchenassistant.R

// KotlinX
import kotlinx.android.synthetic.main.activity_ingredient.amount
import kotlinx.android.synthetic.main.activity_ingredient.categories
import kotlinx.android.synthetic.main.activity_ingredient.ingredient_title
import kotlinx.android.synthetic.main.activity_ingredient.new_ingredient
import kotlinx.android.synthetic.main.activity_ingredient.save
import kotlinx.android.synthetic.main.activity_ingredient.unit

// Save
import jgappsandgames.me.save.ingredient.Ingredient
import jgappsandgames.me.save.ingredient.Quantity
import jgappsandgames.me.save.pantry.getIngredientSelectorList
import jgappsandgames.me.save.pantry.ingredients
import jgappsandgames.me.save.pantry.savePantry

/**
 * IngredientActivity
 * Created by Joshua Garner on 3/13/2018.
 */
class IngredientActivity: Activity() {
    // Constants -----------------------------------------------------------------------------------
    companion object {
        const val INGREDIENT = "ingredient"
        const val QUANTITY = "quantity"
    }

    // Data ----------------------------------------------------------------------------------------
    var ingredient: Ingredient? = null
    var quantity: Quantity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setResult(-1)

        // Set the View
        setContentView(R.layout.activity_ingredient)

        // Load Data and set data
        if (intent.hasExtra(INGREDIENT)) {
            ingredient = Ingredient(JSONObject(intent.getStringExtra(INGREDIENT)))

            ingredient_title.text = ingredient!!.getItem()
            categories!!.text = ingredient!!.getCategoryText()
        } else {
            ingredient = Ingredient()

            ingredient_title!!.text = "Not Yet Set"
            categories!!.text = "None"
        }

        if (intent.hasExtra(QUANTITY)) {
            quantity = Quantity(JSONObject(intent.getStringExtra(QUANTITY)))

            amount.setText(quantity!!.getAmount().toString())
            amount.hint = "Amount"
            unit.text = Quantity.Unit.unitToString(quantity!!.getUnit())
        } else {
            quantity = Quantity()

            amount.setText("")
            amount.hint = "Amount"
            unit.text = "None"
        }

        // Set Listeners
        ingredient_title.setOnClickListener {
            selector("Ingredient", getIngredientSelectorList(), { _, i ->
                changeIngredient(i)
            })
        }

        new_ingredient.setOnClickListener {
            alert {
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
                    changeIngredient(s)
                })

                negativeButton("Cancel", {})
            }.show()
        }

        categories.setOnClickListener {
            selector("Set Category", jgappsandgames.me.save.pantry.categories, {_, i ->
                ingredient!!.setCategory(jgappsandgames.me.save.pantry.categories[i])
                categories!!.text = ingredient!!.getCategoryText()
                save()
            })
        }

        categories.setOnLongClickListener {
            alert {
                var s = ""
                title = "Set Category"

                customView {
                    editText {
                        layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
                        hint = "Category"
                        addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(s: Editable?) {}
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(c: CharSequence?, start: Int, before: Int, count: Int) {
                                s = this@editText.text.toString()
                            }
                        })
                    }
                }

                positiveButton("Okay", {
                    ingredient!!.setCategory(s)
                    if (!jgappsandgames.me.save.pantry.categories.contains(s)) {
                        jgappsandgames.me.save.pantry.categories.add(s)
                        categories!!.text = ingredient!!.getCategoryText()
                        savePantry()
                    }
                    save()
                })
            }.show()

            return@setOnLongClickListener true
        }

        amount.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (amount.text.toString().isEmpty()) return
                try {
                    quantity!!.setAmount(amount.text.toString().toDouble())
                } catch (e: NumberFormatException) {}
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        unit.setOnClickListener {
            selector("Unit", Quantity.Unit.unitToList(), { _, i ->
                quantity!!.setUnit(Quantity.Unit.fromInt(i))
                updateUnit()
            })
        }

        unit.setOnLongClickListener {
            selector("Convert Unit", Quantity.Unit.unitToList(), { _, i ->
                when (i) {
                    1 -> quantity = quantity!!.toTeaspoon()
                    2 -> quantity = quantity!!.toTablespoon()
                    3 -> quantity = quantity!!.toCup()
                    4 -> quantity = quantity!!.toFluidOunce()
                    5 -> quantity = quantity!!.toPint()
                    6 -> quantity = quantity!!.toQuart()
                    7 -> quantity = quantity!!.toGallon()
                    8 -> quantity = quantity!!.toOunce()
                    9 -> quantity = quantity!!.toPound()
                    10 -> quantity = quantity!!.toMilliliter()
                    11 -> quantity = quantity!!.toLiter()
                    12 -> quantity = quantity!!.toGram()
                    13 -> quantity = quantity!!.toKilogram()
                }

                amount.setText(quantity!!.getAmount().toString())
                updateUnit()
            })

            return@setOnLongClickListener true
        }

        save.setOnClickListener {
            save()

            val data = Intent()
            data.putExtra(INGREDIENT, ingredient!!.toJSON().toString())
            data.putExtra(QUANTITY, quantity!!.toJSON().toString())
            setResult(0, data)

            finish()
        }
    }

    override fun onBackPressed() {
        alert {
            title = "Exit?"

            positiveButton("Save", {
                val data = Intent()
                data.putExtra(INGREDIENT, ingredient!!.toJSON().toString())
                data.putExtra(QUANTITY, quantity!!.toJSON().toString())
                setResult(0, data)

                finish()
            })

            neutralPressed("Cancel", {

            })

            negativeButton("Exit", {
                finish()
            })
        }.show()
    }

    // Class Methods -------------------------------------------------------------------------------
    private fun changeIngredient(pos: Int) {
        save()
        ingredient = ingredients[pos]

        ingredient_title.text = ingredient!!.getItem()
        categories.text = ingredient!!.getCategoryText()
    }

    private fun changeIngredient(text: String) {
        save()
        ingredient = Ingredient(null, null, null, text, null)
        ingredients.add(ingredient!!)
        savePantry()

        ingredient_title.text = ingredient!!.getItem()
        categories.text = ingredient!!.getCategoryText()
    }

    private fun updateUnit() {
        unit.text = Quantity.Unit.unitToString(quantity!!.getUnit())
    }

    private fun save() {
        for (i in 0 until ingredients.size) {
            val a = ingredients[i]

            if (a.getItem() == ingredient!!.getItem()) {
                ingredients[i] = ingredient!!
                return
            }
        }

        savePantry()
    }
}