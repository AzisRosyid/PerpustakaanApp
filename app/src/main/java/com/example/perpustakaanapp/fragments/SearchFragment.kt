package com.example.perpustakaanapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.perpustakaanapp.ApiRetrofit
import com.example.perpustakaanapp.Method
import com.example.perpustakaanapp.adapters.BookAdapter
import com.example.perpustakaanapp.book.DetailBookActivity
import com.example.perpustakaanapp.databinding.FragmentSearchBinding
import com.example.perpustakaanapp.models.Book
import com.example.perpustakaanapp.models.Books
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    private val api by lazy { ApiRetrofit().apiEndPoint }
    lateinit var bookAdapter: BookAdapter
    private var page = 1
    private var totalPage = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        setupListener()
        setupList()
        getBook()
        page = 1
    }

    private fun setupListener(){
        binding.search.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                onStart()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return true
            }
        })
        val close = binding.search.findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)
        close.setOnClickListener {
            binding.search.setQuery("", false)
            onStart()
            binding.search.clearFocus()
        }
        binding.recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!binding.recyclerView.canScrollVertically(1) && page <= totalPage){
                    page += 1
                    getBook()
                }
            }
        })
    }

    private fun setupList(){
        bookAdapter = BookAdapter(arrayListOf(), object : BookAdapter.OnClickListener{
            override fun onClick(book: Book) {
                startActivity(Intent(this@SearchFragment.requireContext(), DetailBookActivity::class.java).putExtra("Id", book.id))
            }
        })
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@SearchFragment.requireContext(), 2)
            adapter = bookAdapter
        }
    }

    private fun getBook(){
        api.getBooks(search = binding.search.query.toString(), page = page).enqueue(object: Callback<Books>{
            override fun onResponse(call: Call<Books>, response: Response<Books>) {
                if(response.isSuccessful){
                    bookAdapter.setData(response.body()!!.books)
                    totalPage = response.body()!!.totalPages
                } else {
                    bookAdapter.setData(arrayListOf())
                }
            }

            override fun onFailure(call: Call<Books>, t: Throwable) {
                Method.error(t.message.toString(), this@SearchFragment.requireActivity())
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

}