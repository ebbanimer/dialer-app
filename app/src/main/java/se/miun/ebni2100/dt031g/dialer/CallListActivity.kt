package se.miun.ebni2100.dt031g.dialer

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
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

        displayStoredNumbers()
    }

    /**
     * Display menu option item created in call_list_menu.xml file
     */
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.call_list_menu, menu)
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
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Display all stored numbers in shared pref, if theres no numbe stored it does not print anything and we
     * display message to the user "No telephone numbers are stored" instead
     */
    private fun displayStoredNumbers(){
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