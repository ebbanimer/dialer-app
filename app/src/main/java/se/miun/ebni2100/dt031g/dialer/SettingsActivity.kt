package se.miun.ebni2100.dt031g.dialer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
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

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}