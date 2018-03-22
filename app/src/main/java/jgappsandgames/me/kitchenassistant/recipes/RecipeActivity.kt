package jgappsandgames.me.kitchenassistant.recipes

// Java
import java.io.File

// Android
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle

// Anko
import org.jetbrains.anko.alert
import org.jetbrains.anko.customView
import org.jetbrains.anko.editText

// JSON
import org.json.JSONObject

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
import android.widget.TextView

// App
import jgappsandgames.me.kitchenassistant.R

import jgappsandgames.me.kitchenassistant.ingredients.IngredientActivity
import jgappsandgames.me.kitchenassistant.qr.QRActivity

// KotlinX
import kotlinx.android.synthetic.main.activity_recipe_landscape.add_ingredient_l
import kotlinx.android.synthetic.main.activity_recipe_landscape.add_step_l
import kotlinx.android.synthetic.main.activity_recipe_landscape.ingredients_l
import kotlinx.android.synthetic.main.activity_recipe_landscape.note_l
import kotlinx.android.synthetic.main.activity_recipe_landscape.steps_l
import kotlinx.android.synthetic.main.activity_recipe_landscape.title_l
import kotlinx.android.synthetic.main.activity_recipe_portrait.add_ingredient
import kotlinx.android.synthetic.main.activity_recipe_portrait.add_step
import kotlinx.android.synthetic.main.activity_recipe_portrait.ingredients
import kotlinx.android.synthetic.main.activity_recipe_portrait.note
import kotlinx.android.synthetic.main.activity_recipe_portrait.steps
import kotlinx.android.synthetic.main.activity_recipe_portrait.title_

// Save
import jgappsandgames.me.save.ingredient.Ingredient
import jgappsandgames.me.save.ingredient.Quantity
import jgappsandgames.me.save.recipe.Recipe
import jgappsandgames.me.save.utility.getApplicationFilepath
import jgappsandgames.me.save.utility.loadJSON

/**
 * RecipeActivity
 * Created by Joshua Garner on 3/12/2018.
 */
class RecipeActivity: Activity() {
    companion object {
        // Constants -------------------------------------------------------------------------------
        const val RECIPE: String = "recipe"
    }

    // Data ----------------------------------------------------------------------------------------
    private var orientation: Int = 0
    private var recipe: Recipe? = null
    private var l: Int = 0

    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (updateOrientationPortrait()) portraitCreate()
        else landscapeCreate()
    }

    override fun onResume() {
        super.onResume()

        if (isPortrait()) portraitResume()
        else landscapeResume()
    }

    override fun onPause() {
        super.onPause()

        if (isPortrait()) portraitPause()
        else landscapePause()
    }

    override fun onStop() {
        super.onStop()

        if (isPortrait()) portraitStop()
        else landscapeStop()
    }

    override fun onBackPressed() {
        if (isPortrait()) portraitBack()
        else landscapeBack()

        super.onBackPressed()
    }

    // Menu Methods --------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.recipe_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.to_qr -> {
                QRActivity.TITLE = recipe!!.getTitle()
                QRActivity.DATA = recipe!!.toQRCode()
                startActivity(Intent(this, QRActivity::class.java))
                return true
            }
        }

        return false
    }

    // Portrait Methods ----------------------------------------------------------------------------
    private fun portraitCreate() {
        setContentView(R.layout.activity_recipe_portrait)

        recipe = Recipe(loadJSON(File(getApplicationFilepath(), intent.getStringExtra(RECIPE))))

        title_.setText(recipe!!.getTitle())
        note.setText(recipe!!.getNote())

        title_.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                recipe!!.setTitle(title_.text.toString()).save()
            }
        })

        note.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                recipe!!.setNote(note.text.toString()).save()
            }
        })

        add_ingredient.setOnClickListener {
            startActivityForResult(Intent(this, IngredientActivity::class.java), 1)
        }

        add_step.setOnClickListener {
            alert {
                var t = ""
                title = "New Step " + (recipe!!.getSteps().size + 1).toString()

                customView {
                    editText {
                        hint = "Instructions"
                        addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(s: Editable?) {}
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                t = this@editText.text.toString()
                            }
                        })
                    }.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }

                positiveButton("Save", { _ ->
                    recipe!!.getSteps().add(Recipe.Step(recipe!!.getSteps().size, t))
                    recipe!!.save()
                })

                negativeButton("Cancel", { _ -> })
            }.show()
        }
    }

    private fun portraitResume() {
        ingredients.adapter = IngredientAdapter(this, recipe!!)
        steps.adapter = StepAdapter(this, recipe!!)
    }

    private fun portraitPause() {

    }

    private fun portraitStop() {

    }

    private fun portraitBack() {
        recipe!!.save()
    }

    // Landscape Methods ---------------------------------------------------------------------------
    private fun landscapeCreate() {
        setContentView(R.layout.activity_recipe_landscape)

        recipe = Recipe(loadJSON(File(getApplicationFilepath(), intent.getStringExtra(RECIPE))))

        title_l.setText(recipe!!.getTitle())
        note_l.setText(recipe!!.getNote())

        title_l.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                recipe!!.setTitle(title_l.text.toString()).save()
            }
        })

        note_l.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                recipe!!.setNote(note_l.text.toString()).save()
            }
        })

        add_ingredient_l.setOnClickListener {
            startActivityForResult(Intent(this, IngredientActivity::class.java), 1)
        }

        add_step_l.setOnClickListener {
            alert {
                var t = ""
                title = "New Step " + recipe!!.getSteps().size.toString()

                customView {
                    editText {
                        hint = "Instructions"
                        addTextChangedListener(object : TextWatcher {
                            override fun afterTextChanged(s: Editable?) {}
                            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                t = this@editText.text.toString()
                            }
                        })
                    }.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                }

                positiveButton("Save", {_ ->
                    recipe!!.getSteps().add(Recipe.Step(recipe!!.getSteps().size, t))
                    recipe!!.save()
                })

                negativeButton("Cancel", {_ ->})
            }.show()
        }
    }

    private fun landscapeResume() {
        ingredients_l.adapter = IngredientAdapter(this, recipe!!)
        steps_l.adapter = StepAdapter(this, recipe!!)
    }

    private fun landscapePause() {

    }

    private fun landscapeStop() {

    }

    private fun landscapeBack() {
        recipe!!.save()
    }

    // Private View Methods ------------------------------------------------------------------------
    private fun isPortrait(): Boolean {
        when (orientation) {
            1 -> return true
            2 -> return false

            else -> {
                val size = Point()
                windowManager.defaultDisplay.getSize(size)

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    if (isInMultiWindowMode) {
                        orientation = 1
                        return true
                    }
                }

                return if (size.y >= size.x) {
                    orientation = 1
                    true
                } else {
                    orientation = 2
                    false
                }
            }
        }
    }

    private fun updateOrientationPortrait(): Boolean {
        val size = Point()
        windowManager.defaultDisplay.getSize(size)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            if (isInMultiWindowMode) {
                orientation = 1
                return true
            }
        }

        return if (size.y >= size.x) {
            orientation = 1
            true
        } else {
            orientation = 2
            false
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            1 -> {
                if (resultCode == 0) {
                    recipe!!.getIngredients().add(Recipe.InternalIngredient(
                            Ingredient(JSONObject(data!!.getStringExtra(IngredientActivity.INGREDIENT))),
                            Quantity(JSONObject(data.getStringExtra(IngredientActivity.QUANTITY)))))

                    recipe!!.save()
                    onResume()
                }
            }

            2 -> {
                if (resultCode == 0) {
                    recipe!!.getIngredients()[l] = Recipe.InternalIngredient(
                            Ingredient(JSONObject(data!!.getStringExtra(IngredientActivity.INGREDIENT))),
                            Quantity(JSONObject(data.getStringExtra(IngredientActivity.QUANTITY))))

                    recipe!!.save()
                    onResume()
                }
            }
        }
    }

    // Adapter Class -------------------------------------------------------------------------------
    private class IngredientAdapter(val context: RecipeActivity, val recipe: Recipe): BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = LayoutInflater.from(context).inflate(R.layout.list_ingredient, parent, false)

            view.findViewById<TextView>(R.id.item).text = getItem(position).ingredient.getItem()
            view.findViewById<TextView>(R.id.categories).text = getItem(position).ingredient.getCategoryText()
            view.findViewById<TextView>(R.id.quanity).text = getItem(position).amount.getAmount().toString() + " " + Quantity.Unit.unitToString(getItem(position).amount.getUnit())

            view.findViewById<Button>(R.id.edit_button).setOnClickListener {
                context.l = position
                context.startActivityForResult(Intent(context,
                        IngredientActivity::class.java)
                        .putExtra(IngredientActivity.INGREDIENT, recipe.getIngredients()[position].ingredient.toJSON().toString())
                        .putExtra(IngredientActivity.QUANTITY, recipe.getIngredients()[position].amount.toJSON().toString()),
                        2)
            }

            view.findViewById<Button>(R.id.edit_button).setOnLongClickListener {
                recipe.getIngredients().removeAt(position)
                context.onResume()
                return@setOnLongClickListener true
            }

            return view
        }

        override fun getItem(position: Int): Recipe.InternalIngredient {
            return recipe.getIngredients()[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return recipe.getIngredients().size
        }
    }

    private class StepAdapter(val context: RecipeActivity, val recipe: Recipe): BaseAdapter() {
        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val view = LayoutInflater.from(context).inflate(R.layout.list_step, parent, false)

            view.findViewById<TextView>(R.id.step).text = (getItem(position).position + 1).toString() + ". " + getItem(position).text

            view.findViewById<TextView>(R.id.step).setOnClickListener {
                context.alert {
                    var t = recipe.getSteps()[position].text
                    title = "Edit Step " + (position + 1).toString()

                    customView {
                        editText {
                            hint = "Instructions"
                            setText(t)
                            addTextChangedListener(object : TextWatcher {
                                override fun afterTextChanged(s: Editable?) {}
                                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                                    t = this@editText.text.toString()
                                }
                            })
                        }.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                    }

                    positiveButton("Save", {_ ->
                        recipe.getSteps()[position].text = t
                        recipe.save()
                    })

                    negativeButton("Cancel", {_ ->})
                }.show()
            }

            view.findViewById<TextView>(R.id.step).setOnLongClickListener {
                recipe.getSteps().removeAt(position)
                context.onResume()
                return@setOnLongClickListener true
            }

            return view
        }

        override fun getItem(position: Int): Recipe.Step {
            return recipe.getSteps()[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return recipe.getSteps().size
        }
    }
}