package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.Serializable
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.*

data class Params(val genre:String?, val sortType:String?)
data class Response(val id:Int, val name:String, val rate:Float, val rateV2:Float, val year:Int, val posterUrl:String)

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
        val filmList = mutableListOf<Film>()
        val searchParams = Params(genreValue, sortValue)
        val gson = Gson()
        val json = gson.toJson(searchParams)
        val conn = SocketHandler



        runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)
            val job = scope.launch {
                // ПОДКЛЮЧАЕМСЯ
                conn.connectSocket()
                // ПОЛУЧАЕМ КОД ДЛЯ ПОДКЛЮЧЕНИЯ
                val code = conn.reader?.readLine()
                if (code != null) {
                    Log.e("conn code", code)
                }
                codeText.text = code

                // ОТПРАВЛЯЕМ ДАННЫЕ ДЛЯ ЗАПРОСА
                conn.writer?.writeUTF(json)
                conn.writer?.flush()

                // ПОЛУЧАЕМ ФИЛЬМЫ
                val regex = Regex("\\{.*?\\}")
                val data = conn.reader?.readLine()

                if (data != null) {
                    regex.findAll(data).forEach { result ->
                        val film = gson.fromJson(result.value, Response::class.java)
                        filmList += Film(fId=film.id, title=film.name, rating = film.rate,
                            ratingV2 = film.rateV2, year = film.year,
                            posterUrl = "https://kinopoiskapiunofficial.tech/images/posters/kp/"+ film.id +".jpg")

                    }
                }
            }
        }

        createRoomButton.setOnClickListener {
            val intent = Intent(this, FilmActivity::class.java)
            intent.putExtra("filmList", filmList as Serializable?)
            for (film in filmList){
                Log.d("FILM_LIST", film.posterUrl)
            }
            startActivity(intent)
        }
    }
}

