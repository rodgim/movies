package com.rodgim.movies.ui.main

import android.Manifest
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.rodgim.movies.databinding.ActivityMainBinding
import com.rodgim.movies.ui.common.PermissionRequester
import com.rodgim.movies.ui.common.startActivity
import com.rodgim.movies.ui.main.MainViewModel.UiModel
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : ScopeActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: MoviesAdapter
    private val coarsePermissionRequester = PermissionRequester(this, Manifest.permission.ACCESS_COARSE_LOCATION)
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = MoviesAdapter(viewModel::onMovieClicked)
        binding.recycler.adapter = adapter
        viewModel.model.observe(this, Observer(::updateUi))
    }

    private fun updateUi(model: UiModel) {
        binding.progress.visibility = if (model is UiModel.Loading) View.VISIBLE else View.GONE

        when (model) {
            is UiModel.Content -> adapter.movies = model.movies
            is UiModel.Navigation -> {
                // Go to Detail}
            }
            is UiModel.RequestLocationPermission -> coarsePermissionRequester.request {
                viewModel.onCoarsePermissionRequested()
            }
            else -> {}
        }
    }
}