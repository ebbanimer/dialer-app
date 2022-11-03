package se.miun.ebni2100.dt031g.dialer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import se.miun.ebni2100.dt031g.dialer.databinding.ActivityCallListBinding

/**
 * Activity class for List of calls.
 * @author Ebba Nimér
 */
class CallListActivity : AppCompatActivity() {

    var binding: ActivityCallListBinding? = null

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
    }
}