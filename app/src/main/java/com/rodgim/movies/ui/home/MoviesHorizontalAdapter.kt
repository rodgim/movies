package com.rodgim.movies.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rodgim.entities.Movie
import com.rodgim.movies.databinding.ViewMovieHorizontalBinding
import com.rodgim.movies.ui.common.loadUrl
import com.rodgim.movies.ui.models.getFullPosterPath

class MoviesHorizontalAdapter(private val listener: (Movie, ImageView) -> Unit) :
    ListAdapter<Movie, MoviesHorizontalAdapter.ViewHolder>(MoviesHorizontalDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ViewMovieHorizontalBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val movie = currentList[position]
        holder.bind(movie)
        holder.itemView.setOnClickListener {
            listener(movie, holder.getMoviePosterView())
        }
    }

    inner class ViewHolder(private val binding: ViewMovieHorizontalBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) = with(binding) {
            movieTitle.text = movie.title
            moviePoster.loadUrl(movie.getFullPosterPath())
        }
        fun getMoviePosterView() = binding.moviePoster
    }
}

class MoviesHorizontalDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.title == newItem.title
    }
}
