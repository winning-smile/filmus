package com.example.myapplication

import java.io.Serializable

data class Film(val id: Long = counter++, val fId: Int, val title: String, val rating: Float, val ratingV2: Float, val year: Int, val posterUrl: String) : Serializable {
    companion object {
        private var counter = 0L
    }
}