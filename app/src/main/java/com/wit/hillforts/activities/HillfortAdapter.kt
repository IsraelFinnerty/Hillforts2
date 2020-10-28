package com.wit.hillforts.activities

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.card_hillfort.view.*
import com.wit.hillforts.R
import com.wit.hillforts.helpers.readImage
import com.wit.hillforts.helpers.readImageFromPath
import com.wit.hillforts.models.HillfortModel
import kotlinx.android.synthetic.main.activity_hillfort.*


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
            itemView.hillfortTitle.text = hillfort.name
            itemView.description.text = hillfort.description
            itemView.location.text= "Location: ${hillfort.lat}, ${hillfort.lng}"
            itemView.visited.text= "Visited: $visited"
            if (hillfort.image1.length > 20) {
                itemView.hillfortImage.setImageURI(Uri.parse(hillfort.image1))
            }
            else  itemView.hillfortImage.setImageResource(itemView.context.getResources().getIdentifier(hillfort.image1, "drawable", itemView.context.packageName))
            itemView.setOnClickListener { listener.onHillfortClick(hillfort) }
        }
    }
}