package com.example.perpustakaanapi

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.perpustakaanapi.databinding.ActivityLoadingBinding
import com.example.perpustakaanapi.models.Profile
import com.example.perpustakaanapi.models.Token
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoadingActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoadingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoadingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
    }

    override fun onStart() {
        super.onStart()
        if(Method.token == null){
            val app = this.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
            Method.token = app.getString("Token", "").toString()
        }
        Method.api.getProfile().enqueue(object: Callback<Profile> {
            override fun onResponse(call: Call<Profile>, response: Response<Profile>) {
                if(response.isSuccessful) {
                    Method.id = response.body()!!.user.id.toInt()
                    Method.login = true
                    startActivity(Intent(this@LoadingActivity, MainActivity::class.java))
                    this@LoadingActivity.finish()
                } else {
                    logout(this@LoadingActivity)
                }
            }
            override fun onFailure(call: Call<Profile>, t: Throwable) {
                logout(this@LoadingActivity)
            }
        })
    }

    private fun logout(activity: Activity){
        val app = activity.getSharedPreferences("MyApp", Context.MODE_PRIVATE)
        app.edit().clear().apply()
        Method.token = null
        Method.id = null
        Method.login = false
        startActivity(Intent(activity, LoginActivity::class.java).putExtra("start", true))
        activity.finish()
    }
}