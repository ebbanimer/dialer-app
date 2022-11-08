package se.miun.ebni2100.dt031g.dialer.customviews

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
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
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:" + binding.dialText.text)
            context.startActivity(intent)
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