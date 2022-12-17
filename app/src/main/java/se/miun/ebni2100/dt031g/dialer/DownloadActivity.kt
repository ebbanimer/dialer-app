package se.miun.ebni2100.dt031g.dialer

import android.Manifest
import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.*
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import se.miun.ebni2100.dt031g.dialer.databinding.ActivityDownloadBinding
import se.miun.ebni2100.dt031g.dialer.support.Util
import java.io.*
import java.lang.ref.WeakReference
import java.net.URL


/**
 * Activity for downloading songs.
 * @author Ebba Nimér
 */
class DownloadActivity : AppCompatActivity() {

    var binding : ActivityDownloadBinding? = null
    lateinit var webView : WebView
    lateinit var webSite : String
    lateinit var destDir : String
    private lateinit var urlDownload : String

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initWebView()

        // Initialize progress-dialog.
        mProgressDialog = ProgressDialog(this)
        mProgressDialog!!.setCancelable(true)
        mProgressDialog!!.setIndeterminate(true)
        mProgressDialog!!.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
    }


    /**
     * Create web-view
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(){
        this.webView = binding!!.webView

        binding.apply {
            webSite = intent.getStringExtra("url").toString()
            destDir = intent.getStringExtra("dir").toString()

            webView.loadUrl(webSite)
            webView.isVerticalScrollBarEnabled = true
            webView.settings.javaScriptEnabled = true
        }

        // Create web-client with url-downloading.
        webView.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.R)
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                urlDownload = url
                if (!triggered){
                    triggered = true
                    // if it has needed permissions, start async for downloading.
                    if(hasWriteStoragePermission() && hasReadStoragePermission()){
                        DownloadAsync(
                            this@DownloadActivity,
                            URLUtil.guessFileName(url, null, null),
                            url, destDir
                        ).execute(URL(url))
                        mProgressDialog?.show()
                    }else{
                        requestWriteStoragePermission()
                        requestReadStoragePermission()
                    }
                }
                return false
            }
        }
    }

    /**
     * Request download permission from user.
     */
    private val requestPermissionLauncher = registerForActivityResult( ActivityResultContracts.RequestPermission() ) { isGranted: Boolean ->
        if (isGranted) {
            binding?.webView?.loadUrl(urlDownload)
        } else {
            Toast.makeText( this,
                R.string.request_download,
                Toast.LENGTH_LONG
            ).show()
            triggered = false
        }
    }

    /**
     * Verify if it has write to storage permission.
     */
    private fun hasWriteStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Verify if it has read to storage permission.
     */
    private fun hasReadStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * Request write permission.
     */
    private fun requestWriteStoragePermission(){
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    /**
     * Request read permission.
     */
    private fun requestReadStoragePermission(){
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    companion object{

        private var mProgressDialog: ProgressDialog? = null
        private var triggered = false

        /**
         * Async task for downloading in background thread.
         */
        class DownloadAsync constructor(activity: DownloadActivity, private val fileName: String, private val url: String, private val destDir: String): AsyncTask<URL, Int, Unit>(){
            private lateinit var mWakeLock: PowerManager.WakeLock
            @SuppressLint("StaticFieldLeak")
            var context: Context

            private val root = Environment.getExternalStorageDirectory().toString()

            init {
                this.context = activity
            }

            @SuppressLint("SdCardPath", "RestrictedApi", "Range")
            @Deprecated("Deprecated in Java")
            override fun doInBackground(vararg urls: URL?) {

                urls.first()?.let { url ->
                    try {
                        // Open a connection to URL
                        val connection = url.openConnection().also { it?.connect() }
                        val length = connection?.contentLength   // size of file
                        val input = BufferedInputStream(url.openStream(), 100 * 1024)
                        val data = ByteArray(1024)   // amount of data to be downloaded each time

                        val output = FileOutputStream("$root/Download/$fileName")

                        var count = 0   // how much it downloads in each loop
                        var total = 0   // how much it has totally downloaded

                        // while the downloaded amount is 0 or above..
                        while (count > -1) {
                            count = input.read(data)
                            if (count == -1){
                                break
                            }

                            total += count
                            if (length != null) {
                                if (length > 0){
                                    publishProgress((total * 100) / length)   // publish the progress of total downloaded data
                                }
                            }
                            output.write(data, 0, count)  // save data
                            Thread.sleep(20)
                        }

                        output.flush()
                        output.close()
                        input.close()

                    } catch (e: Exception) {
                        println("DownloadActivity doInBackground exception: ${e.message}")
                    }
                    try {
                        Thread.sleep(1000)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }


            @SuppressLint("WrongThread")
            @RequiresApi(Build.VERSION_CODES.O)
            @Deprecated("Deprecated in Java")
            override fun onPreExecute() {
                super.onPreExecute()

                // Before execute, display info about download and initialize wakelock to ensure the screen stays awake.
                mProgressDialog?.setTitle(R.string.download_title)
                mProgressDialog?.setMessage(fileName)
                mWakeLock =
                    (context.getSystemService(POWER_SERVICE) as PowerManager).run {
                        newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, javaClass.name).apply {
                            acquire(10*60*1000L /*10 minutes*/)
                        }
                    }
                mProgressDialog?.show()
                publishProgress(0)
            }

            @Deprecated("Deprecated in Java")
            override fun onProgressUpdate(vararg values: Int?) {
                super.onProgressUpdate(*values)

                // throughout the download, display progress.
                mProgressDialog?.setIndeterminate(false)
                mProgressDialog?.setMax(100)
                values[0]?.let { mProgressDialog?.setProgress(it) }
            }


            @Deprecated("Deprecated in Java")
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                mWakeLock.release()
                mProgressDialog?.dismiss()
                val file = File("$root/Download/$fileName")

                // if the unzipping and copying was successful, delete file from downloads and
                // display to the user that download was successful.
                if (Util.unzip(file, File(destDir))){
                    Toast.makeText(context,R.string.download_success, Toast.LENGTH_SHORT).show()
                    file.delete()
                } else {
                    Toast.makeText(context,R.string.download_fail, Toast.LENGTH_LONG).show()
                }

                triggered = false

            }
        }
    }
}






