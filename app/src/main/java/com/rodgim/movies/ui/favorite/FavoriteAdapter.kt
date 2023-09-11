package com.rodgim.movies.ui.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rodgim.entities.Movie
import com.rodgim.movies.databinding.ViewFavoriteMovieItemBinding
import com.rodgim.movies.ui.common.loadUrl
import com.rodgim.movies.ui.models.get5StarRating
import com.rodgim.movies.ui.models.getFullPosterPath
import com.rodgim.movies.ui.models.getGenresDisplay

class FavoriteAdapter(private val listener: (Movie, ImageView) -> Unit):
    ListAdapter<Movie, FavoriteAdapter.ViewHolder>(FavoriteMovieDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ViewFavoriteMovieItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = currentList[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            listener(movie, holder.getMoviePosterView())
        }
    }

    inner class ViewHolder(private val view: ViewFavoriteMovieItemBinding): RecyclerView.ViewHolder(view.root) {
        fun bind(movie: Movie) = with(view) {
            moviePoster.loadUrl(movie.getFullPosterPath())
            movieTitle.text = movie.title
            movieDate.text = movie.releaseDate
            movieGenres.text = movie.getGenresDisplay()
            rtStar.rating = movie.get5StarRating()
            movieStar.text = movie.get5StarRating().toString()
        }

        fun getMoviePosterView() = view.moviePoster
    }
}

class FavoriteMovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.title == newItem.title
    }
}
