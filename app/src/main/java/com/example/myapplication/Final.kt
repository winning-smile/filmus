package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

data class resFilm(val title:String)

class Final : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_final)
        val layout = findViewById<LinearLayout>(R.id.finalLayout)
        val filmList = recieve()

        for (film in filmList){
            Log.d("FINAL", film.title)
        }

        for (film in filmList){
            Log.d("filmCycle", film.title)
            val textView = TextView(this)
            textView.text = film.title
            textView.textSize = 20f
            textView.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            layout.addView(textView)
        }
    }

    fun recieve(): MutableList<Film> {
        val conn = SocketHandler
        val gson = Gson()
        var filmList = mutableListOf<Film>()
        runBlocking {
            val scope = CoroutineScope(Dispatchers.IO)
            val job = scope.launch {
                // ПОДКЛЮЧАЕМСЯ
                conn.connectSocket()
                // ОТПРАВЛЯЕМ ФЛАГ
                conn.writer.write("final".toByteArray())
                conn.writer.flush()

                // ПОЛУЧАЕМ ФИЛЬМЫ
                val regex = Regex("\\{.*?\\}")
                val data = conn.reader.readLine()

                Log.d("reader conn", data)

                regex.findAll(data).forEach { result ->
                    val film = gson.fromJson(result.value, Response::class.java)
                    filmList += Film(fId=film.id, title=film.name.toString(), rating = film.rate,
                        ratingV2 = film.rateV2, year = film.year,
                        posterUrl = "https://kinopoiskapiunofficial.tech/images/posters/kp/"+ film.id +".jpg")
                    Log.d("FILM NAME", film.name)
                }
            }
        }

        return filmList
    }
}