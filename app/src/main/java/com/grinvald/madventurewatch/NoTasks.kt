package com.grinvald.madventurewatch

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.grinvald.madventurewatch.common.CacheHelper
import com.grinvald.madventurewatch.databinding.ActivityAuthBinding
import com.grinvald.madventurewatch.databinding.ActivityNoTasksBinding

class NoTasks : Activity() {

    private lateinit var binding: ActivityNoTasksBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNoTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvLogout.setOnClickListener(View.OnClickListener {
            CacheHelper(this).removeToken()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        })

    }
}