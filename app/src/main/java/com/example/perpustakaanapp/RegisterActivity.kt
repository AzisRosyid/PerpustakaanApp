package com.example.perpustakaanapp

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import com.example.perpustakaanapp.databinding.ActivityRegisterBinding
import com.example.perpustakaanapp.models.Response
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import java.io.File

class RegisterActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegisterBinding
    private val api by lazy { ApiRetrofit().apiEndPoint }
    var uri: Uri? = null
    var file: File? = null
    var body: RequestBody? = null

    private val activityResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { s ->
        if (s.resultCode == RESULT_OK) {
            uri = s.data?.data
            var path = ""
            contentResolver.query(uri!!, null, null, null, null)!!.use { cursor ->
                cursor.moveToFirst()
                val column = cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME)
                path = cursor.getString(column)
            }
            file = File(cacheDir, path)
            binding.image.setImageURI(uri)
            body = file!!.asRequestBody(contentResolver.getType(uri!!)!!.toMediaTypeOrNull())
        }
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Register"
        setupList()
        setupListener()
        binding.name.setText("Helsdfjlk")
        binding.email.setText("example@gmail.com")
        binding.password.setText("asdf")
        binding.passwordConfirm.setText("asdf")
        binding.gender.setText("Male")
        binding.date.setText("2003-11-11")
        binding.phone.setText("02394")
        binding.address.setText("sdfa")
    }

    private fun setupList(){
        val adapter = arrayOf("Male", "Female")
        binding.gender.setAdapter(ArrayAdapter(this, R.layout.list_item, adapter))
        binding.gender.setOnItemClickListener(object: AdapterView.OnItemClickListener{
            override fun onItemClick(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }
        })
    }

    private fun setupListener(){
        binding.btnUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            activityResult.launch(intent)
        }
        binding.btnBack.setOnClickListener {
            this.finish()
        }
        binding.btnRegister.setOnClickListener {
            if(binding.name.text.isNullOrEmpty() || binding.email.text.isNullOrEmpty() || binding.password.text.isNullOrEmpty() || binding.gender.text.isNullOrEmpty() || binding.date.text.isNullOrEmpty() || binding.phone.text.isNullOrEmpty() || binding.address.text.isNullOrEmpty()){
                Method.error("All field must be filled!", this)
                return@setOnClickListener
            }
            if(!Patterns.EMAIL_ADDRESS.matcher(binding.email.text).matches()){
                Method.error("Email invalid format!", this)
                return@setOnClickListener
            }
            if(!Patterns.PHONE.matcher(binding.phone.text).matches()){
                Method.error("Phone invalid format!", this)
                return@setOnClickListener
            }
            if(binding.password.text.toString() != binding.passwordConfirm.text.toString()){
                Method.error("Confirm Password does not correct!", this)
            }

            api.register(
                binding.name.text.toString().toRequestBody(),
                binding.email.text.toString().toRequestBody(),
                binding.password.text.toString().toRequestBody(),
                binding.gender.text.toString().toRequestBody(),
                "User".toRequestBody(),
                binding.date.text.toString().toRequestBody(),
                binding.phone.text.toString().toRequestBody(),
                binding.address.text.toString().toRequestBody(),
                if(file != null){
                    MultipartBody.Part.createFormData("image", file!!.name, body!!)
                } else {
                    null
                }
            ).enqueue(object: Callback<Response>{
                override fun onResponse(
                    call: Call<Response>,
                    response: retrofit2.Response<Response>
                ) {
                    if(!response.isSuccessful){
                        val error = JSONObject(response.errorBody()!!.string())
                        Method.error(error.getString("errors"), this@RegisterActivity)
                    } else {
                        Method.message(response.body()!!.messages, this@RegisterActivity,  true)
                    }
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    Method.error(t.message.toString(), this@RegisterActivity)
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == 100) {

        }
    }
}