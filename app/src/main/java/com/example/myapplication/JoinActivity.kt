package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Serializable
import java.net.Socket

val codeDict = mapOf('A' to '0', 'B' to '1', 'C' to '2', 'D' to '3', 'E' to '4',
    'F' to '5', 'G' to '6', 'H' to '7', 'I' to '8', 'J' to '9')

class JoinActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_join)

        val gson = Gson()
        var filmList = mutableListOf<Film>()

        val returnButton = findViewById<Button>(R.id.returnJButton)
        val joinFilmButton = findViewById<Button>(R.id.joinIButton)
        val codeJoinField = findViewById<TextView>(R.id.codeJoinField)
        val joinCode = codeJoinField.text
        var test = 50000

       // for (i in joinCode.indices){
        //    test += codeDict[joinCode[i]]
        //}//



        returnButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        joinFilmButton.setOnClickListener {
            runBlocking {
                val scope = CoroutineScope(Dispatchers.IO)
                val job = scope.launch {
                    // ПОДКЛЮЧАЕМСЯ
                    val socket = Socket("192.168.0.12", test.toInt())
                    val reader = BufferedReader(InputStreamReader(socket.getInputStream()))

                    // ПОЛУЧАЕМ ФИЛЬМЫ
                    val regex = Regex("\\{.*?\\}")
                    val data = reader.readLine()

                    regex.findAll(data).forEach { result ->
                        val film = gson.fromJson(result.value, Response::class.java)
                        filmList += Film(fId=film.id, title=film.name, rating = film.rate,
                            ratingV2 = film.rateV2, year = film.year,
                            posterUrl = "https://kinopoiskapiunofficial.tech/images/posters/kp/"+ film.id +".jpg")

                    }
                    reader.close()
                    socket.close()
                }
            }

            val intent = Intent(this, FilmActivity::class.java)
            intent.putExtra("filmList", filmList as Serializable?)
            for (film in filmList){
                Log.d("FILM_LIST", film.posterUrl)
            }
            startActivity(intent)
        }
    }
}