package com.jacksafblaze.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.jacksafblaze.roomdemo.databinding.ListItemBinding
import com.jacksafblaze.roomdemo.db.Subscriber

class MyRecyclerViewAdapter(private val subscribers: List<Subscriber>) :
    RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder>() {
    class MyViewHolder(private val binding: ListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(subscriber: Subscriber) {
            binding.nameTextView.text = subscriber.name
            binding.emailTextView.text = subscriber.email
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding =
            DataBindingUtil.inflate(inflater, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subscribers[position])
    }

    override fun getItemCount(): Int {
        return subscribers.size
    }
}