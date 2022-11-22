package se.miun.ebni2100.dt031g.dialer.customviews

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import se.miun.ebni2100.dt031g.dialer.R
import se.miun.ebni2100.dt031g.dialer.SettingsActivity
import se.miun.ebni2100.dt031g.dialer.SettingsActivity.Companion.shouldStoreNumbers
import se.miun.ebni2100.dt031g.dialer.databinding.DialinputBinding


/**
 * Custom-view class representing the numbers clicked.
 * @author Ebba Nimér
 */
class DialInput @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle){

    var binding : DialinputBinding

    companion object {
        const val REQUEST_CALL = 1
    }

    /**
     * Initialize binding.
     */
    init {
        binding = DialinputBinding.bind(
            View.inflate(context, R.layout.dialinput, this)
        )

        clickEvents();
    }

    /**
     * Add title to text-field.
     */
    fun addNum(dialpadButton: DialpadButton){
        binding.dialText.append(dialpadButton.getTitle())
    }

    /**
     * Add click-events for buttons.
     */
    private fun clickEvents(){

        // Get current string, pop last character, and update view.
        binding.imgDelete.setOnClickListener {
            val s = binding.dialText.text
            val newS = s.dropLast(1)
            binding.dialText.text = newS
        }

        // Create new dial intent.
        binding.imgPhone.setOnClickListener {
            if (shouldStoreNumbers(context)){
                saveNumber(binding.dialText.text.toString())
            }
            makePhoneCall()
            //val intent = Intent(Intent.ACTION_DIAL)
            //intent.data = Uri.parse("tel:" + binding.dialText.text)
            //context.startActivity(intent)
        }
    }

    /**
     * THIS I DID IN SUBTASK 1
     */
    fun makePhoneCall(){
        val phoneToCall = binding.dialText.text
        val intent : Intent

        if (!phoneToCall.isNullOrEmpty()){
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) ==
                PackageManager.PERMISSION_GRANTED ) {
                println("access granted")
                intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneToCall"))
                context.startActivity(intent)
            } else {
                println("access not granted")
                intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneToCall"))
                context.startActivity(intent)
            }
        } else {
            Toast.makeText(context, "Enter phone number", Toast.LENGTH_SHORT).show()
        }
    }

    /**
     * Save number in default shared preferences, using set of strings.
     */
    private fun saveNumber(s: String){
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val numberSet = prefs.getStringSet(SettingsActivity.NUMBER_SET_KEY, null)
        // Add old + new sets to toSave.
        val toSave : MutableSet<String> = mutableSetOf<String>().apply {
            addAll(numberSet ?: emptySet())
            add(s)
        }
        val editor = prefs.edit()
        editor.putStringSet(SettingsActivity.NUMBER_SET_KEY, toSave).apply()
    }
}