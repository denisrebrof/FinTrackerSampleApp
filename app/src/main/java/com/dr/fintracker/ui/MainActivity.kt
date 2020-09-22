package com.dr.fintracker.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.dr.fintracker.R
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar);
        val navController = nav_host_fragment.findNavController()
        bottom_nav_view.setupWithNavController(navController)
        intent?.extras?.let {
            if(it.containsKey("frgToLoad")){
                val destination = getNavigationAction(it.getInt("frgToLoad"))
                navController.navigate(destination)
            }
        }
    }

    private fun getNavigationAction(frgToLoad : Int) : Int{
        return when(frgToLoad){
            3->R.id.action_global_statisticsFragment
            2->R.id.action_global_targetsListFragment
            1->R.id.action_global_walletFragment
            else->R.id.action_global_targetsListFragment
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_dropdown_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.getItemId()
        if (id == R.id.action_settings) {
            Toast.makeText(this, "Action clicked", Toast.LENGTH_LONG).show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}