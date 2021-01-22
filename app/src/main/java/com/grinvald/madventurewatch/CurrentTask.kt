package com.grinvald.madventurewatch

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.gson.Gson
import com.grinvald.madventurewatch.common.CacheHelper
import com.grinvald.madventurewatch.databinding.ActivityCurrentTaskBinding
import com.grinvald.madventurewatch.databinding.ActivityMainBinding
import com.grinvald.madventurewatch.models.Task
import com.grinvald.madventurewatch.models.TaskDetails
import org.json.JSONObject

class CurrentTask : Activity() {

    private lateinit var binding: ActivityCurrentTaskBinding
    private lateinit var task : TaskDetails

    fun getCurrentTask() {
        val queue = Volley.newRequestQueue(this)
        val request = object: StringRequest(
            Request.Method.GET,
            "http://wsk2019.mad.hakta.pro/api/user/currentTask",
            Response.Listener { response ->
                val jsonObject = JSONObject(response)
                if(jsonObject.has("content")) {

                    val intent = Intent(this, NoTasks::class.java)
                    startActivity(intent)
                    finish()
                }   else {
                    val task : TaskDetails = Gson().fromJson(
                        jsonObject.toString(),
                        TaskDetails::class.java
                    )
                    initTask(task)
                    initHandlers()
                }
            },
            Response.ErrorListener { error ->

            }
        ) {
            override fun getBodyContentType(): String {
                return "application/json"
            }
            override fun getHeaders(): MutableMap<String, String> {
                val map = HashMap<String, String>()
                map.put("Token", CacheHelper(baseContext).getToken())
                return map
            }
        }
        queue.add(request)
    }

    fun initTask(task: TaskDetails) {
        when(task.goalType) {
            "LOCATION" -> {
                binding.ivGoalType.setImageDrawable(getDrawable(R.drawable.type_location))
                binding.tvGoalValue.text = "Find location"
            }
            "SECRET_KEY" -> {
                binding.ivGoalType.setImageDrawable(getDrawable(R.drawable.type_key))
                binding.tvGoalValue.text = "Find key"
            }
            "QR_CODE" -> {
                binding.ivGoalType.setImageDrawable(getDrawable(R.drawable.type_qr))
                binding.tvGoalValue.text = "Find QR-code"
            }
            "STEPS" -> {
                binding.ivGoalType.setImageDrawable(getDrawable(R.drawable.shos))
                binding.tvGoalValue.text = "${task.goalValue} steps"
            }
        }

        this.task = task

        binding.tvDescription.text = task.description

        binding.tvTitle.text = task.name
    }

    fun sendKey(taskId: String, key: String) {
        val queue = Volley.newRequestQueue(this)
        val request = object: StringRequest(Request.Method.PUT, "http://wsk2019.mad.hakta.pro/api/tasks/$taskId/result",
        Response.Listener { response ->

        }, Response.ErrorListener { error ->

            }) {
            override fun getBodyContentType(): String {
                return "application/json"
            }

            override fun getBody(): ByteArray {
                val r = JSONObject()
                r.put("key", key)
                return r.toString().toByteArray()
            }

            override fun getHeaders(): MutableMap<String, String> {
                val headers = HashMap<String, String>()
                headers.put("Token", CacheHelper(baseContext).getToken())
                return headers
            }
        }

        queue.add(request)
    }

    fun initHandlers() {
        binding.tvSendResult.setOnClickListener(View.OnClickListener {
            if(task.goalType == "QR_CODE" || task.goalType == "STEPS") {
                Toast.makeText(this, "Use your phone to finish this task", LENGTH_LONG).show()
            }
            if(task.goalType == "SECRET_KEY") {
                val et = EditText(baseContext)
                val alert = AlertDialog.Builder(this)
                    .setTitle("Enter secret key")
                    .setView(et)
                    .setPositiveButton("Send", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->
                        val key = et.text.toString()
//                        sendKey(, key)
                    })
                    .setNegativeButton("Cancel", DialogInterface.OnClickListener() { dialogInterface: DialogInterface, i: Int ->

                    })
                    .create()
                alert.show()
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCurrentTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        getCurrentTask()


    }
}