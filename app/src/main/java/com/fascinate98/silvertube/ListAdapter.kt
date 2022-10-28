package com.fascinate98.silvertube

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fascinate98.silvertube.network.ListResponse

class ListAdapter(private val context: Context) : RecyclerView.Adapter<ListAdapter.ViewHolder>() {

    var datas = mutableListOf<ListResponse.Item>()

    interface OnItemClickListener{
        fun onItemClick(v:View, data: ListResponse.Item, pos : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position], position)
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val title: TextView = itemView.findViewById(R.id.title)
        private val num: TextView = itemView.findViewById(R.id.num)
        private val deletebtn: Button = itemView.findViewById(R.id.deletebtn)
        private val mainActivity = MainActivity.getInstacne()

        fun bind(item: ListResponse.Item, position: Int) {
            title.text = item.snippet.title
            num.text = (position + 1).toString()

            deletebtn.setOnClickListener {
                mainActivity?.deletevideo(position)
                //Log.d("dssss", position.toString())
            }

            itemView.setOnClickListener {
                listener?.onItemClick(itemView,item,position)
            }
        }
    }


}