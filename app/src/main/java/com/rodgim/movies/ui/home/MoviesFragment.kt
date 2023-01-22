package com.rodgim.movies.ui.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.rodgim.movies.databinding.FragmentMoviesBinding
import com.rodgim.movies.ui.common.PermissionRequester
import com.rodgim.movies.ui.common.startActivity
import com.rodgim.movies.ui.detail.DetailActivity
import com.rodgim.movies.ui.main.MainViewModel
import com.rodgim.movies.ui.main.MoviesAdapter
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : ScopeFragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() = checkNotNull(_binding) {
            "Cannot access binding because is null"
        }

    private lateinit var adapter: MoviesAdapter
    private val coarsePermissionRequester: PermissionRequester by lazy {
        PermissionRequester(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
    }
    private val viewModel: MainViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = MoviesAdapter(viewModel::onMovieClicked)
        binding.recycler.adapter = adapter
        viewModel.model.observe(viewLifecycleOwner, Observer(::updateUi))
    }

    private fun updateUi(model: MainViewModel.UiModel) {
        binding.progress.visibility = if (model is MainViewModel.UiModel.Loading) View.VISIBLE else View.GONE

        when (model) {
            is MainViewModel.UiModel.Content -> adapter.movies = model.movies
            is MainViewModel.UiModel.Navigation -> {
                requireActivity().startActivity<DetailActivity> {
                    putExtra(DetailActivity.MOVIE, model.movie.id)
                }
            }
            is MainViewModel.UiModel.RequestLocationPermission -> coarsePermissionRequester.request {
                viewModel.onCoarsePermissionRequested()
            }
            else -> {}
        }
    }
}