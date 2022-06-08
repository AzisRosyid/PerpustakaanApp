package com.example.perpustakaanapi

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import com.example.perpustakaanapi.databinding.ActivityLoginBinding
import com.example.perpustakaanapi.models.Profile
import com.example.perpustakaanapi.models.Response
import com.example.perpustakaanapi.models.Token
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Login"
        setupListener()
        binding.email.setText("user@example.com")
        binding.password.setText("string")
    }

    private fun setupListener(){
        binding.btnCancel.setOnClickListener {
            if (intent.getBooleanExtra("start", false)) {
                startActivity(Intent(applicationContext, MainActivity::class.java))
            }
            this.finish()
        }
        binding.btnRegister.setOnClickListener {
            startActivity(Intent(applicationContext, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            if(binding.email.text.isNullOrEmpty() || binding.password.text.isNullOrEmpty()){
                Method.error("All field must be filled!", this)
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(binding.email.text).matches()){
                Method.error("Email invalid format!", this)
                return@setOnClickListener
            }
            val json = JsonObject()
            json.addProperty("email", binding.email.text.toString())
            json.addProperty("password", binding.password.text.toString())
            json.addProperty("android", true)
            Method.api.login(json).enqueue(object : Callback<Token> {
                override fun onResponse(
                    call: Call<Token>,
                    response: retrofit2.Response<Token>
                ) {
                    if (!response.isSuccessful) {
                        val error = JSONObject(response.errorBody()!!.string())
                        Method.error(error.getString("errors"), this@LoginActivity)
                    } else {
                        Method.token = "Bearer ${response.body()!!.token}"
                        startActivity(Intent(applicationContext, LoadingActivity::class.java))
                        finish()
                    }
                }

                override fun onFailure(call: Call<Token>, t: Throwable) {
                    Method.error(t.message.toString(), this@LoginActivity)
                }
            })
        }
    }

}