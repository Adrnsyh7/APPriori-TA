package com.submission.tesapp.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.submission.tesapp.R
import com.submission.tesapp.data.preference.UserSessionManager
import com.submission.tesapp.ui.LoginActivity


class DashboardFragment : Fragment() {
    private lateinit var userSessionManager: UserSessionManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)
        userSessionManager = UserSessionManager(requireContext())
        if (!userSessionManager.isUserLoggedIn()) {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.app_dashboard_menu, menu)
        return super.onCreateOptionsMenu(menu,inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {

            R.id.menu_profile -> {
                Toast.makeText(requireActivity(), "Profile Menu Clicked", Toast.LENGTH_SHORT).show()
                true
            }

//            R.id.setting -> {
//                val intent = Intent(requireActivity(), SettingsActivity::class.java)
//                startActivity(intent)
//                true
//            }

            R.id.logout -> {
                userSessionManager.logoutUser()
                val intent = Intent(requireActivity(), LoginActivity::class.java)
                startActivity(intent)
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
    }
}