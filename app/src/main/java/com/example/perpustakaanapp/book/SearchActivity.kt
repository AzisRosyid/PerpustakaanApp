package com.example.perpustakaanapp.book

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.perpustakaanapp.ApiRetrofit
import com.example.perpustakaanapp.Method
import com.example.perpustakaanapp.R
import com.example.perpustakaanapp.adapters.BookAdapter
import com.example.perpustakaanapp.databinding.ActivitySearchBinding
import com.example.perpustakaanapp.models.Book
import com.example.perpustakaanapp.models.Books
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchBinding
    private val api by lazy {ApiRetrofit().apiEndPoint}
    lateinit var bookAdapter: BookAdapter

    var search: String =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupList()
        search = intent.getStringExtra("search").toString()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        getBook()
    }

    private fun setupList(){
        bookAdapter = BookAdapter(arrayListOf(), object: BookAdapter.OnClickListener{
            override fun onClick(book: Book) {
                startActivity(Intent(this@SearchActivity, DetailBookActivity::class.java).putExtra("Id", book.id))
            }
        })
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(applicationContext, 2)
            adapter = bookAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)/*
        val searchItem = menu!!.findItem(R.id.search)
        val searchView = searchItem.actionView as SearchView
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean {
                search = p0.toString()
                onStart()
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                return true
            }
        })
        searchView.queryHint = "Search Books..."
        searchView.isIconifiedByDefault = false
        searchView.setMaxWidth(Integer.MAX_VALUE)
        menu.findItem(R.id.logout).setVisible(false)*/
        return super.onCreateOptionsMenu(menu)
    }

    private fun getBook(){
        api.getBooks(search, "Lite").enqueue(object : Callback<Books>{
            override fun onResponse(call: Call<Books>, response: Response<Books>) {
                if(response.isSuccessful){
                    bookAdapter.setData(response.body()!!.books)
                }
            }
            override fun onFailure(call: Call<Books>, t: Throwable) {
                Method.message("Bad Request 400", this@SearchActivity, false)
            }
        })
    }
}