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
import se.miun.ebni2100.dt031g.dialer.support.SoundPlayer
import se.miun.ebni2100.dt031g.dialer.support.Util
import java.io.File

/**
 * Activity class for settings.
 * @author Ebba Nimér
 */
class SettingsActivity : AppCompatActivity() {

    var binding: SettingsActivityBinding? = null

    /**
     * Upon creation, initialize view-binding, display layout, and settings-fragment.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = SettingsActivityBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Add toolbar.
        //setSupportActionBar(binding?.toolBarSettings)
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

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

        /**
         * Provide to the user which the selected voice is.
         */
        fun voiceToUse(context: Context): String? {
            val sharedPreferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)
            return sharedPreferences.getString(context.getString(R.string.voice_key), "mamacita_us")
        }
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

            // Get list preferences
            val voicePref: ListPreference? = findPreference(getString(R.string.voice_key))
            val sharedPreferences: SharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context)

            // Retrieve the chosen voice from shared preferences.
            val initVoice = sharedPreferences.getString((context?.getString(R.string.voice_key) ?: String) as String?, "mamacita_us")

            // Set the selected voice in SoundPlayer.
            if (initVoice != null) {
                context?.let { SoundPlayer.getInstance(it) }?.setSelectedVoice(initVoice)
            } else {
                // If voice is null, send default mamacita.
                context?.let { SoundPlayer.getInstance(it) }?.setSelectedVoice(Util.MAMACITA_DIR)
            }

            // Display chosen voice in summary and populate list-preferences.
            voicePref?.summary = initVoice
            voicePref?.entries = createList()
            voicePref?.entryValues = createList()

            // If the selected voice changes, change the summary and update voice in SoundPlayer.
            voicePref?.setOnPreferenceChangeListener { preference, newValue ->
                if (preference is ListPreference) {
                    val index = preference.findIndexOfValue(newValue.toString())
                    val entry = preference.entries[index]
                    voicePref.summary = entry
                    context?.let { SoundPlayer.getInstance(it) }?.setSelectedVoice(entry as String)
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

        /**
         * Create and populate list-preferences based on files in local storage.
         */
        private fun createList(): Array<String>{
            val mutableList : MutableList<String> = arrayListOf()

            File(getString(R.string.new_dir)).walk().forEach {
                if (it.isDirectory){
                    if (it.name != "voices"){
                        mutableList.add(it.name)
                    }
                }
            }

            return mutableList.toTypedArray()
        }
    }
}