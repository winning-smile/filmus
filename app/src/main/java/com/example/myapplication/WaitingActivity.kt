package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject
import java.io.Serializable
import java.net.HttpURLConnection
import java.net.URL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class WaitingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_waiting)

        val returnButton = findViewById<Button>(R.id.returnWButton)
        val createRoomButton = findViewById<Button>(R.id.startFilmingButton)

        val apiToken = "6a0de868-081f-4100-b6a2-d8aa65dd3296"
        val url = "https://kinopoiskapiunofficial.tech/api/v2.2/films?"
        val genreValue = intent.getStringExtra("genreValue")
        val sortValue = intent.getStringExtra("sortValue")
        val quValue = intent.getStringExtra("quanityValue")
        var filmList = mutableListOf<Film>()

        returnButton.setOnClickListener{ finish() }
        fun getRequest() = runBlocking {
            filmList = searchFilms(apiToken, url, genreValue, sortValue)
        }

        getRequest()

        createRoomButton.setOnClickListener {
            val intent = Intent(this, FilmActivity::class.java)
            intent.putExtra("filmList", filmList as Serializable?)
            for (film in filmList){
                Log.d("FILM_LIST", film.title)
            }
            startActivity(intent)
        }
    }

    suspend fun searchFilms(apiToken: String, url: String?, genre : String?, sortType : String?):  MutableList<Film> = withContext(Dispatchers.IO) {
        val genreMap = mapOf("триллер" to 1,
            "драма" to "2",
            "криминал" to "3",
            "мелодрама" to "4",
            "детектив" to "5",
            "фантастика" to "6",
            "приключения" to "7",
            "биография" to "8",
            "фильм-нуар" to "9",
            "вестерн" to "10",
            "боевик" to "11",
            "фэнтези" to "12",
            "комедия" to "13",
            "военный" to "14",
            "история" to "15",
            "музыка" to "16",
            "ужасы" to "17",
            "мультфильм" to "18",
            "семейный" to "19",
            "мюзикл" to "20",
            "спорт" to "21",
            "документальный" to "22",
            "короткометражка" to "23",
            "аниме" to "24")
        val filmList = mutableListOf<Film>()
        val actualGenre = genreMap[genre]
        val targetUrl = URL("$url&genres=$actualGenre&order=$sortType&type=FILM&page=1")

        val connection = targetUrl.openConnection() as HttpURLConnection
        connection.setRequestProperty("X-API-KEY", apiToken)
        connection.requestMethod = "GET"

        // val responseCode = connection.responseCode
        // Log.w("respondeCode", responseCode.toString())
        val responseBody = connection.inputStream.bufferedReader().use { it.readText() }
        val json = JSONObject(responseBody).getJSONArray("items")

        for (i in 0..<json.length()) {
            val movie = json.getJSONObject(i)
            filmList += Film(title=movie.getString("nameRu"), rating = movie.getInt("ratingKinopoisk"), year = movie.getInt("year"), posterUrl = movie.getString("posterUrl"))
        }
        filmList
    }
}

