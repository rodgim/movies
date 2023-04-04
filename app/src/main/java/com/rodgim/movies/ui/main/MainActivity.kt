package com.rodgim.movies.ui.main

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.GravityCompat
import androidx.customview.widget.Openable
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.NavigationUI.setupWithNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
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

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_fragment_container)
        navHostFragment?.let {
            navController = it.findNavController()
            setupWithNavController(binding.navigationView, navController)
        }

        appBarConfig = AppBarConfiguration(
            topLevelDestinationIds = setOf(
                R.id.menu_movie, R.id.menu_tv_show, R.id.menu_favorite
            ),
            drawerLayout = openable
        )

        setupActionBarWithNavController(navController, appBarConfig)
        onBackPressedListener()
    }

    private val openable: Openable = object : Openable {
        override fun isOpen(): Boolean {
            return binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START)
        }

        override fun open() {
            binding.mainDrawerLayout.openDrawer(GravityCompat.START)
        }

        override fun close() {
            binding.mainDrawerLayout.closeDrawers()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu?.findItem(R.id.search)?.actionView as SearchView?
        searchView?.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView?.queryHint = "Find something here.."

        searchView?.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // TODO
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.search) {
            item.setOnActionExpandListener(object : OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                    // TODO
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                    onSupportNavigateUp()
                    return true
                }
            })
            return true
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_fragment_container)
        return NavigationUI.navigateUp(navController, appBarConfig) || super.onSupportNavigateUp()
    }

    private fun onBackPressedListener() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (binding.mainDrawerLayout.isDrawerOpen(GravityCompat.START)) {
                    binding.mainDrawerLayout.closeDrawers()
                    return
                }
                finish()
            }
        })
    }
}