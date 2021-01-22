package com.grinvald.madventurewatch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import com.grinvald.madventurewatch.common.CacheHelper
import com.grinvald.madventurewatch.databinding.ActivityMainBinding

class MainActivity : Activity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(CacheHelper(this).getToken().equals("null")) {
            binding.tvSignin.setOnClickListener(View.OnClickListener {
                val intent = Intent(this, Auth::class.java)
                startActivity(intent)
            })
        }   else {
            Log.d("DEBUG", "token:  ${CacheHelper(this).getToken()}")
            val intent = Intent(this, CurrentTask::class.java)
            startActivity(intent)
            finish()
        }


    }
}