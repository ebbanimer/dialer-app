package se.miun.ebni21000.dt031g.dialer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import se.miun.ebni21000.dt031g.dialer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    // Lazy initialization of binding
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // get binding of this class
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        clickEvents()
    }

    // Click events
    private fun clickEvents(){
        binding.dialBtn.setOnClickListener {
            val intent = Intent(this, DialActivity::class.java)
            startActivity(intent)
        }

        binding.callBtn.setOnClickListener {
            val intent = Intent(this, CallListActivity::class.java)
            startActivity(intent)
        }

        binding.settingsBtn.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        binding.mapBtn.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

//        binding.dialBtn.setOnClickListener {
//            val intent = Intent(this, DialActivity::class.java)
//            startActivity(intent)
//        }


    }
}