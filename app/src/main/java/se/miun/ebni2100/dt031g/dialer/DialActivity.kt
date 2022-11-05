package se.miun.ebni2100.dt031g.dialer

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import se.miun.ebni2100.dt031g.dialer.databinding.ActivityDialBinding
/**
 * Activity class for dial.
 * @author Ebba Nimér
 */
class DialActivity : AppCompatActivity() {

    var binding: ActivityDialBinding? = null

    /**
     * Upon creation, initialize view-binding and layout.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize view-binding and display layout.
        binding = ActivityDialBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Add toolbar.
        setSupportActionBar(binding?.toolbarDial)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}