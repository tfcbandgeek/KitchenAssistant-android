package jgappsandgames.me.kitchenassistant.home

// Android
import android.app.Activity
import android.content.Intent
import android.graphics.Point
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem

// Views
import android.view.View
import android.view.ViewGroup
import android.widget.*

// Anko

// JSON
import org.json.JSONObject

// Zxing
import com.google.zxing.integration.android.IntentIntegrator

// App
import jgappsandgames.me.kitchenassistant.R
import jgappsandgames.me.kitchenassistant.pantry.PantryAdapter

import jgappsandgames.me.kitchenassistant.recipes.RecipeActivity
import jgappsandgames.me.kitchenassistant.recipes.RecipeAdapter
import jgappsandgames.me.kitchenassistant.shoppinglist.ShoppingListActivity
import jgappsandgames.me.kitchenassistant.shoppinglist.ShoppingListAdapter
import jgappsandgames.me.save.ingredient.Ingredient
import jgappsandgames.me.save.pantry.getIngredientSelectorList

// KotlinX
import kotlinx.android.synthetic.main.activity_main_landscape.add_button_a
import kotlinx.android.synthetic.main.activity_main_landscape.add_button_b
import kotlinx.android.synthetic.main.activity_main_landscape.list_a
import kotlinx.android.synthetic.main.activity_main_landscape.list_b
import kotlinx.android.synthetic.main.activity_main_landscape.new_title_a
import kotlinx.android.synthetic.main.activity_main_landscape.new_title_b
import kotlinx.android.synthetic.main.activity_main_landscape.spinner_a
import kotlinx.android.synthetic.main.activity_main_landscape.spinner_b
import kotlinx.android.synthetic.main.activity_main_portrait.add_button
import kotlinx.android.synthetic.main.activity_main_portrait.list
import kotlinx.android.synthetic.main.activity_main_portrait.new_title
import kotlinx.android.synthetic.main.activity_main_portrait.selector

// Save
import jgappsandgames.me.save.pantry.importIngredients
import jgappsandgames.me.save.pantry.ingredients
import jgappsandgames.me.save.pantry.pantry
import jgappsandgames.me.save.recipe.createRecipe
import jgappsandgames.me.save.recipe.Recipe
import jgappsandgames.me.save.recipe.recipes
import jgappsandgames.me.save.recipe.saveRecipes
import jgappsandgames.me.save.shoppinglist.ShoppingList
import jgappsandgames.me.save.shoppinglist.createShoppingList
import jgappsandgames.me.save.shoppinglist.saveShopping
import jgappsandgames.me.save.shoppinglist.shopping_lists
import org.jetbrains.anko.*

/**
 * HomeActivity
 * Created by Joshua Garner on 3/12/2018.
 */
class HomeActivity: Activity(), RecipeAdapter.RecipeListener, ShoppingListAdapter.ShoppingListListener, PantryAdapter.PantryListener {
    // Data ----------------------------------------------------------------------------------------
    private var orientation: Int = 0

    private var l: Int = 0
    private var ll: Int = 0

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
        super.onBackPressed()

        if (isPortrait()) portraitBack()
        else landscapeBack()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                val json = JSONObject(result.contents)

                if (json.optInt("TYPE", 0) == 1) {
                    val r = Recipe(json).save()
                    importIngredients(r)
                    recipes.add(r.getFilename())
                    saveRecipes()
                    startActivity(Intent(this, RecipeActivity::class.java).putExtra(RecipeActivity.RECIPE, r.getFilename()))
                } else if (json.optInt("TYPE", 0) == 2) {
                    val s = ShoppingList(json).save()
                    importIngredients(s)
                    shopping_lists.add(s.getFilename())
                    saveShopping()
                    startActivity(Intent(this, ShoppingListActivity::class.java).putExtra(ShoppingListActivity.LIST, s.getFilename()))
                }
            }
        }
    }

    // Menu Methods --------------------------------------------------------------------------------
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item!!.itemId) {
            R.id.switch_ -> {
                startActivity(Intent(this, HomeActivityTablet::class.java))
                toast("Change to Tablet View")
                finish()
                return true
            }
        }

        return false
    }

    // Portrait Methods ----------------------------------------------------------------------------
    private fun portraitCreate() {
        setContentView(R.layout.activity_main_portrait)

        selector.adapter = ArrayAdapter.createFromResource(this, R.array.selector, android.R.layout.simple_selectable_list_item)
        selector.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                l = position
                setPosition(position, new_title, list, add_button)
            }
        }
    }

    private fun portraitResume() {
        setPosition(l, new_title, list, add_button)
    }

    private fun portraitPause() {

    }

    private fun portraitStop() {

    }

    private fun portraitBack() {

    }

    // Landscape Methods ---------------------------------------------------------------------------
    private fun landscapeCreate() {
        setContentView(R.layout.activity_main_landscape)

        spinner_a.adapter = ArrayAdapter.createFromResource(this, R.array.selector, android.R.layout.simple_selectable_list_item)
        spinner_b.adapter = ArrayAdapter.createFromResource(this, R.array.selector, android.R.layout.simple_selectable_list_item)

        spinner_a.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                l = position
                setPosition(position, new_title, list, add_button)
            }
        }

        spinner_b.onItemSelectedListener = object: AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                ll = position
                setPosition(position, new_title, list, add_button)
            }
        }
    }

    private fun landscapeResume() {
        setPosition(l, new_title_a, list_a, add_button_a)
        setPosition(ll, new_title_b, list_b, add_button_b)
    }

    private fun landscapePause() {

    }

    private fun landscapeStop() {

    }

    private fun landscapeBack() {

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

    // Private Class Methods -----------------------------------------------------------------------
    private fun setPosition(_position: Int, _title: EditText, _list: ListView, _button: Button) {
        when (_position) {
            0 -> {
                title = "Recipes"
                _title.setText("")
                _title.hint = "Create Recipe"
                _list.adapter = RecipeAdapter(this@HomeActivity, this@HomeActivity)
                _button.setOnClickListener {
                    val t = _title.text.toString()
                    if (!t.isEmpty()) {
                        val r = createRecipe(t)
                        startActivity(Intent(this@HomeActivity, RecipeActivity::class.java).putExtra(RecipeActivity.RECIPE, r.getFilename()))
                    }
                }
                _button.setOnLongClickListener {
                    IntentIntegrator(this).setPrompt("Scan in Recipe")
                            .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                            .initiateScan()
                    return@setOnLongClickListener true
                }
            }

            1 -> {
                title = "Shopping Lists"
                _title.setText("")
                _title.hint = "Create Shopping List"
                _list.adapter = ShoppingListAdapter(this@HomeActivity, this@HomeActivity)
                _button.setOnClickListener {
                    val t = _title.text.toString()
                    if (!t.isEmpty()) {
                        val l = createShoppingList(t)
                        startActivity(Intent(this@HomeActivity, ShoppingListActivity::class.java).putExtra(ShoppingListActivity.LIST, l.getFilename()))
                    }
                }
                _button.setOnLongClickListener {
                    IntentIntegrator(this).setPrompt("Scan in Recipe")
                            .setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
                            .initiateScan()
                    return@setOnLongClickListener true
                }
            }

            2 -> {
                title = "Pantry"
                _title.setText("")
                _title.hint = "Add Pantry Item"
                _list.adapter = PantryAdapter(this@HomeActivity, this@HomeActivity)
                _button.setOnClickListener {
                    selector("Select Ingredient", getIngredientSelectorList(), {_, i ->
                        pantry.add(ingredients[i])
                        onResume()
                    })
                }

                _button.setOnLongClickListener {
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
                            val a = Ingredient(null, null, null, s, null)
                            pantry.add(a)
                            ingredients.add(a)
                            onResume()
                        })

                        negativeButton("Cancel", {})
                    }.show()

                    return@setOnLongClickListener true
                }
            }
        }
    }

    // Recipe Listener -----------------------------------------------------------------------------
    override fun onChange() {
        onResume()
    }
}