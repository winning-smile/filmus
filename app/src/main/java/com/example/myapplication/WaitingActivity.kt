package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class WaitingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        val returnButton = findViewById<Button>(R.id.returnWButton)
        val createRoomButton = findViewById<Button>(R.id.startFilmingButton)


        returnButton.setOnClickListener{ finish() }

        createRoomButton.setOnClickListener {
            val intent = Intent(this, FilmActivity::class.java)
            startActivity(intent)
        }
    }
}