package se.miun.ebni2100.dt031g.dialer

import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import se.miun.ebni2100.dt031g.dialer.databinding.ActivityDownloadBinding

/**
 * Activity for downloading songs.
 * @author Ebba Nimér
 */
class DownloadActivity : AppCompatActivity() {

    var binding : ActivityDownloadBinding? = null

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding!!.toolBarDownload)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        val webView = binding!!.webView

        webView.webViewClient = WebViewClient()
        webView.loadUrl("https://dt031g.programvaruteknik.nu/dialer/voices/")
        webView.settings.javaScriptEnabled = true

        // Initiate WebView and load URL.
        //binding?.webView?.webViewClient  = WebViewClient()
        //binding?.webView?.loadUrl("https://dt031g.programvaruteknik.nu/dialer/voices/")

        // enable the javascript settings
        //binding?.webView?.settings?.javaScriptEnabled  = true

    }

}
