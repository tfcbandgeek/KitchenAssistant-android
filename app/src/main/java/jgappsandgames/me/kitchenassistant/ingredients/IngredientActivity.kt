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
import jgappsandgames.me.save.ingredient.Quanity
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
        const val QUANITY = "quanity"
    }

    // Data ----------------------------------------------------------------------------------------
    var ingredient: Ingredient? = null
    var quanity: Quanity? = null

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

        if (intent.hasExtra(QUANITY)) {
            quanity = Quanity(JSONObject(intent.getStringExtra(QUANITY)))

            amount.setText(quanity!!.getAmount().toString())
            amount.hint = "Amount"
            unit.text = Quanity.Unit.unitToString(quanity!!.getUnit())
        } else {
            quanity = Quanity()

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
                    quanity!!.setAmount(amount.text.toString().toDouble())
                } catch (e: NumberFormatException) {}
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        unit.setOnClickListener {
            selector("Unit", Quanity.Unit.unitToList(), {_, i ->
                quanity!!.setUnit(Quanity.Unit.fromInt(i))
                updateUnit()
            })
        }

        unit.setOnLongClickListener {
            selector("Convert Unit", Quanity.Unit.unitToList(), {_, i ->
                when (i) {
                    1 -> quanity = quanity!!.toTeaspoon()
                    2 -> quanity = quanity!!.toTablespoon()
                    3 -> quanity = quanity!!.toCup()
                    4 -> quanity = quanity!!.toFluidOunce()
                    5 -> quanity = quanity!!.toPint()
                    6 -> quanity = quanity!!.toQuart()
                    7 -> quanity = quanity!!.toGallon()
                    8 -> quanity = quanity!!.toOunce()
                    9 -> quanity = quanity!!.toPound()
                    10 -> quanity = quanity!!.toMilliliter()
                    11 -> quanity = quanity!!.toLiter()
                    12 -> quanity = quanity!!.toGram()
                    13 -> quanity = quanity!!.toKilogram()
                }

                amount.setText(quanity!!.getAmount().toString())
                updateUnit()
            })

            return@setOnLongClickListener true
        }

        save.setOnClickListener {
            save()

            val data = Intent()
            data.putExtra(INGREDIENT, ingredient!!.toJSON().toString())
            data.putExtra(QUANITY, quanity!!.toJSON().toString())
            setResult(0, data)

            finish()
        }
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onBackPressed() {
        alert {
            title = "Exit?"

            positiveButton("Save", {
                val data = Intent()
                data.putExtra(INGREDIENT, ingredient!!.toJSON().toString())
                data.putExtra(QUANITY, quanity!!.toJSON().toString())
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
        unit.text = Quanity.Unit.unitToString(quanity!!.getUnit())
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