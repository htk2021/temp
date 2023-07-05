package com.example.myapplication6

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.ContentValues.TAG
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val  channelName= "Emoji Party"
    private val channelDescription = "Emoji Party를 위한 채널"
    private val channelId = "채널 ID"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("FCM TOKEN", "Refreshed token: $token")
        println(token)
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // FCM registration token to your app server.
        sendRegistrationToServer(token)
    }
    private fun sendRegistrationToServer(token: String) {
        // 서버로 토큰을 전송하는 코드 작성
        // 이 코드에서는 네트워크 요청을 통해 토큰을 서버로 보낼 수 있습니다.
        // 네트워크 요청에는 Retrofit, Volley, OkHttp 등을 사용할 수 있습니다.

        // 예시: Volley를 사용하여 토큰을 서버로 보내는 요청 예시
        val url = "http://your-server.com/register_token"
        val params = HashMap<String, String>()
        params["token"] = token

        val request = object : StringRequest(
            Request.Method.POST, url,
            Response.Listener { response ->
                // 서버 응답 처리
            },
            Response.ErrorListener { error ->
                // 오류 처리
            }) {
            override fun getParams(): Map<String, String> {
                return params
            }
        }

        val queue = Volley.newRequestQueue(this)
        queue.add(request)
    }


    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
    }
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = channelDescription
            }

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }





}