package se.miun.ebni2100.dt031g.dialer

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.webkit.DownloadListener
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import se.miun.ebni2100.dt031g.dialer.databinding.ActivityDownloadBinding
import java.io.File
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * Activity for downloading songs.
 * @author Ebba Nimér
 */
class DownloadActivity : AppCompatActivity() {

    var binding : ActivityDownloadBinding? = null
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    var isWorking = false
    lateinit var webView : WebView
    lateinit var webSite : String
    lateinit var downloadListener : DownloadListener

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        //if(savedInstanceState != null)
        //    isWorking = savedInstanceState.getBoolean("savedBooleanWork")

        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding!!.toolBarDownload)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        println(Thread.currentThread().name + " in on create. fiffi")

        initWebView()
        executorService.execute(this::setDownloadListener)
        //setDownloadListener()
        //createDownloadListener()
        //onDownloadComplete()

        // Initiate WebView and load URL.
        //binding?.webView?.webViewClient  = WebViewClient()
        //binding?.webView?.loadUrl("https://dt031g.programvaruteknik.nu/dialer/voices/")

        // enable the javascript settings
        //binding?.webView?.settings?.javaScriptEnabled  = true

    }


    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(){
        webView = binding!!.webView

        webSite = "https://dt031g.programvaruteknik.nu/dialer/voices/"
        webView.loadUrl(webSite)
        webView.settings.javaScriptEnabled = true

        //webView.webViewClient = WebViewClient()
        //intent.extras?.getString("url")?.let { webView.loadUrl(it) }

        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }
        }
    }
    
    private fun setDownloadListener(){
        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            isWorking = true

            println(Thread.currentThread().name + " in download. fiffi")

            val request = DownloadManager.Request(
                Uri.parse(url)
            )

            request.allowScanningByMediaScanner()
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) //Notify client once download is completed!

            val fileName = URLUtil.guessFileName(url, contentDisposition, mimetype)

            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                fileName
            )
            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            dm.enqueue(request)
            Toast.makeText(
                applicationContext,
                "Downloading File",  //To notify the Client that the file is being downloaded
                Toast.LENGTH_LONG
            ).show()


            unzipFiles(fileName)
            //Util.unzip(fileName, Util.getInternalStorageDir())

        }
    }

    private fun unzipFiles(string: String){

        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

        File(dir.name).walk().forEach {
            println("amen tjena " + it.name)
        }


        /**

        File(Environment.DIRECTORY_DOWNLOADS).walk().forEach {
            println("ebba heter jag $it")
            println("String grejsimos: $string")
            if (it.name == string){
                println("It grejsimos: " + it.name)
                Util.unzip(it, Util.getDirForVoice(this, string))
                return
            }
        }*/
    }

    /**
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean("savedBooleanWork", isWorking)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        isWorking = savedInstanceState.getBoolean("savedBooleanWork")
    }*/




    /**
     * NOT SURE ????????
     */
    private fun onDownloadComplete() {
        val onComplete = object : BroadcastReceiver (){
            override fun onReceive(context: Context?, intent: Intent?) {
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show()
            }
        }
        registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    /**
     * NOT SURE ????????
     */
    private fun checkDownloadPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this@DownloadActivity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        ) {
            Toast.makeText(
                this@DownloadActivity,
                "Write External Storage permission allows us to save files. Please allow this permission in App Settings.",
                Toast.LENGTH_LONG
            ).show()
        } else {
            ActivityCompat.requestPermissions(
                this@DownloadActivity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                100
            )
        }
    }


    /**
     * NOT SURE ????????
     */
    private fun createDownloadListener(){

        downloadListener = DownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            val request = DownloadManager.Request(Uri.parse(url))
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            val fileName = URLUtil.guessFileName(url, contentDisposition, mimetype)
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName)
            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        }


        /**
        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->
            //tell what to happen here


            val request = DownloadManager.Request(Uri.parse(url))

            request.setTitle(URLUtil.guessFileName(url,contentDisposition,mimetype))
            request.setDescription("Downloading file...")
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
                URLUtil.guessFileName(url, contentDisposition, mimetype));

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        }*/
    }
}
