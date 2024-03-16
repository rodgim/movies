package com.rodgim.movies.ui.home

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import com.facebook.shimmer.ShimmerFrameLayout
import com.rodgim.entities.Movie
import com.rodgim.movies.databinding.FragmentMoviesBinding
import com.rodgim.movies.ui.common.PermissionRequester
import com.rodgim.movies.ui.common.startActivity
import com.rodgim.movies.ui.detail.DetailActivity
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : ScopeFragment() {

    private var _binding: FragmentMoviesBinding? = null
    private val binding: FragmentMoviesBinding
        get() = checkNotNull(_binding) {
            "Cannot access binding because is null"
        }

    private val coarsePermissionRequester: PermissionRequester by lazy {
        PermissionRequester(requireActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    private val viewModel: MoviesViewModel by viewModel()
    private lateinit var popularAdapter: MoviesHorizontalAdapter
    private lateinit var nowPlayingAdapter: MoviesHorizontalAdapter
    private lateinit var topRatedAdapter: MoviesHorizontalAdapter

    private lateinit var movieViewClicked: ImageView

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
        setupRecyclerViews()
        viewModel.model.observe(viewLifecycleOwner, Observer(::updateUi))
    }

    private fun setupRecyclerViews() {
        binding.apply {
            popularAdapter = MoviesHorizontalAdapter(::movieClicked)
            rvPopular.adapter = popularAdapter

            nowPlayingAdapter = MoviesHorizontalAdapter(::movieClicked)
            rvNowPlaying.adapter = nowPlayingAdapter

            topRatedAdapter = MoviesHorizontalAdapter(::movieClicked)
            rvTopRated.adapter = topRatedAdapter
        }
    }

    private fun movieClicked(movie: Movie, view: ImageView) {
        movieViewClicked = view
        navigateToDetail(movie)
    }

    private fun updateUi(model: MoviesViewModel.UiModel) {
        when (model) {
            is MoviesViewModel.UiModel.Loading -> {
                binding.popularShimmerContainer.startShimmer()
                binding.nowPlayingShimmerContainer.startShimmer()
                binding.topRatedShimmerContainer.startShimmer()
            }
            is MoviesViewModel.UiModel.Content -> {
                popularAdapter.submitList(model.popular)
                nowPlayingAdapter.submitList(model.nowPlaying)
                topRatedAdapter.submitList(model.topRated)
                hideShimmer(binding.popularShimmerContainer)
                hideShimmer(binding.nowPlayingShimmerContainer)
                hideShimmer(binding.topRatedShimmerContainer)
            }
            is MoviesViewModel.UiModel.RequestLocationPermission -> coarsePermissionRequester.request {
                viewModel.onCoarsePermissionRequested()
            }
        }
    }

    private fun hideShimmer(container: ShimmerFrameLayout) {
        container.hideShimmer()
        container.visibility = View.GONE
    }

    private fun navigateToDetail(movie: Movie) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(), movieViewClicked, movieViewClicked.transitionName
        )
        requireActivity().startActivity<DetailActivity>(options.toBundle()) {
            putExtra(DetailActivity.MOVIE, movie.id)
        }
    }
}