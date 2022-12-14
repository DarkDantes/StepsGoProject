package com.sna.stepsgo

import android.media.ImageWriter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private var list = mutableListOf<User>()
    private var actionDelete : ((User)->Unit)? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.posts_list_item, parent, false)


        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = list[position]
        holder.tvFirstName.text= user.firstName
        holder.tvLastName.text = user.lastName
        holder.countsteps.text = user.countsteps

        holder.actionDelete.setOnClickListener { actionDelete?.invoke(user) }


    }

    override fun getItemCount() = list.size

    fun setData(data: List<User>){
        list.apply {
            clear()
            addAll(data)
        }
        notifyDataSetChanged()
    }

    fun setOnActionDeleteListener(callback: (User)->Unit){
  this.actionDelete = callback
    }



    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvFirstName: TextView = itemView.findViewById(R.id.tv_start_time)
        val tvLastName: TextView = itemView.findViewById(R.id.tv_end_time)
        val countsteps : TextView = itemView.findViewById(R.id.tv_last_steps)
        val actionDelete : ImageView = itemView.findViewById(R.id.action_delete)

    }
}