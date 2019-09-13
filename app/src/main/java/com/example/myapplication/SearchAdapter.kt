package com.example.myapplication

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView

class SearchAdapter (val ctx: Context, val data:List<pharmacie>) : BaseAdapter() {


    override fun getItem(p0: Int)= data.get(p0)

    override fun getItemId(p0: Int) = data.get(p0).hashCode().toLong()

    override fun getCount() = data.size




    override fun getView(position: Int, p0: View?, parent: ViewGroup?): View {
        var view = p0
        var holder:ViewHolder
        if (view == null) {
            view = LayoutInflater.from(ctx).inflate(R.layout.search_pharmacie_layout,parent,false)
            val textView1 = view?.findViewById(R.id.textView_search_address) as TextView
            val textView2 = view?.findViewById(R.id.textView_search_distance) as TextView
            val textView3 = view?.findViewById(R.id.textView_search_phone) as TextView
            val imageView1 = view?.findViewById(R.id.imageView_search_address) as ImageView
            val imageView2 = view?.findViewById(R.id.imageView_search_distance) as ImageView
            val imageView3 = view?.findViewById(R.id.imageView_search_phone) as ImageView
            holder = ViewHolder(textView1,textView2,textView3,imageView1,imageView2,imageView3)
            view?.setTag(holder)
        }
        else {
            holder = view.tag as ViewHolder


        }
        holder.imageView1.setImageResource(R.drawable.map)
        holder.imageView2.setImageResource(R.drawable.map)
        holder.imageView3.setImageResource(R.drawable.call)
        return view

    }
    private class ViewHolder(val textView1:TextView,val textView2:TextView,val textView3:TextView,
                             val imageView1:ImageView,val imageView2:ImageView,val imageView3:ImageView)
}