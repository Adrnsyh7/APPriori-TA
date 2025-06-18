package com.submission.tesapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.navigation.navOptions
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.submission.tesapp.R
import com.submission.tesapp.data.preference.UserSessionManager
import com.submission.tesapp.ui.transactions.InputTransactionFragment
import com.submission.tesapp.utils.DatePickerFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity(){
    private lateinit var userSessionManager: UserSessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        userSessionManager = UserSessionManager(this)
        if (!userSessionManager.isUserLoggedIn()) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)

        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.nav_dashboard,
            R.id.nav_transactions,
            R.id.nav_input,
            R.id.nav_process,
            R.id.nav_report,
        ).build()

        navController.navigate(
            R.id.nav_input,
            null,
            navOptions {
                anim {
                    enter = R.anim.slide_in_up
                    exit = R.anim.slide_out_down
                    popEnter = R.anim.slide_in_up
                    popExit = R.anim.slide_out_down
                }
            }
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = when (destination.id) {
                R.id.nav_dashboard -> "Home"
                R.id.nav_transactions -> "Data Transaksi"
                R.id.nav_input -> "Add"
                R.id.nav_process -> "Proses Apriori"
                R.id.nav_report -> "Laporan"
                else -> "Save Money"
            }

            supportActionBar?.hide()

//            if (destination.id == R.id.nav_process) {
//                supportActionBar?.hide()
//            } else {
//                supportActionBar?.show()
//            }
        }
    }



}