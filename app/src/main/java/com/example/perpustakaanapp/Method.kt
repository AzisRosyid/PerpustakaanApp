package com.example.perpustakaanapp

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Environment

object Method {

    val api by lazy { ApiRetrofit().apiEndPoint }

    // Profile
    var id: Int? = null
    var login = false
    var token: String? = null

    val baseFile = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS + "/PerpustakaanApi/").toString()
    val baseUrl = "http://192.168.21.1:8021/api/"
    val baseImg = "${baseUrl}Users/UserImage/"

    fun message(message: String, activity: Activity, st: Boolean = false){
        val alertDialog = AlertDialog.Builder(activity)
            .setTitle("Message")
            .setMessage(message)
            .setPositiveButton("OK"){s, t ->
                if(st){
                    activity.finish()
                }
            }
        alertDialog.show()
    }

    fun error(error: String, activity: Activity){
        val alertDialog = AlertDialog.Builder(activity)
            .setTitle("Error!")
            .setMessage(error)
            .setPositiveButton("OK"){s, t ->
            }
        alertDialog.show()
    }

    fun logout(activity: Activity){
        if(login){
            val alertDialog = AlertDialog.Builder(activity)
                .setTitle("Message")
                .setMessage("Token already expired!, go back to Login?")
                .setPositiveButton("Yes"){s, t->
                    activity.startActivity(Intent(activity, LoginActivity::class.java))
                    activity.finish()
                }
                .setNegativeButton("No") { s, t ->
                    activity.startActivity(Intent(activity, MainActivity::class.java))
                    activity.finish()
                }
            id = null
            token = null
            login = false
            alertDialog.show()
        }
    }
}