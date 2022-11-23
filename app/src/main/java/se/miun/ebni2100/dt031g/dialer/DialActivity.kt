package se.miun.ebni2100.dt031g.dialer

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import se.miun.ebni2100.dt031g.dialer.customviews.DialpadButton
import se.miun.ebni2100.dt031g.dialer.databinding.ActivityDialBinding
import se.miun.ebni2100.dt031g.dialer.support.SoundPlayer

/**
 * Activity class for dial.
 * @author Ebba Nimér
 */
class DialActivity : AppCompatActivity(), DialpadButton.OnClickListener {

    var binding: ActivityDialBinding? = null

    companion object{
        const val CALL_PERMISSION_REQ_CODE = 10
    }

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
        setOnMakeCallListener()

    }

    private fun displayRationale(){
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.call_title)
            .setMessage(R.string.call_description)
            .setPositiveButton(R.string.ok_dialog) { _, _ ->  sendUserToSettings() }
            .setNegativeButton(R.string.cancel_dialog) { dialog, _ -> dialog.dismiss() }

        // Create alert with created dialog.
        val alert = builder.create()
        alert.show()
    }

    private fun sendUserToSettings(){
        Intent().apply {
            action = ACTION_APPLICATION_DETAILS_SETTINGS
            data = Uri.fromParts("package", packageName, null)
        }.also { startActivity(it) }
    }

    private val requestPermissionLauncher = registerForActivityResult( ActivityResultContracts.RequestPermission() ) { isGranted: Boolean ->
        if (isGranted) {
            binding?.dialInput?.makePhoneCall()
        } else {
            displayRationale()
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

    /**
     * Verify if the user has permission to make phone call.
     */
    private fun hasCallPhonePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Request permission to call phone.
     */
    private fun requestCallPhonePermission(){
        requestPermissionLauncher.launch(Manifest.permission.CALL_PHONE)
    }

    /**
     * Click-listener for making a phone-call.
     */
    private fun setOnMakeCallListener(){
        binding?.dialInput?.onMakeCallListener {
            if (hasCallPhonePermission()){
                binding?.dialInput?.makePhoneCall()
            }else{
                requestCallPhonePermission()
            }
        }
    }
}