package se.miun.ebni2100.dt031g.dialer

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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

    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }

        override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
            super.onViewCreated(view, savedInstanceState)
            findPreference<Preference>(getString(R.string.delete_key))?.setOnPreferenceClickListener {
                clearStoredNumbers()
                true
            }
        }

        private fun clearStoredNumbers(){
            val toSave : MutableSet<String>? = null
            val prefs = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = prefs.edit()
            editor.putStringSet(NUMBER_SET_KEY, toSave).apply()
        }
    }
}