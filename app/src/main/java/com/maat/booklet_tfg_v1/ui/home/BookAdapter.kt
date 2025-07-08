package com.maat.booklet_tfg_v1.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.maat.booklet_tfg_v1.R
import com.maat.booklet_tfg_v1.data.dto.BookWithAuthorAndGenre
import com.maat.booklet_tfg_v1.databinding.CardItemBinding
import com.maat.booklet_tfg_v1.ui.home.BookAdapter.BookViewHolder

class BookAdapter() : ListAdapter<BookWithAuthorAndGenre, BookViewHolder>(DiffCallback()) {

    inner class BookViewHolder(private val binding: CardItemBinding) : ViewHolder(binding.root) {

        fun bind(bookWithDetails: BookWithAuthorAndGenre) {
            with(binding) {

                textViewBookTitle.text = bookWithDetails.book.title
                //Insertar la imagen de la portada del libro mediante Glide.
                Glide.with(binding.root.context)
                    .load(bookWithDetails.book.bookCoverUrl)
                    .placeholder(R.drawable.no_book_cover)
                    .error(R.drawable.no_book_cover)
                    .into(imageViewBookCover)

                //Asignamos el comportamiento del clic encima de la portadas.
                root.setOnClickListener {
                    onBookClickListener?.invoke(bookWithDetails)
                }
            }
        }
    }

    private var onBookClickListener: ((BookWithAuthorAndGenre) -> Unit)? = null
    fun setBookClickListener(listener: (BookWithAuthorAndGenre) -> Unit){
        onBookClickListener = listener
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BookViewHolder {
        //Inflamos el layout y lo pasamos el valor raíz al BookViewHolder.
        val binding = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BookViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: BookViewHolder,
        position: Int
    ) {
        //Llamamos al holder para que cargue los datos pertinentes sobre los libros en la posición pasada por parámetros.
        holder.bind(getItem(position))
    }

    class DiffCallback: DiffUtil.ItemCallback<BookWithAuthorAndGenre>(){
        override fun areItemsTheSame(
            oldItem: BookWithAuthorAndGenre,
            newItem: BookWithAuthorAndGenre
        ): Boolean {
            return oldItem.book.bookId == newItem.book.bookId
        }

        override fun areContentsTheSame(
            oldItem: BookWithAuthorAndGenre,
            newItem: BookWithAuthorAndGenre
        ): Boolean {
            return oldItem == newItem
        }


    }
}