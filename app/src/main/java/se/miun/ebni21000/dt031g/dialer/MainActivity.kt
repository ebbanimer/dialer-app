package se.miun.ebni21000.dt031g.dialer

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import se.miun.ebni21000.dt031g.dialer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Lazy initialization of binding
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get binding of this class
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickEvents()
    }

    // Click events
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

    // Show the about dialog
    private fun aboutDialog(){
        val builder = AlertDialog.Builder(this)
        builder///.setTitle()
            .setMessage("This app is supposed to mimic the keypad  \n" +
                "on a phone. The app will consist of\n" +
                "activities to:\n" +
                "\n" +
                "- Enter numbers to dial\n" +
                "- See previously dialed numbers\n" +
                "- Change the keypad settings\n" +
                "- Show on a Map where previous calls are \n" +
                "dialed from \n")
            .setPositiveButton("Ok") { dialog, _ -> dialog.cancel()
            }

        val alert = builder.create()
        // set title for alert dialog box
        alert.setTitle("About")
        // show alert dialog
        alert.show()

        //builder.show()
    }
}