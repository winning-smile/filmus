package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val createButton = findViewById<Button>(R.id.createMainButton)
        val joinButton = findViewById<Button>(R.id.joinMainButton)

        createButton.setOnClickListener{
            val intent = Intent(this, Create_Activity::class.java)
            startActivity(intent)
        }

        joinButton.setOnClickListener{
            val intent = Intent(this, Activity_Join::class.java)
            startActivity(intent)
        }
    }
}