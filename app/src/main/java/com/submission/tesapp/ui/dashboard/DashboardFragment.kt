package com.submission.tesapp.ui.dashboard

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.submission.tesapp.R
import com.submission.tesapp.data.preference.UserSessionManager
import com.submission.tesapp.ui.LoginActivity


class DashboardFragment : Fragment() {
    private lateinit var userSessionManager: UserSessionManager
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        setHasOptionsMenu(true)
        userSessionManager = UserSessionManager(requireContext())
        if (!userSessionManager.isUserLoggedIn()) {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }
//        val popup = PopupMenu(activity?.applicationContext, view) // view = tombol atau ikon yang ditekan
//        popup.menuInflater.inflate(R.menu.app_dashboard_menu, popup.menu)
//        popup.setOnMenuItemClickListener { item ->
//            when (item.itemId) {
//                R.id.logout -> {
//                    userSessionManager.logoutUser()
//                    val intent = Intent(requireActivity(), LoginActivity::class.java)
//                    startActivity(intent)
//                    true
//                }
//
//                else -> false
//            }
//        }
//        popup.show()

        val ivMenu = view.findViewById<ImageView>(R.id.ivMenu)
        ivMenu.setOnClickListener {
            val popup = PopupMenu(requireContext(), ivMenu)
            popup.menuInflater.inflate(R.menu.app_dashboard_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.logout -> {
                    userSessionManager.logoutUser()
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                    true
                }
                    else -> false
                }
            }
            popup.show()
        }
        }




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }



    companion object {
    }
}