package com.example.week_7.ui.splash

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.week_7.R
import com.example.week_7.ui.splash.asynctasks.SplashAsyncTask

class SplashActivity : AppCompatActivity() {
    private lateinit var splashAsyncTask: SplashAsyncTask

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        splashAsyncTask =
            SplashAsyncTask(this)
        splashAsyncTask.execute()
    }
}