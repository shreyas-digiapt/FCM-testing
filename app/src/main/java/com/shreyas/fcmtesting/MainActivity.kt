package com.shreyas.fcmtesting

import android.app.NotificationChannel
import android.app.NotificationManager
import android.graphics.Color
import android.os.Build
import android.os.Build.VERSION_CODES.O
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    companion object {
        val BLACK = "black"
        val WHITE = "white"
        val COLOR = "color"
        val FROMSERIVCE = "service"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (Build.VERSION.SDK_INT >= O) {
//            val channelId = getString(R.string.default_notification_channel_id)
//            val channelname = getString(R.string.default_notification_channel_name)
//            val notificationManager = getSystemService(NotificationManager::class.java)
//            notificationManager.createNotificationChannel(NotificationChannel(channelId,
//                channelname,
//                NotificationManager.IMPORTANCE_LOW))
//        }

        getData()

        setUpFirebase()
    }

    private fun getData() {
        try {
            val ops = intent.extras!!.getString("ops")
            val text = intent.extras!!.getString("ops")
            Log.d("firebase_1234", "data: ${ops}   ${text} gyhu ")
            setContetnt(ops)


        }catch (e:Exception) {
            Log.d("firebase_1234", "Exception: ${e.message}")
            try {
//                val ops = intent.extras!!.getString(FROMSERIVCE)
                setContetnt(Signlton.ops)
            }catch (e1:Exception) {
                Log.d("firebase_1234", "Exception:22 ${e.message}")

            }
        }
    }

    private fun setContetnt(ops: String?) {
        Log.d("firebase_12345", "data: ${ops} ")
        if (ops.equals(BLACK)) {
            container.setBackgroundColor(resources.getColor(R.color.black, null))
        }else if(ops.equals(COLOR)) {
            val colors = arrayOf(R.color.Aqua, R.color.Aquamarine, R.color.BlueViolet, R.color.Brown, R.color.Chocolate, R.color.Cyan).random()
            container.setBackgroundColor(resources.getColor(colors, null))
        }else {
            container.setBackgroundColor(resources.getColor(R.color.white, null))
        }

    }

    private fun setUpFirebase() {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task->

                if (!task.isSuccessful) {
                    Log.d("firebase_123", "notsuccess: ${task.exception!!.message}")
                    return@OnCompleteListener
                }

                val token = task.result?.token
                val msg = getString(R.string.msg_token_fmt, token)

                Log.d("firebase_123", "token: ${token}")
                Log.d("test_123", "tokentest: ${msg}")
            })
    }
}
