package se.miun.ebni2100.dt031g.dialer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import se.miun.ebni2100.dt031g.dialer.customviews.DialpadButton
import se.miun.ebni2100.dt031g.dialer.databinding.ActivityDialBinding
import se.miun.ebni2100.dt031g.dialer.support.SoundPlayer

/**
 * Activity class for dial.
 * @author Ebba Nimér
 */
class DialActivity : AppCompatActivity(), DialpadButton.OnClickListener {

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

        setDialPadsListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        SoundPlayer.getInstance(this).destroy()
    }

    private fun setDialPadsListener(){
        binding?.dialpad?.setListener(this)
    }

    override fun onClick(button: DialpadButton) {
        binding?.dialInput?.addNum(button)
    }
}