package io.github.ravitripathi.blazekotlin

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.photo_image_view.view.*

class HomeFeedAdapter(val items: ArrayList<PhotoModelDict>, val context: Context?): RecyclerView.Adapter<HomeFeedAdapter.PhotoHolder>() {

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoHolder {
        return PhotoHolder(LayoutInflater.from(context).inflate(R.layout.photo_image_view, parent, false))
    }

    override fun onBindViewHolder(holder: PhotoHolder, position: Int) {
        val imageUrlString = items.get(position).model.urlString
        Picasso.get().load(imageUrlString).into(holder.photoImageView)
    }

    class PhotoHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val photoImageView = view.imageView
    }
}