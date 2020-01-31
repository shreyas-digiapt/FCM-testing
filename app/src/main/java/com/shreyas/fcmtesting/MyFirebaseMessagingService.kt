package com.shreyas.fcmtesting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {

        Log.d("test_123", "dummy:  ")

        remoteMessage.data.isNotEmpty().let { it->
            Log.d("test_123", "eret: ${remoteMessage.data.get("ops")}")
            Signlton.ops = remoteMessage.data.get("ops")!!

        }
        remoteMessage.notification.let {

            val filter  = IntentFilter("com.google.firebase.MESSAGING_EVENT")
            registerReceiver(TestReciver(), filter)
            showNotification(it?.body!!, remoteMessage)
        }

    }

    private fun sheduleJob() {
        val work = OneTimeWorkRequest.Builder(Woker::class.java).build()
        WorkManager.getInstance().beginWith(work).enqueue()

    }

    override fun onNewToken(token: String) {
        Log.d("firebase_123", "token: ${token}")

    }

    private fun showNotification(
        messageBody: String,
        remoteMessage: RemoteMessage
    ) {
        val intent = Intent(this, MainActivity::class.java)
        remoteMessage.data.isNotEmpty().let {

            intent.putExtra("service", remoteMessage.data.get("ops"))
        }
        Log.d("firebase_12346", "data: ${remoteMessage.data.get("ops")}")
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
            PendingIntent.FLAG_ONE_SHOT)

        val channelId = getString(R.string.default_notification_channel_id)
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.icom_123)
            .setContentTitle(getString(R.string.fcm_message))
            .setContentText(messageBody+"  asgdyusauduhuashduihuiahsui")
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(pendingIntent)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                "Channel human readable title",
                NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build())
    }

}