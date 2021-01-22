package com.grinvald.madventurewatch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.grinvald.madventurewatch.common.CacheHelper
import com.grinvald.madventurewatch.databinding.ActivityAuthBinding
import com.grinvald.madventurewatch.databinding.ActivityMainBinding
import org.json.JSONObject

class Auth : Activity() {

    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvSignin.setOnClickListener(View.OnClickListener {

            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            tryAuth(email, password)

        })
    }

    fun tryAuth(email: String, password: String) {

        val queue = Volley.newRequestQueue(this)
        val request = object: StringRequest(
            Request.Method.POST,
            "http://wsk2019.mad.hakta.pro/api/user/login",
            Response.Listener { response ->
                try {
                    val j = JSONObject(response)

                    if(j.has("token")){
                        CacheHelper(this).saveToken(j.getString("token"))
                        val intent = Intent(this, CurrentTask::class.java)
                        startActivity(intent)
                        finish()
                    }   else {
                        binding.etEmail.text = null
                        binding.etPassword.text = null
                        val intent = Intent(this, Error::class.java)
                        intent.putExtra("description", "Wrong password")
                        startActivity(intent)
                    }

                }   catch (e: Exception) {}
            },
            Response.ErrorListener { error ->

                val intent = Intent(this, Error::class.java)
                intent.putExtra("description", "Api error")
                startActivity(intent)

            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json"
            }

            override fun getBody(): ByteArray {
                val r = JSONObject()
                r.put("email", email)
                r.put("password", password)
                return r.toString().toByteArray()
            }
        }

        queue.add(request)

    }
}