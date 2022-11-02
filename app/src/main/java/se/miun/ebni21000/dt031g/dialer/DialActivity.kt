package se.miun.ebni21000.dt031g.dialer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import se.miun.ebni21000.dt031g.dialer.databinding.ActivityDialBinding

class DialActivity : AppCompatActivity() {

    var binding: ActivityDialBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDialBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarDial)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }
}