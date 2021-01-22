package com.wit.hillforts.views.hillfortlist

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.card_hillfort.view.*
import com.wit.hillforts.R
import com.wit.hillforts.models.HillfortModel
import com.wit.hillforts.views.BaseView
import com.wit.hillforts.views.hillfort.HillfortPresenter


interface HillfortListener {

    fun onHillfortClick(hillfort: HillfortModel)
}

class HillfortAdapter constructor(
    private var hillforts: List<HillfortModel>,
    private val listener: HillfortListener

) : RecyclerView.Adapter<HillfortAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_hillfort,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillfort = hillforts[holder.adapterPosition]
        holder.bind(hillfort, listener)

    }

    override fun getItemCount(): Int = hillforts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {


        fun bind(hillfort: HillfortModel, listener: HillfortListener) {
            var visited = if (hillfort.visited) "Yes" else "Not Yet"
            var formattedLat = String.format("%.5f", hillfort.lat)
            var formattedLng = String.format("%.5f", hillfort.lng)
            itemView.hillfortTitle.text = hillfort.name
            itemView.tag = hillfort.id
            itemView.description.text = hillfort.description
            itemView.location.text= "Location: ${formattedLat}, ${formattedLng}"
            itemView.visited.text= "Visited: $visited"
            if (hillfort.image1.length > 20) {
                 Glide.with(itemView.context).load(hillfort.image1).into(itemView.hillfortImage);
            }
            else  itemView.hillfortImage.setImageResource(itemView.context.getResources().getIdentifier(hillfort.image1, "drawable", itemView.context.packageName))
            itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
        }

    }




}