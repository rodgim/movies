package com.rodgim.movies.ui.detail

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import com.rodgim.movies.R
import com.rodgim.movies.databinding.ActivityDetailBinding
import com.rodgim.movies.ui.common.loadUrlAndPostponeEnterTransition
import com.rodgim.movies.ui.models.get5StarRating
import com.rodgim.movies.ui.models.getDurationDisplay
import com.rodgim.movies.ui.models.getFullPosterPath
import com.rodgim.movies.ui.models.getGenresDisplay
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
    private val movieDetailsAnimator: MovieDetailAnimator by lazy {
        MovieDetailAnimator()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        movieDetailsAnimator.postponeEnterTransition(this)

        viewModel.model.observe(this, Observer(::updateUi))
        binding.movieDetailFavorite.setOnClickListener {
            viewModel.onFavoriteClicked()
        }

        configToolbar()
        onBackPressedListener()
    }

    private fun updateUi(model: DetailViewModel.UiModel) = with(binding) {
        val movie = model.movie
        movieDetailToolbar.title = movie.title
        movieDetailImage.loadUrlAndPostponeEnterTransition(movie.getFullPosterPath(780), this@DetailActivity)

        movieTitle.text = movie.title
        rtStar.rating = movie.get5StarRating()
        movieStar.text = movie.get5StarRating().toString()
        movieGenres.text = movie.getGenresDisplay()
        movieDuration.text = movie.getDurationDisplay()
        movieDate.text = movie.releaseDate
        overviewContent.text = movie.overview

        val icon = if (model.isFavorite) R.drawable.ic_favorite_on else R.drawable.ic_favorite_off
        movieDetailFavorite.setImageDrawable(ContextCompat.getDrawable(this@DetailActivity, icon))

        movieDetailsAnimator.fadeVisible(scrollView, movieDetails)
        movieDetailsAnimator.scaleUpView(movieDetailFavorite)
    }

    private fun configToolbar() {
        setSupportActionBar(binding.movieDetailToolbar)
        with(supportActionBar) {
            this?.setDisplayShowTitleEnabled(false)
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

    private fun onBackPressedListener() {
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                movieDetailsAnimator.fadeInvisible(binding.scrollView, binding.movieDetails)
                binding.movieDetailFavorite.isVisible = false
                finishAfterTransition()
            }
        })
    }
}