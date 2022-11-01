package se.miun.ebni21000.dt031g.dialer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import se.miun.ebni21000.dt031g.dialer.databinding.ActivityCallListBinding
import se.miun.ebni21000.dt031g.dialer.databinding.ActivityDialBinding

class CallListActivity : AppCompatActivity() {

    var binding: ActivityCallListBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCallListBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbarList)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}