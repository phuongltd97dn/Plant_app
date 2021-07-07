package com.example.week_7.ui.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.week_7.R
import com.example.week_7.database.MyDatabase
import com.example.week_7.models.Cultivation
import com.example.week_7.ui.detail.CultivationDetailActivity
import com.example.week_7.ui.home.adapters.CultivationAdapter
import com.example.week_7.ui.home.fragments.PlantTreeDialogFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.nav_header.view.*
import java.text.SimpleDateFormat
import java.util.*


class HomeActivity : AppCompatActivity() {
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var cultivationAdapter: CultivationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setSupportActionBar(findViewById(R.id.homeToolbar))
        supportActionBar?.setDisplayShowTitleEnabled(false)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        initDrawerNavigation()
        bindUserToDrawerNavigation()
        onNavigationItemSelectedListener()

        initCultivationAdapter()
        onCultivationRecyclerViewRefresh()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initDrawerNavigation() {
        nav_view.itemIconTintList = null

        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.open, R.string.close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
    }

    @SuppressLint("SimpleDateFormat")
    private fun onNavigationItemSelectedListener() {
        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_first -> {
                    val cultivations = MyDatabase.getInstance(this).myDAO.getAllCultivations()
                    cultivationAdapter.setData(cultivations)
                }

                R.id.nav_second -> {
                    val dialog = PlantTreeDialogFragment(this)

                    dialog.show(
                        supportFragmentManager,
                        PlantTreeDialogFragment::class.java.simpleName
                    )
                }

                R.id.nav_third -> {
                    val nearHarvestCultivations = mutableListOf<Cultivation>()

                    val cultivations = MyDatabase.getInstance(this).myDAO.getAllCultivations()
                    cultivations?.forEach { it ->
                        val growZoneNumber =
                            MyDatabase.getInstance(this).myDAO.getPlantWithId(it.plantId)?.growZoneNumber
                        if (growZoneNumber != null) {
                            val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")
                            val dateCultivationString = it.dateCultivation
                            val dateCultivation = dateFormat.parse(dateCultivationString)

                            val calendar = Calendar.getInstance()
                            calendar.time = dateCultivation
                            calendar.add(Calendar.DAY_OF_YEAR, 0)
                            calendar.add(Calendar.DAY_OF_YEAR, growZoneNumber / 2)
                            val dateNearHarvestString = dateFormat.format(calendar.time)
                            val dateNearHarvest = dateFormat.parse(dateNearHarvestString)

                            val currentDate = Calendar.getInstance().time

                            if (currentDate >= dateNearHarvest) {
                                nearHarvestCultivations.add(it)
                            }
                        }
                    }

                    cultivationAdapter.setData(nearHarvestCultivations)
                }

                R.id.nav_fourth -> Toast.makeText(
                    this,
                    getString(R.string.fourth_item_title),
                    Toast.LENGTH_SHORT
                ).show()

                R.id.nav_fifth -> Toast.makeText(
                    this,
                    getString(R.string.fifth_item_title),
                    Toast.LENGTH_SHORT
                ).show()
            }
            true
        }
    }

    private fun bindUserToDrawerNavigation() {
        val user = MyDatabase.getInstance(this).myDAO.getUser()
        if (user?.avatar != null && user.userName.isNotEmpty() && user.university.isNotEmpty()) {
            nav_view.getHeaderView(0).imgHomePhoto.setImageBitmap(user.avatar)
            nav_view.getHeaderView(0).tvHomeUserName.text = user.userName
            nav_view.getHeaderView(0).tvHomeUniversity.text = user.university
        } else {
            Toast.makeText(this, "null user", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initCultivationAdapter() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        rcvCultivation.layoutManager = layoutManager
        val itemDecorator = SpacingItemDecorator(24)
        rcvCultivation.addItemDecoration(itemDecorator)

        setupCultivationAdapter()
    }

    private fun setupCultivationAdapter() {
        cultivationAdapter = CultivationAdapter(this)
        val cultivations = MyDatabase.getInstance(this).myDAO.getAllCultivations()
        cultivationAdapter.setData(cultivations)

        rcvCultivation.adapter = cultivationAdapter
    }

    private fun onCultivationRecyclerViewRefresh() {
        refreshView.setOnRefreshListener {
            setupCultivationAdapter()
            refreshView.isRefreshing = false
        }
    }
}
