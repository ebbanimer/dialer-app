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


    /**
     * Initialize binding.
     */
    init {
        binding = DialinputBinding.bind(
            View.inflate(context, R.layout.dialinput, this)
        )

        deleteButtonClickListener()
    }

    /**
     * Add title to text-field.
     */
    fun addNum(dialpadButton: DialpadButton){
        binding.dialText.append(dialpadButton.getTitle())
    }

    /**
     * Add click-events for delete button.
     */
    private fun deleteButtonClickListener(){

        // Get current string, pop last character, and update view.
        binding.imgDelete.setOnClickListener {
            val s = binding.dialText.text
            val newS = s.dropLast(1)
            binding.dialText.text = newS
        }

    }


    /**
     * THIS I DID IN SUBTASK 1
     */
    fun makePhoneCall(){
        val phoneToCall = binding.dialText.text

        if (!phoneToCall.isNullOrEmpty()){
            println("access granted")
            Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneToCall")).also { context.startActivity(it) }
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

    /**
     * When icon is clicked, store number if desired, and invoke listener.
     */
    fun onMakeCallListener(listener:() -> Unit){
        binding.imgPhone.setOnClickListener {
            if (shouldStoreNumbers(context)){
                saveNumber(binding.dialText.text.toString())
            }
            listener.invoke()
        }
    }
}