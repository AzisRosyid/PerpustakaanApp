package com.example.perpustakaanapi.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.example.perpustakaanapi.ApiRetrofit
import com.example.perpustakaanapi.Method
import com.example.perpustakaanapi.R
import com.example.perpustakaanapi.databinding.AdapterBookBinding
import com.example.perpustakaanapi.models.Book

class BookAdapter(val book: ArrayList<Book>, val listener: OnClickListener): RecyclerView.Adapter<BookAdapter.ViewHolder>() {
    class ViewHolder(val binding: AdapterBookBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(AdapterBookBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = book[position]
        holder.binding.title.text = book.title
        holder.binding.author.text = book.author

        Glide.with(holder.itemView)
            .load(GlideUrl(Method.baseUrl + "Books/ImageBook/" + book.image, LazyHeaders.Builder().addHeader("Authorization", "").build()))
            .override(400)
            .error(R.drawable.ic_baseline_book_24)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .skipMemoryCache(true)
            .into(holder.binding.image)

        holder.itemView.setOnClickListener {
            listener.onClick(book)
        }
    }

    override fun getItemCount(): Int {
        return book.size
    }

    fun setData(data: List<Book>){
        book.clear()
        book.addAll(data)
        notifyDataSetChanged()
    }

    interface OnClickListener{
        fun onClick(book: Book)
    }
}