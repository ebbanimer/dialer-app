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
    var triggered = false
    private lateinit var urlDownload : String
    private lateinit var downLoadAsync : AsyncTask<URL, Int, Unit>

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

        webView.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.R)
            @Deprecated("Deprecated in Java")
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                urlDownload = url
                if (!triggered){
                    triggered = true
                    if(hasWriteStoragePermission() && hasReadStoragePermission()){
                        downLoadAsync = DownloadAsync(
                            this@DownloadActivity,
                            URLUtil.guessFileName(url, null, null),
                            url, destDir
                        ).execute(URL(url))
                        mProgressDialog?.show();
                    }else{
                        requestWriteStoragePermission()
                        requestReadStoragePermission()
                    }
                }
                return false
            }
        }
    }

    // request the permission
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

    // verify if it has permission
    private fun hasWriteStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun hasReadStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(this,
            Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestWriteStoragePermission(){
        requestPermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    private fun requestReadStoragePermission(){
        requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    companion object{

        private var mProgressDialog: ProgressDialog? = null

        class DownloadAsync constructor(activity: DownloadActivity, private val fileName: String, private val url: String, private val destDir: String): AsyncTask<URL, Int, Unit>(){
            private val weakRef: WeakReference<DownloadActivity> = WeakReference(activity)
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
                        val connection = url.openConnection().also { it?.connect() }
                        val length = connection?.contentLength
                        val input = BufferedInputStream(url.openStream(), 100 * 1024)
                        val data = ByteArray(1024)

                        val output = FileOutputStream("$root/Download/$fileName")
                        println("DownloadActivity doInBackground root: ${"$root/Download/$fileName"}")

                        var count = 0   // how much it downloads right now
                        var total = 0   // how much it has totally downloaded

                        // until the count amount is above -1
                        while (count > -1) {
                            count = input.read(data)
                            if (count == -1){
                                break
                            }

                            total += count
                            if (length != null) {
                                if (length > 0){
                                    publishProgress((total * 100) / length)
                                }
                            }
                            output.write(data, 0, count)
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
                    /*var input: InputStream? = null
                var output: OutputStream? = null
                var connection: HttpURLConnection? = null

                try {
                    connection = urls.openConnection() as HttpURLConnection
                    connection.connect()

                    if (connection.responseCode != HttpURLConnection.HTTP_OK){
                        return
                    }

                    val fileLength: Int = connection.contentLength
                    input = connection.inputStream
                    output = FileOutputStream(Environment.getExternalStorageDirectory().toString() +"/Download/" + fileName)

                    val data = ByteArray(4096)
                    var total: Long = 0
                    var count: Int

                    while ((count = input.read(data)) != -1) {
                        // allow canceling with back button
                        if (isCancelled()) {
                            input.close();
                            return
                        }
                        total += count;
                        // publishing the progress....
                        if (fileLength > 0) // only if total length is known
                            publishProgress((total * 100 / fileLength).toInt())
                        output.write(data, 0, count)

                        val path = Environment.getExternalStorageDirectory().toString() +"/Download/" + fileName

                        if (Util.unzip(File(path), File(destDir))){
                            println("it was successful hallåja")
                        } else {
                            println("it was NOT successful hallåja")
                        }
                    }
                } catch (e :Exception){
                    e.printStackTrace()
                } finally {
                    try {
                        if (output != null)
                            output.close();
                        if (input != null)
                            input.close();
                    } catch (ignored: IOException) {
                    }
                    connection?.disconnect()
                }*/

                    /*
                val downloadReference: Long

                val request = DownloadManager.Request(Uri.parse(url))
                request.allowScanningByMediaScanner()
                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED) //Notify client once download is completed!

                request.setDestinationInExternalPublicDir( Environment.DIRECTORY_DOWNLOADS, fileName )
                val dm = weakRef.get()?.getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                downloadReference = dm.enqueue(request)

                val myDownloadQuery = DownloadManager.Query()
                myDownloadQuery.setFilterById(downloadReference)
                val c: Cursor = dm.query(myDownloadQuery)

                if (c.moveToFirst()) {
                    val totalSizeIndex: Int =
                        c.getColumnIndex(DownloadManager.COLUMN_TOTAL_SIZE_BYTES)
                    val downloadedIndex: Int =
                        c.getColumnIndex(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR)
                    val totalSize: Int = c.getInt(totalSizeIndex)
                    val downloaded: Int = c.getInt(downloadedIndex)
                    if (totalSize != -1) {
                        mProgressStatus = ((downloaded * 100.0 / totalSize).toInt())
                    }
                    println("hej in do background: " + mProgressStatus)
                    publishProgress(mProgressStatus)
                }

                val path = Environment.getExternalStorageDirectory().toString() +"/Download/" + fileName

                if (Util.unzip(File(path), File(destDir))){
                    println("it was successful hallåja")
                } else {
                    println("it was NOT successful hallåja")
                }

                Thread.sleep(3000)*/

            }
            @RequiresApi(Build.VERSION_CODES.O)
            @Deprecated("Deprecated in Java")
            override fun onPreExecute() {
                super.onPreExecute()
                mProgressDialog?.setTitle(R.string.download_title)
                mProgressDialog?.setMessage(fileName)
                mWakeLock =
                    (context.getSystemService(POWER_SERVICE) as PowerManager).run {
                        newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, javaClass.name).apply {
                            acquire(10*60*1000L /*10 minutes*/)
                        }
                    }
                mProgressDialog?.show()
                //mProgressStatus = 0

            /*weakRef.get()?.binding?.customPb?.setTitle(fileName)
            weakRef.get()?.binding?.customPb?.setProgress(progress)
            weakRef.get()?.binding?.customPb?.isVisible = true*/
                //println("in pre-execute hej $mProgressStatus")
            }

            @Deprecated("Deprecated in Java")
            override fun onProgressUpdate(vararg values: Int?) {
                super.onProgressUpdate(*values)
                mProgressDialog?.setIndeterminate(false)
                mProgressDialog?.setMax(100)
                values[0]?.let { mProgressDialog?.setProgress(it) }
                //mProgressDialog?.setProgress(mProgressStatus)
                /*weakRef.get()?.binding?.customPb?.setTitle(fileName)
                values[0]?.let { weakRef.get()?.binding?.customPb?.setProgress(it) }
                weakRef.get()?.binding?.customPb?.isVisible = true
                println("in progress update hej " + values[0])*/
            }


            @Deprecated("Deprecated in Java")
            override fun onPostExecute(result: Unit?) {
                super.onPostExecute(result)
                mWakeLock.release()
                mProgressDialog?.dismiss()

                if (Util.unzip(File("$root/Download/$fileName"), File(destDir))){
                    Toast.makeText(context,R.string.download_success, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context,R.string.download_fail, Toast.LENGTH_LONG).show()
                }

                /*mProgressDialog?.setProgress(100)
                //mProgressDialog?.dismiss();

                mProgressDialog?.dismiss()

                //weakRef.get()?.binding?.customPb?.isVisible = false
                weakRef.get()?.let{
                    it.triggered = false
                    Toast.makeText(it,"Download Completed", Toast.LENGTH_SHORT).show()
                }*/
            }

            /*@RequiresApi(Build.VERSION_CODES.O)
            @Deprecated("Deprecated in Java",
                ReplaceWith("super.onProgressUpdate(*values)", "android.os.AsyncTask")
            )
            override fun onProgressUpdate(vararg values: Int?) {
                super.onProgressUpdate(*values)
                mProgressDialog?.setProgress(mProgressStatus)
                mProgressDialog?.setIndeterminate(false);
                mProgressDialog?.setMax(100)
                values[0]?.let { mProgressDialog?.setProgress(it) }
                /*weakRef.get()?.binding?.customPb?.setTitle(fileName)
                values[0]?.let { weakRef.get()?.binding?.customPb?.setProgress(it) }
                weakRef.get()?.binding?.customPb?.isVisible = true
                println("in progress update hej " + values[0])*/
            }*/
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
