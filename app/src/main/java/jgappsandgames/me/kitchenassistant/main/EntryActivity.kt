package jgappsandgames.me.kitchenassistant.main

// Android
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Gravity

// Anko
import org.jetbrains.anko.button
import org.jetbrains.anko.verticalLayout

// App
import jgappsandgames.me.kitchenassistant.home.HomeActivity
import jgappsandgames.me.kitchenassistant.home.HomeActivityTablet

// Save
import jgappsandgames.me.save.create
import jgappsandgames.me.save.load
import jgappsandgames.me.save.save
import jgappsandgames.me.save.utility.deviceIsTablet
import jgappsandgames.me.save.utility.isFirstRun
import jgappsandgames.me.save.utility.loadFilepaths

/**
 * EntryActivity
 * Created by Joshua Garner on 3/12/2018.
 */
class EntryActivity: Activity() {
    // LifeCycle Methods ---------------------------------------------------------------------------
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verticalLayout {
            button {
                gravity = Gravity.CENTER_HORIZONTAL
                text = "Phone"
                setOnClickListener {
                    startActivity(Intent(this@EntryActivity, HomeActivity::class.java))
                }
            }

            button {
                gravity = Gravity.CENTER_HORIZONTAL
                text = "Tablet"
                setOnClickListener {
                    startActivity(Intent(this@EntryActivity, HomeActivityTablet::class.java))
                }
            }
        }

        loadFilepaths(this)
        if (isFirstRun()) {
            create()
            save()

            if (deviceIsTablet()) startActivity(Intent(this, HomeActivityTablet::class.java))
            else startActivity(Intent(this, HomeActivity::class.java))
        } else {
            load()

            if (deviceIsTablet()) startActivity(Intent(this, HomeActivityTablet::class.java))
            else startActivity(Intent(this, HomeActivity::class.java))
        }
    }
}