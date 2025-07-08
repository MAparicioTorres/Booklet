package com.maat.booklet_tfg_v1.ui.showdetailsbook

import android.animation.ArgbEvaluator
import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.maat.booklet_tfg_v1.R
import com.maat.booklet_tfg_v1.data.dto.BookWithAuthorAndGenre
import com.maat.booklet_tfg_v1.data.util.Constants.BOOK_ID_KEY
import com.maat.booklet_tfg_v1.databinding.ActivityBookDetailsBinding
import com.maat.booklet_tfg_v1.ui.addbook.AddBookActivity
import com.maat.booklet_tfg_v1.ui.viewmodel.BookViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class BookDetailsActivity : AppCompatActivity() {
    private val bookViewModel: BookViewModel by viewModels()
    private lateinit var binding: ActivityBookDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBookDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bookId = intent.getIntExtra(BOOK_ID_KEY, -1)
        if (bookId == -1) {
            //Si el libro no es válido, volvemos a la pantalla de inicio.
            finish()
            return
        }

        //Conseguimos los datos del libro por su ID y los mostramos por pantalla
        setupUi(bookId)

    }

    private fun setupToolbar(bookId: Int) {
        with(binding) {
            //Hacemos que la flecha hacia la izquierda nos permita volver a la pantalla de inicio.
            materialToolBar.setNavigationOnClickListener {
                onBackPressedDispatcher.onBackPressed()
            }

            //Creamos los listener para los botones de la barra superior.
            materialToolBar.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    //El botón del lápiz nos llevará al formulario de libros, pero pasaremos su ID a la actividad para poder precargar sus datos.
                    R.id.action_edit -> {
                        val intent = Intent(this@BookDetailsActivity, AddBookActivity::class.java)
                        intent.putExtra(BOOK_ID_KEY, bookId)
                        startActivity(intent)
                        true
                    }

                    //El botón de la papelera nos permitirá eliminar el libro.
                    R.id.action_delete -> {
                        //Creamos un mensaje de confirmación para eliminar el libro.
                        MaterialAlertDialogBuilder(this@BookDetailsActivity)
                            .setTitle("Eliminar libro")
                            .setMessage("¿Seguro que quieres eliminar el libro de la lista?")
                            .setPositiveButton("Aceptar") { _, _ ->
                                lifecycleScope.launch {
                                    //Eliminamos el libro mediante su ID.
                                    bookViewModel.deleteBookById(bookId)
                                    //Limpiamos la tabla Author si quedan autores que no estén en la tabla Book
                                    bookViewModel.deleteAuthorsWithNoBooks()
                                    //Volvemos a la pantalla de inicio.
                                    finish()
                                }
                            }
                            .setNegativeButton("Cancelar") { dialog, _ ->
                                dialog.dismiss()
                            }.show()

                        true //«True» porque hemos manejado los clics.
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupUi(bookId: Int) {
        //Obtenemos un flujo que se actualiza sobre el libro y cargamos todos sus datos.
        lifecycleScope.launch {
            bookViewModel.getBookWithAuthorAndGenreByIdFlow(bookId)
                .filterNotNull()
                .distinctUntilChanged()
                .collect { bookWithDetails ->
                    loadBookCover(bookWithDetails)
                    setupDates(bookWithDetails)
                    setupRating(bookWithDetails)
                    setupBookSpecifications(bookWithDetails)
                    setupSummary(bookWithDetails)
                    setupToolbar(bookId)
                }
        }
    }

    private fun setupSummary(bookWithDetails: BookWithAuthorAndGenre) {
        //Configuramos el resumen para que solo ocupe espacio y se vea si hay contenido, si no tiene se colapsará.
        with(binding) {
            if (bookWithDetails.book.summary.isNullOrBlank()) {
                textViewSummary.visibility = View.GONE
            } else {
                textViewSummary.visibility = View.VISIBLE
                textViewSummary.text = bookWithDetails.book.summary
            }
        }
    }

    private fun setupBookSpecifications(bookWithDetails: BookWithAuthorAndGenre) {
        with(binding) {
            textViewBookTitle.text = bookWithDetails.book.title
            textViewBookAuthor.text = bookWithDetails.author.authorName
            textViewBookGenre.text = bookWithDetails.genre.genreName
        }
    }

    private fun loadBookCover(bookWithDetails: BookWithAuthorAndGenre) {
        Glide.with(binding.root.context)
            .load(bookWithDetails.book.bookCoverUrl)
            .placeholder(R.drawable.no_book_cover)
            .error(R.drawable.no_book_cover)
            .into(binding.imageViewBookCover)
    }

    private fun setupDates(bookWithDetails: BookWithAuthorAndGenre) {
        //Configuramos las fechas para que solo ocupen espacio y se vean si hay contenido, si no tienen se colapsarán.
        with(binding) {
            if (bookWithDetails.book.startDate.isNullOrBlank() &&
                bookWithDetails.book.endDate.isNullOrBlank()
            ) {
                constraintLayoutDates.visibility = View.GONE
            } else {
                constraintLayoutDates.visibility = View.VISIBLE
                textViewBookStartDateValue.text = bookWithDetails.book.startDate
                textViewBookEndDateValue.text = bookWithDetails.book.endDate
            }
        }
    }

    private fun setupRating(bookWithDetails: BookWithAuthorAndGenre) {
        with(binding) {

            //Formateamos la nota del libro para dejar dos decimales y quitar los ceros sobrantes.
            val formatter = DecimalFormat("#.##")
            //Usamos «Locale.US» para que tenga punto y no coma como separador de decimales.
            formatter.decimalFormatSymbols = DecimalFormatSymbols(Locale.US)
            //Si existe un valor de nota lo usaremos, pero sino usaremos un guión («-») para simbolizar la ausencia de valor.
            val ratingStringFormatted = bookWithDetails.book.rating?.let { rating ->
                formatter.format(rating)
            } ?: "-"

            //Evaluamos qué color se establecerá como fondo según su nota.
            val colorInt = when (ratingStringFormatted) {
                "-" -> ContextCompat.getColor(this@BookDetailsActivity, R.color.black)
                "10" -> ContextCompat.getColor(this@BookDetailsActivity, R.color.diamond)
                //Obtenemos un color del degradado entre rojo y verde según la nota.
                else -> getRatingColor(ratingStringFormatted.toFloat())
            }

            //Obtenemos la instancia del fondo, pero la mutamos para que solo se modifique en esta instancia, no en todas.
            val backgroundDrawable =
                ContextCompat.getDrawable(this@BookDetailsActivity, R.drawable.rating_background)
                    ?.mutate()

            if (backgroundDrawable is GradientDrawable) {
                //Cambiamos el color del fondo.
                backgroundDrawable.setColor(colorInt)
            }

            //Asignamos el nuevo fondo mutado a la vista.
            textViewRatingBadge.background = backgroundDrawable
            textViewRatingBadge.text = ratingStringFormatted
        }

    }

    private fun getRatingColor(rating: Float): Int {
        val red = ContextCompat.getColor(this@BookDetailsActivity, R.color.dark_red)
        val green = ContextCompat.getColor(this@BookDetailsActivity, R.color.dark_green)
        //Dividimos entre 10 porque el valor de la fracción en ArgbEvaluator solo va de 0.00 a 1.00 y nos aseguramos de que los valores de la nota únicamente puedan estar entre 0 y 10.
        val fraction = (rating.coerceIn(0f, 10f)) / 10

        val argbEvaluator = ArgbEvaluator()
        //Obtenemos el color pertinente en el degradado con el tipo Int para usarlo como fondo.
        return argbEvaluator.evaluate(fraction, red, green) as Int
    }
}