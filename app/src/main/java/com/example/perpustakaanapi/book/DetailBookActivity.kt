package com.example.perpustakaanapi.book

import android.Manifest
import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.webkit.WebView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.perpustakaanapi.ApiRetrofit
import com.example.perpustakaanapi.Method
import com.example.perpustakaanapi.R
import com.example.perpustakaanapi.databinding.ActivityDetailBookBinding
import com.example.perpustakaanapi.models.Book
import com.example.perpustakaanapi.models.BookId
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.lang.Exception
import java.net.URI

class DetailBookActivity : AppCompatActivity() {

    lateinit var binding: ActivityDetailBookBinding
    private val api by lazy { ApiRetrofit().apiEndPoint }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBookBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Detail Book"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupListener()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        binding.progressBar.visibility = View.VISIBLE
        getBook()
    }

    var download = ""
    var name = ""

    @SuppressLint("IntentReset")
    private fun setupListener(){
        binding.btnDownload.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(applicationContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 2)
                return@setOnClickListener
            } else if (File(Method.baseFile , download).exists()) {
                val intent = Intent(Intent.ACTION_VIEW, Uri.fromFile(File(Method.baseFile + download))).setType("application/pdf").setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION).setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                startActivity(Intent.createChooser(intent, "Open File"))
            } else {
                downloadBook()
            }
        }
    }

    private fun downloadBook(){
        val uri = Uri.parse(Method.baseUrl + "Books/DownloadBook/" + download)
/*            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS).path*/
        val request = DownloadManager.Request(uri)
            .setMimeType("application/pdf")
            .setTitle(name)
            .setDescription("Downloading...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setAllowedOverMetered(true)
            .setAllowedOverRoaming(false)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS , "PerpustakaanApi/" + download)
        /*.setDestinationInExternalFilesDir(this, dir, name)*/

        val dwn = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        dwn.enqueue(request)
    }

    private fun getBook(){
        api.getBook(intent.getStringExtra("Id").toString()).enqueue(object: Callback<BookId>{
            override fun onResponse(call: Call<BookId>, response: Response<BookId>) {
                if(response.isSuccessful){
                    binding.title.text = response.body()!!.book.title
                    binding.publisher.text = response.body()!!.book.publisher
                    binding.author.text = response.body()!!.book.author
                    binding.category.text = response.body()!!.book.category.name
                    var genres = ""
                    if (response.body()!!.book.genres.any()) {
                        for (i in response.body()!!.book.genres){
                            genres += i.name + ", "
                        }
                        genres = genres.substring(0, genres.count() - 2)
                    }
                    binding.genres.text = genres
                    binding.pages.text = response.body()!!.book.page.toString()
                    binding.views.text = response.body()!!.book.viewCount.toString()
                    binding.description.text = response.body()!!.book.description
                    download = response.body()!!.book.download
                    name = response.body()!!.book.title + ".pdf"

                    if(File(Method.baseFile, download).exists()){
                        binding.btnDownload.text = "Open Book"
                    }

                    Glide.with(this@DetailBookActivity)
                        .load(Method.baseUrl + "Books/ImageBook/" + response.body()!!.book.image)
                        .fitCenter()
                        .error(R.drawable.ic_baseline_book_24)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(binding.image)

                    binding.progressBar.visibility = View.GONE
                } else {
                    val st = JSONObject(response.errorBody()!!.string())
                    Method.error(st.getString("errors"), this@DetailBookActivity)
                }
            }

            override fun onFailure(call: Call<BookId>, t: Throwable) {
                Method.error(t.message.toString(), this@DetailBookActivity)
            }
        })
    }
}