package com.example.perpustakaanapi.fragments

import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.perpustakaanapi.ApiRetrofit
import com.example.perpustakaanapi.Method
import com.example.perpustakaanapi.R
import com.example.perpustakaanapi.databinding.FragmentHomeBinding
import com.example.perpustakaanapi.models.Books
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    private val api by lazy { ApiRetrofit().apiEndPoint }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

}