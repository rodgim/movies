package com.rodgim.movies.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.rodgim.entities.Movie
import com.rodgim.movies.R
import com.rodgim.movies.databinding.FragmentFavoriteBinding
import com.rodgim.movies.ui.common.startActivity
import com.rodgim.movies.ui.detail.DetailActivity
import org.koin.androidx.scope.ScopeFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment : ScopeFragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding: FragmentFavoriteBinding
        get() = checkNotNull(_binding) {
            "Cannot access binding because is null"
        }

    private val viewModel: FavoriteViewModel by viewModel()
    private lateinit var favoriteAdapter: FavoriteAdapter

    private lateinit var movieViewClicked: ImageView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        viewModel.model.observe(viewLifecycleOwner, Observer(::updateUi))
    }

    private fun setupRecyclerView() {
        binding.apply {
            rvFavorite.addItemDecoration(getItemDecoration())
            favoriteAdapter = FavoriteAdapter(::movieClicked)
            rvFavorite.adapter = favoriteAdapter
        }
    }

    private fun getItemDecoration(): MaterialDividerItemDecoration {
        val itemDecoration = MaterialDividerItemDecoration(requireContext(), VERTICAL)
        itemDecoration.isLastItemDecorated = false
        itemDecoration.setDividerColorResource(requireContext(), R.color.white)
        itemDecoration.setDividerThicknessResource(requireContext(), R.dimen.divider)
        return itemDecoration
    }

    private fun movieClicked(movie: Movie, view: ImageView) {
        movieViewClicked = view
        navigateToDetail(movie)
    }

    private fun updateUi(model: FavoriteViewModel.UiModel) {
        when (model) {
            FavoriteViewModel.UiModel.Loading -> {
                showProgressBar()
            }
            is FavoriteViewModel.UiModel.Content -> {
                hideProgressBar()
                favoriteAdapter.submitList(model.favorites)
            }
        }
    }

    private fun navigateToDetail(movie: Movie) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(), movieViewClicked, movieViewClicked.transitionName
        )
        requireActivity().startActivity<DetailActivity>(options.toBundle()) {
            putExtra(DetailActivity.MOVIE, movie.id)
        }
    }

    private fun showProgressBar() {
        binding.pbLoading.isVisible = true
    }

    private fun hideProgressBar() {
        binding.pbLoading.isVisible = false
    }
}