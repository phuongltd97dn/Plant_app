package com.example.week_7.ui.home.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.week_7.R
import com.example.week_7.database.MyDatabase
import com.example.week_7.models.Cultivation
import com.example.week_7.ui.detail.CultivationDetailActivity
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.item_cultivation.view.*
import java.util.*

class CultivationAdapter(val context: Context) :
    RecyclerView.Adapter<CultivationAdapter.PlantViewHolder>() {
    private var data: List<Cultivation>? = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlantViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_cultivation, parent, false)
        return PlantViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data?.size ?: 0
    }

    override fun onBindViewHolder(holder: PlantViewHolder, position: Int) {
        val androidColors: IntArray = context.resources.getIntArray(R.array.androidcolors)
        val randomAndroidColor = androidColors[Random().nextInt(androidColors.size)]
        holder.container.setBackgroundColor(randomAndroidColor)

        data?.get(position).apply {
            val plantImage =
                MyDatabase.getInstance(context).myDAO.getPlantWithId(this?.plantId)?.imageUrl
            val plantName =
                MyDatabase.getInstance(context).myDAO.getPlantWithId(this?.plantId)?.name

            if (plantImage != null) {
                Glide.with(context).load(plantImage).into(holder.imgCultivation)
            } else {
                Glide.with(context).load(R.mipmap.ic_launcher_round).into(holder.imgCultivation)
            }

            holder.tvCultivationName.text = plantName
        }

        holder.tvCultivationTime.text = data?.get(position)?.dateCultivation

        holder.container.setOnClickListener {
            val intent = Intent(context, CultivationDetailActivity::class.java)
            intent.putExtra("cultivation", data?.get(position))
            context.startActivity(intent)
        }
    }

    fun setData(data: List<Cultivation>?) {
        this.data = data
        notifyDataSetChanged()
    }

    inner class PlantViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val container: ConstraintLayout = itemView.clPlantContainer
        val imgCultivation: CircleImageView = itemView.imgPlant
        val tvCultivationName: TextView = itemView.tvCultivationName
        val tvCultivationTime: TextView = itemView.tvCultivationTime
    }
}