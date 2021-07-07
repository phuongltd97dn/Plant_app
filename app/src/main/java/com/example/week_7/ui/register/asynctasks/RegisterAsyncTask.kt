package com.example.week_7.ui.register.asynctasks

import android.app.Activity
import android.content.Intent
import android.os.AsyncTask
import android.widget.Toast
import com.example.week_7.models.User
import com.example.week_7.ui.home.HomeActivity

class RegisterAsyncTask(val contextParent: Activity, var user: User?, val isFirstTime: Boolean) :
    AsyncTask<Unit, Int, Unit>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: Unit?) {

    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)

    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)
        if (user != null) {
            val intent = Intent(contextParent, HomeActivity::class.java)
            contextParent.startActivity(intent)
            contextParent.finish()
        } else if (isFirstTime) {
            Toast.makeText(
                contextParent,
                "This is your first time using the app. Please register!",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}