package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity


class CreateActivity : AppCompatActivity() {

    private lateinit var currentLayout: LinearLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        currentLayout = findViewById(R.id.createLayout)

        val returnButton = findViewById<Button>(R.id.returnCButton)
        val createRoomButton = findViewById<Button>(R.id.continueButton)
        val addGenreButton = findViewById<Button>(R.id.addGenreButton)

        addGenreButton.setOnClickListener { addSpinner() }

        returnButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        createRoomButton.setOnClickListener {
            val soloCheck = findViewById<CheckBox>(R.id.soloCheckBox)

            if (soloCheck.isChecked){
                val intent = Intent(this, FilmActivity::class.java)
                startActivity(intent)
            }
            else {
                val intent = Intent(this, WaitingActivity::class.java)
                val genreValue: String = findViewById<Spinner>(R.id.genreSpinner).selectedItem.toString()
                val sortValue: String = findViewById<Spinner>(R.id.sortSpinner).selectedItem.toString()
                val quanityValue: String = findViewById<Spinner>(R.id.quanitySpinner).selectedItem.toString()
                intent.putExtra("genreValue", genreValue);
                intent.putExtra("sortValue", sortValue);
                intent.putExtra("quanityValue", quanityValue);
                Log.d("GENREVALUE", genreValue)
                startActivity(intent)
            }
        }
    }


    private fun addSpinner() {
        val spinner = Spinner(this)
        val spinnerItems = getResources().getStringArray(R.array.genreItems)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override  fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedItem = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        currentLayout.addView(spinner, 2) // Добавление Spinner в контейнер
    }
}
