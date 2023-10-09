package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jaredrummler.materialspinner.MaterialSpinner


class Create_Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create)

        val returnButton = findViewById<Button>(R.id.returnCButton)

        returnButton.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val genreSpinner = findViewById<View>(R.id.genreSpinner) as MaterialSpinner
        genreSpinner.setItems("Аниме", "Биография", "Боевик", "Вестерн", "Военный", "Детектив", "Документальный", "Драма", "Комедия", "Криминал", "Мелодрама", "Мультфильм", "Приключения", "Спорт", "Триллер", "Ужасы", "Фантастика")

        val sortSpinner = findViewById<View>(R.id.sortSpineer) as MaterialSpinner
        sortSpinner.setItems("Рейтинг", "Год выпуска")
        val quanitySpinner = findViewById<View>(R.id.quanitySpinner) as MaterialSpinner
        quanitySpinner.setItems("10", "25", "50", "75", "100")

    }
}
