package com.example.finalalarm

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.TimePicker
import android.widget.Toast
import android.widget.ToggleButton
import androidx.annotation.RequiresApi
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var alarmTimePicker: TimePicker
    private lateinit var pendingIntent: PendingIntent
    private lateinit var alarmManager: AlarmManager

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val intent = Intent(this, MyBroadcastReceiver::class.java)
        val requestCode = 0
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
        pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent, flags)

        alarmTimePicker = findViewById(R.id.timePicker)
        alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun onToggleClicked(view: View) {
        if ((view as ToggleButton).isChecked) {
            Toast.makeText(this, "Alarm On", Toast.LENGTH_SHORT).show()
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, alarmTimePicker.hour)
            calendar.set(Calendar.MINUTE, alarmTimePicker.minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            val intent = Intent(this, AlarmReceiver::class.java)
            pendingIntent = PendingIntent.getBroadcast(
                this@MainActivity, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )

            val currentTime = System.currentTimeMillis()
            var alarmTime = calendar.timeInMillis
            if (alarmTime <= currentTime) {
                alarmTime += AlarmManager.INTERVAL_DAY
            }

            alarmManager.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                alarmTime,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )

        } else {
            alarmManager.cancel(pendingIntent)
            Toast.makeText(this, "Alarm Off", Toast.LENGTH_SHORT).show()
        }
    }
}
