package com.abilsayuri.flix.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.abilsayuri.flix.R
import com.abilsayuri.flix.listMovie.data.response.ApiService
import com.abilsayuri.flix.listMovie.data.response.cast_response.Cast

class ActorList(private val context: Context,
                private val castList: List<Cast>) :
    RecyclerView.Adapter<ActorList.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.actor_card_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cast = castList[position]

        holder.realNameTextView.text = cast.name
        holder.characterNameTextView.text = cast.character

        Glide.with(context)
            .load(getFullProfilePath(cast.profile_path))
            .placeholder(R.drawable.placeholder)
            .error(R.drawable.placeholder)
            .into(holder.profileImageView)
    }

    override fun getItemCount(): Int {
        return castList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val profileImageView: ImageView = itemView.findViewById(R.id.profileImageView)
        val realNameTextView: TextView = itemView.findViewById(R.id.realNameTextView)
        val characterNameTextView: TextView = itemView.findViewById(R.id.characterNameTextView)
    }

    private fun getFullProfilePath(profilePath: String): String {
        return "${ApiService.IMAGE_BASE_URL}$profilePath"
    }
}