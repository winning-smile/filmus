package com.example.myapplication

import java.io.Serializable

data class Film(val id: Long = counter++, val title: String, val rating: Int, val year: Int, val posterUrl: String) : Serializable {
    companion object {
        private var counter = 0L
    }
}