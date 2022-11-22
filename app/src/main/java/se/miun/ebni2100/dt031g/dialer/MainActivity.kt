package se.miun.ebni2100.dt031g.dialer

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import se.miun.ebni2100.dt031g.dialer.databinding.ActivityMainBinding
import se.miun.ebni2100.dt031g.dialer.support.Util

/**
 * Activity class for main..
 * @author Ebba Nimér
 */
class MainActivity : AppCompatActivity() {

    // Lazy initialization of binding
    lateinit var binding: ActivityMainBinding
    var hasOpened = false

    /**
     * Upon creation, get binding and initialize click-events.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(savedInstanceState != null)
            hasOpened = savedInstanceState.getBoolean("savedBoolean")

        // Get binding of this class
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickEvents()
    }

    override fun onResume() {
        super.onResume()
        Util.getInternalStorageDir(this)

    }

    override fun onPause() {
        super.onPause()
        if (!Util.defaultVoiceExist(this)){
            Util.copyDefaultVoiceToInternalStorage(this)
        }
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
            if (!hasOpened){
                aboutDialog()
            } else {
                Toast.makeText(this, R.string.already_seen,
                Toast.LENGTH_LONG).show()
            }

        }
    }

    /**
     * Save boolean
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("savedBoolean", hasOpened)
    }

    /**
     * Restore boolean
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        hasOpened = savedInstanceState.getBoolean("savedBoolean")
    }

    /**
     * Create the about-dialog.
     */
    private fun aboutDialog(){
        hasOpened = true
        // Build dialog.
        val builder = AlertDialog.Builder(this)
        builder
            .setMessage(R.string.about_dialog)
            .setPositiveButton(R.string.ok_dialog) { dialog, _ -> dialog.cancel()
            }

        // Create alert with created dialog.
        val alert = builder.create()
        alert.setTitle(R.string.about_title)
        alert.show()
    }
}