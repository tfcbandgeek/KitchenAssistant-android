package jgappsandgames.me.save

// Save
import jgappsandgames.me.save.pantry.createPantry
import jgappsandgames.me.save.pantry.loadPantry
import jgappsandgames.me.save.pantry.savePantry
import jgappsandgames.me.save.recipe.createRecipes
import jgappsandgames.me.save.recipe.loadRecipes
import jgappsandgames.me.save.recipe.saveRecipes
import jgappsandgames.me.save.shoppinglist.createShopping
import jgappsandgames.me.save.shoppinglist.loadShopping
import jgappsandgames.me.save.shoppinglist.saveShopping

/**
 * MasterManager
 * Created by Joshua Garner on 3/12/2018.
 */
fun create() {
    createRecipes()
    createPantry()
    createShopping()
}

fun load() {
    loadRecipes()
    loadPantry()
    loadShopping()
}

fun save() {
    saveRecipes()
    savePantry()
    saveShopping()
}