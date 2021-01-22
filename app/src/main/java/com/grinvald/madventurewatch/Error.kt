package com.grinvald.madventurewatch

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.grinvald.madventurewatch.databinding.ActivityErrorBinding

class Error : Activity() {

    private lateinit var binding: ActivityErrorBinding
    private lateinit var extras: Bundle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityErrorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        extras = intent.extras!!

        val error_description = extras.getString("description")
        binding.tvErrorDescription.text = error_description

        binding.tvTryAgain.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, Auth::class.java)
            startActivity(intent)
            finish()
        })



    }
}