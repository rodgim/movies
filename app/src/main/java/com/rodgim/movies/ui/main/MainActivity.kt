package com.rodgim.movies.ui.main

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.rodgim.movies.R
import com.rodgim.movies.databinding.ActivityMainBinding
import org.koin.androidx.scope.ScopeActivity

class MainActivity : ScopeActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var appBarConfig: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment_container)
        navHostFragment?.let {
            navController = it.findNavController()
            setupWithNavController(binding.navigationView, navController)
        }
    }
}