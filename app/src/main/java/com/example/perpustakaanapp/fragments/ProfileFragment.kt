package com.example.perpustakaanapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.perpustakaanapp.LoginActivity
import com.example.perpustakaanapp.Method
import com.example.perpustakaanapp.R
import com.example.perpustakaanapp.databinding.FragmentProfileBinding
import com.example.perpustakaanapp.models.Profile
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ProfileFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        super.onStart()
        if(Method.login){
            binding.withLogin.visibility = View.VISIBLE
            binding.notLogin.visibility = View.GONE
            setupView()
        } else {
            binding.withLogin.visibility = View.GONE
            binding.notLogin.visibility = View.VISIBLE
        }
        setupListener()
    }

    private fun setupListener(){
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this.context, LoginActivity::class.java))
        }
    }

    private fun setupView(){
        Method.api.getProfile().enqueue(object: Callback<Profile>{
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if(response.isSuccessful){
                    binding.name.text = response.body()!!.user.name
                    binding.email.text = "Email : ${response.body()!!.user.email}"
                    binding.phone.text = "Phone : ${response.body()!!.user.phoneNumber}"
                    Glide.with(this@ProfileFragment)
                        .load(GlideUrl("${Method.baseImg}${response.body()!!.user.image}", LazyHeaders.Builder().addHeader("Authorization", Method.token.toString()).build()))
                        .override(100, 100)
                        .error(R.drawable.ic_baseline_account_circle_24)
                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                        .skipMemoryCache(true)
                        .into(binding.images)
                } else {
                    Method.logout(this@ProfileFragment.requireActivity())
                }
            }
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                Method.logout(this@ProfileFragment.requireActivity())
            }
        })
    }

    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

}