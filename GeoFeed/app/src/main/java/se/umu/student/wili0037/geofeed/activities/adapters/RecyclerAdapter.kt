package se.umu.student.wili0037.geofeed.activities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import se.umu.student.wili0037.geofeed.R

class RecyclerAdapter (private var titles: List<String>, private var details: List<String>, private var images: List<Int>) :
RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemDistrict: TextView = itemView.findViewById(R.id.tv_district)
        val itemBody: TextView = itemView.findViewById(R.id.tv_postBody)
        val itemNrComment: TextView = itemView.findViewById(R.id.tv_nrComments)

        init {
            itemView.setOnClickListener { v: View ->
                val position: Int = adapterPosition
                Toast.makeText(itemView.context, "You clicked on item ${position + 1}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v= LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemDistrict.text = titles[position]
        holder.itemBody.text = details[position]
        holder.itemNrComment.text = "3"
    }

    override fun getItemCount(): Int {
        return titles.size
    }
}