package se.miun.ebni2100.dt031g.dialer

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.Uri
import android.os.*
import android.util.Log
import android.webkit.DownloadListener
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import se.miun.ebni2100.dt031g.dialer.databinding.ActivityDownloadBinding
import se.miun.ebni2100.dt031g.dialer.support.Util
import java.io.File
import java.lang.ref.WeakReference
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


/**
 * Activity for downloading songs.
 * @author Ebba Nimér
 */
class DownloadActivity : AppCompatActivity() {

    // REGISTER A DOWNLOAD-LISTENER
    // onDownloadStart CHECK AND MANAGE THE DOWNLOADS BEING MADE
    // DOWNLOAD & UNZIP IN BACKGROUND THREAD (COROUTINE)
    // PROVIDE LOCATION TO STORE IN THE INTENT
    // USE THE CLASSES 'URL' AND 'URLConnection' TO CREATE InputStream TO THE FILE BEING DOWNLOADED
    // USE Util.unzip FOR THE DOWNLOADED FILE
    // WHILE BEING DOWNLOADED, USE ProgressDialog


    var binding : ActivityDownloadBinding? = null
    lateinit var webView : WebView
    lateinit var webSite : String
    lateinit var destDir : String
    var triggered = false
    private lateinit var urlDownload : String

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_download)

        binding = ActivityDownloadBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding!!.toolBarDownload)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initWebView()


        //if(savedInstanceState != null)
        //    isWorking = savedInstanceState.getBoolean("savedBooleanWork")

        //executorService.execute(this::setDownloadListener)
        //setDownloadListener()
        //createDownloadListener()
        //onDownloadComplete()

        // Initiate WebView and load URL.
        //binding?.webView?.webViewClient  = WebViewClient()
        //binding?.webView?.loadUrl("https://dt031g.programvaruteknik.nu/dialer/voices/")

        // enable the javascript settings
        //binding?.webView?.settings?.javaScriptEnabled  = true

    }


    /**
     * Create web-view
     */
    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(){
        this.webView = binding!!.webView

        binding.apply {
            webSite = "https://dt031g.programvaruteknik.nu/dialer/voices/"
            webView.loadUrl(webSite)
            webView.settings.javaScriptEnabled = true
            destDir = intent.getStringExtra("dir").toString()
            println("Fisis This is the destDir from init web view: $destDir")
        }


        //webView.webViewClient = WebViewClient()
        //intent.extras?.getString("url")?.let { webView.loadUrl(it) }

        webView.webViewClient = object : WebViewClient() {
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                urlDownload = url
                if (!triggered){
                    triggered = true
                    if(hasWriteStoragePermission()){
                        // setDownloadListener()
                        DownloadAsync(
                            this@DownloadActivity,
                            URLUtil.guessFileName(url, null, null),
                            url, destDir
                        ).execute(URL(url))
                    }else{
                        requestCallPhonePermission()
                    }
                }
                return false
            }
        }
    }

    // request the permission
    private val requestPermissionLauncher = registerForActivityResult( ActivityResultContracts.RequestPermission() ) { isGranted: Boolean ->
        if (isGranted) {
            //setDownloadListener()
            binding?.webView?.loadUrl(urlDownload)
        } else {
            Toast.makeText( this,
                "Write External Storage permission allows us to save files. Please allow this permission in App Settings.",
                Toast.LENGTH_LONG
            ).show()
            triggered = false
        }
    }


    // verify if it has permission
    private fun hasWriteStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }


    private fun requestCallPhonePermission(){
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    companion object{
        class DownloadAsync constructor(activity: DownloadActivity, private val fileName: String, private val url: String, private val destDir: String): AsyncTask<URL, Int, Unit>(){

            private val weakRef: WeakReference<DownloadActivity> = WeakReference(activity)


            @Deprecated("Deprecated in Java")
            override fun doInBackground(vararg urls: URL?) {

                val request = DownloadManager.Request(Uri.parse(url))
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) //Notify client once download is completed!

                val fileName = URLUtil.guessFileName(url, null, null)
                request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS, fileName )
                val dm = weakRef.get()?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                dm.enqueue(request)

                val path ="${Environment.DIRECTORY_DOWNLOADS}/${fileName}"

                println("Fisis This is the path name: $path")
                println("Fisis This is the new dir name: $destDir")

                if (Util.unzip(File(path), File(destDir))){
                    println("it was successful hallåja")
                } else {
                    println("it was NOT successful hallåja")
                }



                //File(path).let { sourceFile ->
                //    sourceFile.copyTo(File("C:/Users/sampleuser/Documents/test.txt"))
                //    sourceFile.delete()
                //}

                /* urls.first()?.let{ url ->

                     try{
                         val connection = url.openConnection().also { it?.connect() }
                         val length = connection?.contentLength
                         val input = BufferedInputStream(url.openStream(),100*1024)
                         val data =  ByteArray(1024)


                         val root = Environment.getExternalStorageDirectory().toString()

                         val output =  FileOutputStream(root+"/"+fileName)
                         println("DownloadActivity doInBackground root: ${root+"/"+fileName}")

                         var count = 0
                         var total = 0
                         while ( count != -1){
                             count = input.read(data)
                             total += count
                             output.write(data, 0, count)
                         }

                         output.flush()
                         output.close()
                         input.close()
                     }catch (e: Exception){
                         println("DownloadActivity doInBackground exception: ${e.message}")
                     }*/



                /* try{
                     Thread.sleep(3000)
                 }catch (e :Exception){
                     e.printStackTrace()
                 }*/
            }

            @Deprecated("Deprecated in Java")
            override fun onPreExecute() {
                super.onPreExecute()
                weakRef.get()?.binding?.customPb?.setTitle(fileName)
                weakRef.get()?.binding?.customPb?.isVisible = true
            }

            @Deprecated("Deprecated in Java")
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)

                weakRef.get()?.binding?.customPb?.isVisible = false
                weakRef.get()?.let{
                    it.triggered = false
                    Toast.makeText(it,"Download Completed", Toast.LENGTH_SHORT).show()
                }
            }

            @Deprecated("Deprecated in Java",
                ReplaceWith("super.onProgressUpdate(*values)", "android.os.AsyncTask")
            )
            override fun onProgressUpdate(vararg values: Int?) {
                super.onProgressUpdate(*values)
            }

        }
    }


    /**
     * Register the download-listener.
     */
    /*private fun registerDownLoadListener(){
        checkDownloadPermission()
        // CHECK AND MANAGE THE DOWNLOADS BEING MADE
        webView.setDownloadListener { url, userAgent, contentDisposition, mimetype, contentLength ->

            GlobalScope.launch {
                delay(20)
                Log.d(TAG, "Coroutine Hello from thread ${Thread.currentThread().name}")
            }
        }
    }*/

    /*private fun checkDownloadPermission() {
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


    /*
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
    }*/

    /*private fun unzipFiles(string: String){

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
    }*/*/
}
