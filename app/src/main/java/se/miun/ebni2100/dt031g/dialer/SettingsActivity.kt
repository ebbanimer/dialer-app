package se.miun.ebni2100.dt031g.dialer

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.ListPreference
import androidx.preference.ListPreference.SimpleSummaryProvider
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import se.miun.ebni2100.dt031g.dialer.databinding.SettingsActivityBinding

/**
 * Activity class for settings.
 * @author Ebba Nimér
 */
class SettingsActivity : AppCompatActivity() {

    var binding: SettingsActivityBinding? = null
    var voicePref: ListPreference? = null
    var chosenVoice: String? = null

    /**
     * Upon creation, initialize view-binding, display layout, and settings-fragment.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Add toolbar.
        setSupportActionBar(binding?.toolBarSettings)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        if (savedInstanceState == null) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(R.id.settings, SettingsFragment())
                    .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    /**
     * Click listener to send to the previous screen when back button arrow pressed,
     * without this logic the user would be sent to MainActivity.class even if the user
     * would come from CallListActivity or DialActivity
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Define static methods to be accessed by other classes.
     */
    companion object {
        const val NUMBER_SET_KEY = "NumberSetKey"

        /**
         * Verify which preference in settings is set.
         */
        fun shouldStoreNumbers(context: Context): Boolean {
            val sharedPreferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getBoolean(context.getString(R.string.store_key), true)
        }

        /*fun voiceToUse(context: Context): String {
            voicePref
            val sharedPreferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getBoolean(context.getString(R.string.store_key), true)
        }*/
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        /**
         * Inflates root_preferences.xml file as layout
         */
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        /**
         * Binds clear stored number option and triggers logic to remove stored numbers
         */
        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            findPreference<Preference>(getString(R.string.delete_key))?.setOnPreferenceClickListener {
                clearStoredNumbers()
                true
            }

            val voicePref: ListPreference? = findPreference(getString(R.string.voice_key))
            val voices = arrayOf("hello", "bajs")

            voicePref?.entries = voices
            voicePref?.entryValues = voices

            voicePref?.setOnPreferenceChangeListener { preference, newValue ->
                if (preference is ListPreference) {
                    val index = preference.findIndexOfValue(newValue.toString())
                    val entry = preference.entries[index]
                    val entryvalue = preference.entryValues[index]
                    voicePref.summary = entry
                    println("haj entry is: $entry")
                    println("haj entryvalue is: $entryvalue")
                }

                true
            }




        }

        /**
         * Saves a null Set<String> object, therefore all previous numbers stored are removed
         */
        private fun clearStoredNumbers(){
            val toSave : MutableSet<String>? = null
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()
            editor.putStringSet(NUMBER_SET_KEY, toSave).apply()
        }

    }
}