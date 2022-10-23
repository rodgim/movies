package com.rodgim.movies.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.rodgim.movies.R
import com.rodgim.movies.databinding.ActivityDetailBinding
import com.rodgim.movies.ui.common.loadUrl
import org.koin.androidx.scope.ScopeActivity
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class DetailActivity : ScopeActivity() {

    companion object {
        const val MOVIE = "DetailActivity:movie"
    }

    private lateinit var binding: ActivityDetailBinding

    private val viewModel: DetailViewModel by viewModel {
        parametersOf(intent.getIntExtra(MOVIE, -1))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.model.observe(this, Observer(::updateUi))
        binding.movieDetailFavorite.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        configToolbar()
    }

    private fun updateUi(model: DetailViewModel.UiModel) = with(binding) {
        val movie = model.movie
        movieDetailToolbar.title = movie.title
        movieDetailImage.loadUrl("https://image.tmdb.org/t/p/w780${movie.backdropPath}")
        movieDetailSummary.text = movie.overview
        movieDetailInfo.setMovie(movie)

        val icon = if (movie.favorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
        movieDetailFavorite.setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, icon))
    }

    private fun configToolbar() {
        with(supportActionBar) {
            this?.setDisplayHomeAsUpEnabled(true)
            this?.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}