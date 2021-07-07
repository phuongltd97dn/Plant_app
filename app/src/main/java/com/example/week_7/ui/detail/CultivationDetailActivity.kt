package com.example.week_7.ui.detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.week_7.R
import com.example.week_7.database.MyDatabase
import com.example.week_7.models.Cultivation
import kotlinx.android.synthetic.main.activity_cultivation_detail.*

class CultivationDetailActivity : AppCompatActivity() {
    private lateinit var cultivation: Cultivation

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cultivation_detail)

        setSupportActionBar(findViewById(R.id.detailToolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        cultivation = intent.getSerializableExtra("cultivation") as Cultivation
//        Log.d("TAG", "onCreate: ${intent.getSerializableExtra("cultivation")}")

        val plantImage =
            MyDatabase.getInstance(this).myDAO.getPlantWithId(cultivation.plantId)?.imageUrl
        val plantDes =
            MyDatabase.getInstance(this).myDAO.getPlantWithId(cultivation.plantId)?.description

        if (plantImage != null) {
            Glide.with(this).load(plantImage).into(imgDetail)
        } else {
            Glide.with(this).load(R.mipmap.ic_launcher_round).into(imgDetail)
        }

        tvDetailCultivationDate.text = cultivation.dateCultivation
        tvDetailDescribe.text = plantDes
    }
}