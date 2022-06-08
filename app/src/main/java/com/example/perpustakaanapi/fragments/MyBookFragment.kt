package com.example.perpustakaanapi.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.perpustakaanapi.ApiRetrofit
import com.example.perpustakaanapi.LoginActivity
import com.example.perpustakaanapi.Method
import com.example.perpustakaanapi.R
import com.example.perpustakaanapi.adapters.BookAdapter
import com.example.perpustakaanapi.book.DetailBookActivity
import com.example.perpustakaanapi.databinding.FragmentMyBookBinding
import com.example.perpustakaanapi.models.Book
import com.example.perpustakaanapi.models.Books
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyBookFragment : Fragment() {
    private val api by lazy { ApiRetrofit().apiEndPoint }
    lateinit var bookAdapter: BookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        setupListener()
        setupList()
        getBook()
    }

    private fun setupListener(){

    }

    private fun setupList(){
        bookAdapter = BookAdapter(arrayListOf(), object: BookAdapter.OnClickListener{
            override fun onClick(book: Book) {
                startActivity(Intent(this@MyBookFragment.requireContext(), DetailBookActivity::class.java).putExtra("Id", book.id))
            }
        })
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(this@MyBookFragment.requireContext(), 2)
            adapter = bookAdapter
        }
    }

    lateinit var binding: FragmentMyBookBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMyBookBinding.inflate(inflater, container, false)
        return binding.root
    }

    var search = ""

    private fun getBook(){
        api.getBooks(user = Method.id, search = search, content = "Lite").enqueue(object: Callback<Books> {
            override fun onResponse(call: Call<Books>, response: Response<Books>) {
                if(response.isSuccessful){
                    bookAdapter.setData(response.body()!!.books)
                } else {
                    val error = JSONObject(response.errorBody()!!.string())
                    Method.error(error.getString("errors"), this@MyBookFragment.requireActivity())
                }
            }
            override fun onFailure(call: Call<Books>, t: Throwable) {
                Method.error(t.message.toString(), this@MyBookFragment.requireActivity())
            }
        })
    }
}