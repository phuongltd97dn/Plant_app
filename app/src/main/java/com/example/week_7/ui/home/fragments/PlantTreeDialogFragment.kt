package com.example.week_7.ui.home.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import com.example.week_7.R
import com.example.week_7.database.MyDatabase
import com.example.week_7.models.Cultivation
import com.example.week_7.models.Plant
import kotlinx.android.synthetic.main.fragment_plant_tree_dialog.*
import java.text.SimpleDateFormat
import java.util.*

class PlantTreeDialogFragment(val contextParent: Activity) : DialogFragment() {
    private var plants: List<Plant>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set width for Dialog
        setStyle(STYLE_NO_TITLE, R.style.AppTheme_Dialog_PlantTreeDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Set background round corner for DialogFragment
        plants = MyDatabase.getInstance(contextParent).myDAO.getAllPlants()

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return inflater.inflate(R.layout.fragment_plant_tree_dialog, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initPlantTreeDropDown()

        onCancelImageClickedListener()
        onOKButtonClickedListener()
    }

    private fun initPlantTreeDropDown() {
        val options = mutableListOf<String>("Choose your tree to plant:")
        plants?.forEach {
            options.add(it.name)
        }

        spPlantTree.adapter = ArrayAdapter<String>(
            contextParent,
            R.layout.support_simple_spinner_dropdown_item,
            options
        )

        spPlantTree.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }
        }
    }

    private fun onCancelImageClickedListener() {
        imgCancel.setOnClickListener {
            dismiss()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun onOKButtonClickedListener() {
        btnOk.setOnClickListener {
            val selectedItem = spPlantTree.selectedItem.toString()
            val selectedPosition = spPlantTree.selectedItemPosition

            val userGrowId = MyDatabase.getInstance(contextParent).myDAO.getUser()?.userId
            val plantId =
                MyDatabase.getInstance(contextParent).myDAO.getPlantWithName(selectedItem)?.plantId

            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
            val dateCultivation = dateFormat.format(Calendar.getInstance().time)

            if (selectedPosition != 0) {
                val cultivation = Cultivation(
                    userGrowId = userGrowId,
                    plantId = plantId,
                    dateCultivation = dateCultivation
                )

                MyDatabase.getInstance(contextParent).myDAO.insertCultivation(cultivation)
            }

            dismiss()
        }
    }

}