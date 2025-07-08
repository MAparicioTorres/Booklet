package com.maat.booklet_tfg_v1.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.maat.booklet_tfg_v1.data.models.ReadingStatus
import com.maat.booklet_tfg_v1.data.util.Constants
import com.maat.booklet_tfg_v1.databinding.FragmentBookListBinding
import com.maat.booklet_tfg_v1.ui.showdetailsbook.BookDetailsActivity
import com.maat.booklet_tfg_v1.ui.viewmodel.BookViewModel
import kotlinx.coroutines.launch

class BookListFragment : Fragment() {
    private val bookListAdapter = BookAdapter()
    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var readingStatus: ReadingStatus
    private lateinit var binding: FragmentBookListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Obtenemos el valor de la String que esté en los argumentos del Fragment (Bundle).
        val statusString = requireArguments().getString(READING_STATUS_KEY)
        //Obtenemos la entrada del estado de la lectura mediante la cadena del Bundle.
        readingStatus = ReadingStatus.valueOf(statusString!!)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Inflamos la vista raíz.
        binding = FragmentBookListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Asignamos el adaptador.
        binding.recyclerView.adapter = bookListAdapter

        //Filtramos los libros por su estado de lectura.
        setBooksByStatus()
        //Configuramos el listener de la portada de los libros.
        setupBookClickListener()
    }

    private fun setBooksByStatus() {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                bookViewModel.getBooksByStatus(readingStatus).collect { filteredBooks ->
                    //Enviamos la lista de los libros filtrados por su estado de lectura en un flujo que ira actualizándose según agreguemos o eliminemos libros.
                    bookListAdapter.submitList(filteredBooks)
                }
            }
        }
    }

    private fun setupBookClickListener() {
        bookListAdapter.setBookClickListener { bookWithDetails ->
            //Creamos el Intent con referencia a la pantalla de detalles.
            val intent = Intent(activity, BookDetailsActivity::class.java)
            //Introducimos en el Bundle un Int con el ID del libro que clicamos.
            intent.putExtra(Constants.BOOK_ID_KEY, bookWithDetails.book.bookId)
            startActivity(intent)
        }
    }

    companion object {
        private const val READING_STATUS_KEY = "reading_status"

        fun getNewFragmentInstanceFromStatus(status: ReadingStatus): BookListFragment {
            //Obtenemos una instancia del fragment.
            val fragment = BookListFragment()
            val bundle = Bundle()
            //Introducimos en el bundle el nombre de la constante enum de los estados del libro.
            bundle.putString(READING_STATUS_KEY, status.name)
            //Asignamos el bundle a los argumentos del Fragment, mediante el que podremos acceder a los datos cuando se cree.
            fragment.arguments = bundle
            //Devolvemos la instancia.
            return fragment
        }
    }


}