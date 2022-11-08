package se.miun.ebni2100.dt031g.dialer

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import se.miun.ebni2100.dt031g.dialer.databinding.ActivityCallListBinding

/**
 * Activity class for List of calls.
 * @author Ebba Nimér
 */
class CallListActivity : AppCompatActivity() {

    var binding: ActivityCallListBinding? = null
    private lateinit var numberSet : MutableSet<String>

    /**
     * Upon creation, initialize view-binding and layout.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view-binding and display layout.
        binding = ActivityCallListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Add toolbar.
        setSupportActionBar(binding?.toolbarList)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        updateList()
    }

    private fun updateList(){
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
        numberSet = prefs.getStringSet(SettingsActivity.NUMBER_SET_KEY, null) ?: mutableSetOf()
        if (numberSet.isEmpty()) return

        numberSet.map{ it.plus(" \n") }.also {
            binding?.callList?.text = it.toString()
                .replace(", " ," " )
                .replace("["," ")
                .replace("]"," ")
        }
    }

}