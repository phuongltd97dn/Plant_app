package com.example.week_7.ui.home.asynctasks

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import com.example.week_7.database.MyDatabase
import com.example.week_7.models.Plant
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException

class HomeAsyncTask(val contextParent: Activity) : AsyncTask<Unit, Unit, Unit>() {

    override fun onPreExecute() {
        super.onPreExecute()
    }

    override fun doInBackground(vararg params: Unit?) {
        val plants = getListPlantFromJson()
        MyDatabase.getInstance(contextParent).myDAO.insertPlants(plants)
    }

    override fun onProgressUpdate(vararg values: Unit?) {
        super.onProgressUpdate(*values)
    }

    override fun onPostExecute(result: Unit?) {
        super.onPostExecute(result)

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