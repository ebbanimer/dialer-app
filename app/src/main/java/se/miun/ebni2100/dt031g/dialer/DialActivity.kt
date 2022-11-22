package se.miun.ebni2100.dt031g.dialer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import se.miun.ebni2100.dt031g.dialer.customviews.DialpadButton
import se.miun.ebni2100.dt031g.dialer.databinding.ActivityDialBinding
import se.miun.ebni2100.dt031g.dialer.support.SoundPlayer

/**
 * Activity class for dial.
 * @author Ebba Nimér
 */
class DialActivity : AppCompatActivity(), DialpadButton.OnClickListener {

    var binding: ActivityDialBinding? = null

    /**
     * Upon creation, initialize view-binding and layout.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view-binding and display layout.
        binding = ActivityDialBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Add toolbar.
        setSupportActionBar(binding?.toolbarDial)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding

        setDialPadsListener()

        // ???? Haj
        requestPermissionLauncher
    }

    /**
     * THIS I DID IN SUBTASK 1
     */
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                binding?.dialInput?.makePhoneCall()
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onDestroy() {
        super.onDestroy()
        SoundPlayer.getInstance(this).destroy()
    }

    /**
     * Sets listener to dialpad view, without this listener, whenever the user taps on a button
     * it would not update the dial input custom view
     */
    private fun setDialPadsListener(){
        binding?.dialpad?.setListener(this)
    }


    /**
     * Triggered every time the user clicks on a dial button and sends the object to be processed
     * within input dial custom view
     */
    override fun onClick(button: DialpadButton) {
        binding?.dialInput?.addNum(button)
    }

    /**
     * Display menu option item created in dial_menu.xml file
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dial_menu, menu)
        return true
    }

    /**
     * Click listener to send user to SettingsActivity when menu option selected
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.setting_option -> {
                startActivity(Intent(this,SettingsActivity::class.java))
                true
            }
            R.id.download_option -> {
                startActivity(Intent(this, DownloadActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}