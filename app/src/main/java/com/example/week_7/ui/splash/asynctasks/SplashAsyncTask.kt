package com.example.week_7.ui.splash.asynctasks

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.os.SystemClock
import com.example.week_7.database.MyDatabase
import com.example.week_7.models.Plant
import com.example.week_7.ui.register.RegisterActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.activity_splash.*
import java.io.IOException

class SplashAsyncTask(val contextParent: Activity) :
    AsyncTask<Unit, Int, Unit>() {
    private lateinit var plants: List<Plant>

    override fun onPreExecute() {
        super.onPreExecute()

        plants = getListPlantFromJson()
    }

    override fun doInBackground(vararg params: Unit?) {
        MyDatabase.getInstance(contextParent).myDAO.insertPlants(plants)
        for (i in 1..100) {
            SystemClock.sleep(5000 / 100)
            publishProgress(i)
        }
    }

    override fun onProgressUpdate(vararg values: Int?) {
        super.onProgressUpdate(*values)

        val progressBar = contextParent.progressBar
        val number = values[0]
        if (number != null) {
            progressBar.progress = number
        }
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)

        val intent = Intent(contextParent, RegisterActivity::class.java)
        contextParent.startActivity(intent)
        contextParent.finish()
    }

    private fun getListPlantFromJson(): List<Plant> {
        val jsonFileString = getJsonDataFromAsset(contextParent, "plants.json")
        val plantType = object : TypeToken<List<Plant>>() {}.type

        return Gson().fromJson(jsonFileString, plantType)
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}