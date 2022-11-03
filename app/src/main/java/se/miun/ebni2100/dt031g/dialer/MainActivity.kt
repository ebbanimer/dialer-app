package se.miun.ebni2100.dt031g.dialer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import se.miun.ebni2100.dt031g.dialer.databinding.ActivityMainBinding

/**
 * Activity class for main..
 * @author Ebba Nimér
 */
class MainActivity : AppCompatActivity() {

    // Lazy initialization of binding
    lateinit var binding: ActivityMainBinding

    /**
     * Upon creation, get binding and initialize click-events.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get binding of this class
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickEvents()
    }

    /**
     * Add click-events for each button. Opening relevant activity based on choice.
     */
    private fun clickEvents(){
        binding.dialBtn.setOnClickListener {
            val intent = Intent(this, DialActivity::class.java)
            startActivity(intent)
        }

        binding.callBtn.setOnClickListener {
            val intent = Intent(this, CallListActivity::class.java)
            startActivity(intent)
        }

        binding.settingsBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.mapBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        binding.aboutBtn.setOnClickListener {
            aboutDialog()
        }
    }

    /**
     * Create the about-dialog.
     */
    private fun aboutDialog(){

        // Build dialog.
        val builder = AlertDialog.Builder(this)
        builder
            .setMessage("This app is supposed to mimic the keypad " +
                "on a phone. The app will consist of " +
                "activities to:\n" +
                "\n" +
                "- Enter numbers to dial\n" +
                "- See previously dialed numbers\n" +
                "- Change the keypad settings\n" +
                "- Show on a Map where previous calls are " +
                "dialed from \n")
            .setPositiveButton("Ok") { dialog, _ -> dialog.cancel()
            }

        // Create alert with created dialog.
        val alert = builder.create()
        alert.setTitle("About")
        alert.show()
    }
}