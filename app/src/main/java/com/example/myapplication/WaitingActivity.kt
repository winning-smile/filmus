package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable
import java.io.DataOutputStream
import java.net.Socket
import com.google.gson.Gson
import java.io.BufferedReader
import java.io.InputStreamReader

data class Params(val genre:String?, val sortType:String?)
data class Response(val id:Int, val name:String, val rate:Float, val rateV2:Float, val year:Int, val posterURL: String)

class WaitingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        // UI элементы
        val returnButton = findViewById<Button>(R.id.returnWButton)
        val codeText = findViewById<TextView>(R.id.codeTextView)
        val createRoomButton = findViewById<Button>(R.id.startFilmingButton)

        returnButton.setOnClickListener{ finish() }

        // Переменные для запроса
        val genreValue = intent.getStringExtra("genreValue")
        val sortValue = intent.getStringExtra("sortValue")
        val quValue = intent.getStringExtra("quanityValue")
        var filmList = mutableListOf<Film>()
        val searchParams = Params(genreValue, sortValue)
        val gson = Gson()
        val json = gson.toJson(searchParams)

        // ПОДКЛЮЧАЕМСЯ
        val socket = Socket("192.168.0.12", 50000)

        // ПОЛУЧАЕМ КОД ДЛЯ ПОДКЛЮЧЕНИЯ
        val reader = BufferedReader(InputStreamReader(socket.getInputStream()))
        val code = reader.readLine()
        Log.e("conn code", code)

        codeText.text = code

        // ОТПРАВЛЯЕМ ДАННЫЕ ДЛЯ ЗАПРОСА
        val dataOutputStream = DataOutputStream(socket.getOutputStream())
        dataOutputStream.writeUTF(json)
        dataOutputStream.flush()

        // ПОЛУЧАЕМ ФИЛЬМЫ
        val films = mutableListOf<Response>()
        val regex = Regex("\\{.*?\\}")
        val data = reader.readLine()

        regex.findAll(data).forEach { result ->
            val film = gson.fromJson(result.value, Response::class.java)
            films.add(film)
            filmList += Film(fId=film.id, title=film.name, rating = film.rate, ratingV2 = film.rateV2, year = film.year, posterUrl = film.posterURL)
        }

        films.clear()

        dataOutputStream.close()
        reader.close()
        socket.close()

        createRoomButton.setOnClickListener {
            val intent = Intent(this, FilmActivity::class.java)
            intent.putExtra("filmList", filmList as Serializable?)
            for (film in filmList){
                Log.d("FILM_LIST", film.title)
            }
            startActivity(intent)
        }
    }
}

