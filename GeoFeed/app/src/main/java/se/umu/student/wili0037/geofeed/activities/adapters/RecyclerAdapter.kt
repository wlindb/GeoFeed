package se.umu.student.wili0037.geofeed.activities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import se.umu.student.wili0037.geofeed.R
import se.umu.student.wili0037.geofeed.model.Post


class RecyclerAdapter (private var posts: List<Post>) :
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
        holder.itemDistrict.text = posts[position].district
        holder.itemBody.text = posts[position].body
        holder.itemNrComment.text = posts[position].comments.size.toString()
    }

    override fun getItemCount(): Int {
        return posts.size
    }
}