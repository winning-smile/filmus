package com.example.myapplication
import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.Socket


object SocketHandler {
    private var socket: Socket? = null
    var reader: BufferedReader? = null
    var writer: DataOutputStream? = null

    fun connectSocket() {
        if (socket == null) {
            socket = Socket("192.168.0.12", 50000)
            reader = BufferedReader(InputStreamReader(socket!!.getInputStream()))
            writer = DataOutputStream(socket!!.getOutputStream())
        }
    }

    fun closeSocket() {
        writer?.close()
        reader?.close()
        socket?.close()
    }
}