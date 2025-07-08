package com.maat.booklet_tfg_v1.ui.home

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.tabs.TabLayoutMediator
import com.maat.booklet_tfg_v1.R
import com.maat.booklet_tfg_v1.data.dto.BookWithAuthorAndGenre
import com.maat.booklet_tfg_v1.data.models.ReadingStatus
import com.maat.booklet_tfg_v1.databinding.ActivityMainBinding
import com.maat.booklet_tfg_v1.databinding.CardItemCompactDialogBinding
import com.maat.booklet_tfg_v1.ui.addbook.AddBookActivity
import com.maat.booklet_tfg_v1.ui.viewmodel.BookViewModel
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val readingStatuses = ReadingStatus.entries.toList()
    private val bookViewModel: BookViewModel by viewModels()
    private var toReadBooks: List<BookWithAuthorAndGenre> = emptyList()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Establecemos el layout principal mediante ViewBinding.
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //Configuramos el ViewPager.
        setupViewPager()

        //Animamos el botón flotante de recomendación de libros para que aparezca solo en la pestaña «Pendientes».
        animateFab()

        //Activamos el flujo para la lista de libros disponibles.
        getToReadBookList()

        //Configuramos el botón de recomendar la próxima lectura y el de añadir un nuevo libro.
        setupRecommendBookButton()
        setupAddBookButton()
    }

    private fun setupAddBookButton() {
        //Hacemos que el botón de añadir nos lleve al formulario.
        binding.fabNewBooks.setOnClickListener {
            val intent = Intent(this, AddBookActivity::class.java)
            startActivity(intent)
        }
    }

    private fun setupViewPager() {
        //Obtenemos la referencia de las vistas en el layout.
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        //Creamos el adaptador con las pantallas según el estado de lectura de los libros.
        val adapter = object : FragmentStateAdapter(this) {
            override fun createFragment(position: Int): Fragment {
                return BookListFragment.getNewFragmentInstanceFromStatus(readingStatuses[position])
            }

            override fun getItemCount(): Int {
                return readingStatuses.size
            }
        }
        //Asignamos el adaptador.
        viewPager.adapter = adapter

        // Iniciamos la aplicación en la pestaña «Leido» sin deslizamiento para evitar que el botón de recomendación de libros se vea al cargar la pantalla.
        viewPager.setCurrentItem(2, false)


        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            //Según el estado establecemos el texto de la pestaña.
            tab.text = when (readingStatuses[position]) {
                ReadingStatus.READ -> "Leídos"
                ReadingStatus.READING -> "Leyendo"
                ReadingStatus.TO_READ -> "Pendientes"
            }

        }.attach() //Vinculamos el TabLayout con el ViewPager.
    }


    private fun setupRecommendBookButton() {
        binding.fabRecommendBooks.setOnClickListener {
            //Si no hay libros disponibles queremos que no se muestre ningún layout.
            if (toReadBooks.isEmpty()) {
               val dialog =  MaterialAlertDialogBuilder(this@MainActivity)
                    .setMessage("""Tu lista de libros pendientes está vacía, así que no podemos recomendarte ninguno.
                        |¿Por qué no pruebas a añadir uno?""".trimMargin())
                    .show()

                val textViewDialog = dialog.findViewById<TextView>(android.R.id.message)
                textViewDialog?.gravity = Gravity.CENTER //Centramos el mensaje.
            }
            //Si hay libros disponibles, queremos que se recomiende un libro de la lista de «Pendientes».
            else {
                //Seleccionamos un libro aleatorio de entre los disponibles.
                val selectedBook = toReadBooks.random()

                val title = selectedBook.book.title
                val author = selectedBook.author.authorName

                val titleMessage = "Tu próxima lectura"
                val bodyMessage = "$title de $author"

                val spannableTitleMessage = SpannableString(titleMessage)
                val spannableBodyMessage = SpannableString(bodyMessage)

                val startIndex = bodyMessage.indexOf(title)
                val endIndex = startIndex + title.length

                //Subrayamos la primera parte del mensaje para un aspecto más estético
                spannableTitleMessage.setSpan(
                    UnderlineSpan(),
                    0,
                    titleMessage.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                //Hacemos que el título del libro esté en negrita y cursiva.
                spannableBodyMessage.setSpan(
                    StyleSpan(Typeface.BOLD_ITALIC),
                    startIndex,
                    endIndex,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                //Inflamos el layout personalizado para la ventana emergente.
                val cardItemBinding = CardItemCompactDialogBinding.inflate(layoutInflater)

                //Asignamos los valores del título y el mensaje.
                cardItemBinding.textViewDialogTitle.text = spannableTitleMessage
                cardItemBinding.textViewDialogMessage.text = spannableBodyMessage

                //Cargamos la imagen de la portada del libro.
                Glide.with(binding.root.context)
                    .load(selectedBook.book.bookCoverUrl)
                    .placeholder(R.drawable.no_book_cover)
                    .error(R.drawable.no_book_cover)
                    .into(cardItemBinding.imageViewBookCover)

                //Creamos la ventana emergente con el layout.
                MaterialAlertDialogBuilder(this@MainActivity)
                    .setView(cardItemBinding.root)
                    .show() //La mostramos.
            }
        }
    }

    private fun getToReadBookList() {
        //Activamos el flujo de la lista de libros categorizados como «Pendientes».
        lifecycleScope.launch {
            bookViewModel.getBooksByStatus(ReadingStatus.TO_READ).collect { books ->
                toReadBooks = books
            }
        }
    }

    private fun animateFab() {
        //Obtenemos la posición del valor «TO_READ» en el enum.
        val toReadPosition = readingStatuses.indexOf(ReadingStatus.TO_READ)

        binding.viewPager.registerOnPageChangeCallback(object :
            ViewPager2.OnPageChangeCallback() {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels)

                if (position == toReadPosition) {
                    // Si estamos en la pestaña «Pendientes» lo establecemos en visible,
                    // pero conforme se salga de la pestaña queremos restarle a la opacidad
                    // la posición de la pantalla en el ViewPager para que se vaya quitando.
                    binding.fabRecommendBooks.visibility = View.VISIBLE
                    //Evitamos que el valor alfa sea negativo.
                    binding.fabRecommendBooks.alpha = 0.5f * (1 - positionOffset)
                } else {
                    // En cualquier otro caso, queremos que esté invisible y transparente.
                    binding.fabRecommendBooks.alpha = 0f
                    binding.fabRecommendBooks.visibility = View.GONE
                }
            }

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                if (position == toReadPosition) {
                    binding.fabRecommendBooks.visibility = View.VISIBLE
                    binding.fabRecommendBooks.alpha = 0.5f
                } else {
                    binding.fabRecommendBooks.alpha = 0f
                    binding.fabRecommendBooks.visibility = View.GONE
                }
            }
        })
    }

}


