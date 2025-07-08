package com.maat.booklet_tfg_v1.ui.addbook

import android.app.DatePickerDialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.os.Bundle
import android.text.Editable
import android.text.SpannableString
import android.text.Spanned
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.maat.booklet_tfg_v1.data.entities.Author
import com.maat.booklet_tfg_v1.data.entities.Book
import com.maat.booklet_tfg_v1.data.entities.Genre
import com.maat.booklet_tfg_v1.data.models.ReadingStatus
import com.maat.booklet_tfg_v1.data.util.Constants.BOOK_ID_KEY
import com.maat.booklet_tfg_v1.databinding.ActivityAddBookBinding
import com.maat.booklet_tfg_v1.ui.viewmodel.AuthorViewModel
import com.maat.booklet_tfg_v1.ui.viewmodel.BookViewModel
import com.maat.booklet_tfg_v1.ui.viewmodel.GenreViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddBookActivity : AppCompatActivity() {
    private val bookId by lazy {
        intent.getIntExtra(BOOK_ID_KEY, -1)
    }
    private lateinit var binding: ActivityAddBookBinding
    private lateinit var genreList: List<Genre>
    private val bookViewModel: BookViewModel by viewModels()
    private val genreViewModel: GenreViewModel by viewModels()
    private val authorViewModel: AuthorViewModel by viewModels()
    private val startCalendar = Calendar.getInstance()
    private val endCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configuramos la interfaz.
        setupUi(bookId)

        // Si existe el ID del libro, poblamos los campos de formulario.
        if (bookId != -1) {
            lifecycleScope.launch {
                populateFieldsByBookId(bookId)
            }
        }
    }

    private fun labelToReadingStatus(label: String): ReadingStatus? {
        //Encontramos la entrada del estado del libro que coincida con la String.
        return ReadingStatus.entries.find { it.label == label }
    }

    private suspend fun populateFieldsByBookId(bookId: Int) {
        //Obtenemos el flujo de los datos del libro, pero este solo nos proporcionará un único objeto
        //a no ser que se actualice alguno de sus valores, en cuyo caso cambiará, de ahí el «distinctUntilChanged()».
        bookViewModel.getBookWithAuthorAndGenreByIdFlow(bookId).distinctUntilChanged()
            .collect { bookWithDetails ->
                with(binding) {
                    bookWithDetails?.let { bookWithDetails ->
                        //Rellenamos los campos con los valores existentes en la BD.
                        editTextBookCoverUrl.setText(bookWithDetails.book.bookCoverUrl)
                        editTextTitle.setText(bookWithDetails.book.title)
                        editTextAuthor.setText(bookWithDetails.author.authorName)
                        //Establecemos filter a «false» para que podamos volver a elegir otro género de lectura del menú desplegable.
                        autoCompleteTextViewGenre.setText(bookWithDetails.genre.genreName, false)
                        //Establecemos filter a «false» para que podamos volver a elegir otro estado de lectura del menú desplegable.
                        autoCompleteTextViewReadingStatus.setText(
                            bookWithDetails.book.readingStatus.label,
                            false
                        )
                        editTextStartDate.setText(bookWithDetails.book.startDate)
                        editTextEndDate.setText(bookWithDetails.book.endDate)
                        //Si no tenemos una nota almacenada en la BD, mostramos el campo vacío.
                        if (bookWithDetails.book.rating != null) editTextRating.setText(
                            bookWithDetails.book.rating.toString()
                        ) else editTextRating.setText("")
                        editTextSummary.setText(bookWithDetails.book.summary)
                    }
                }
            }
    }

    private fun setupUi(bookId: Int) {
        //Configuramos todas las vistas que componen el layout.
        setupGenreSpinner()
        setupDatePickersDialogs()
        setupRatingTextCheckListener()
        setupSaveButtonClickListener(bookId)
        setupCancelButtonClickListener()
        setupSummaryButtonClickListener()
        setupReadingStatusSpinner()
    }

    private fun setupReadingStatusSpinner() {
        with(binding) {
            //Obtenemos los estados de lectura de ReadingStatus en una Array para mostrarlos en el menú deplegable.
            val readingStatuses = ReadingStatus.entries.toTypedArray()
            val readingStatusAdapter = ArrayAdapter(
                this@AddBookActivity,
                android.R.layout.simple_list_item_1,
                readingStatuses
            )
            //Establecemos el adaptador.
            autoCompleteTextViewReadingStatus.setAdapter(readingStatusAdapter)


            if (bookId == -1) {
                //Asignamos «Pendiente» como predefinido si estamos añadiendo el libro y no actualizándolo.
                autoCompleteTextViewReadingStatus.setText(ReadingStatus.TO_READ.label, false)
            }
        }
    }


    private fun setupSummaryButtonClickListener() {
        with(binding) {
            //Mostramos un mensaje de confirmación al clicar en el botón para eliminar los valores del resumen.
            textInputLayoutSummary.setEndIconOnClickListener {
                MaterialAlertDialogBuilder(this@AddBookActivity)
                    .setTitle("Borrar resumen")
                    .setMessage("¿Quieres borrar el resumen?")
                    .setPositiveButton("Aceptar") { _, _ ->
                        editTextSummary.text?.clear()
                    }
                    .setNegativeButton("Cancelar") { dialog, _ ->
                        dialog.dismiss()
                    }.show()
            }
        }
    }

    private fun setupCancelButtonClickListener() {
        binding.buttonBack.setOnClickListener {
            //Mostramos un mensaje de confirmación al clicar en el botón «Volver».
            MaterialAlertDialogBuilder(this@AddBookActivity)
                .setTitle("Salir")
                .setMessage("¿Quieres salir?")
                .setPositiveButton("Aceptar") { _, _ ->
                    finish()
                }
                .setNegativeButton("Cancelar") { dialog, _ ->
                    dialog.dismiss()
                }.show()

        }
    }

    private fun setupSaveButtonClickListener(bookId: Int) {
        with(binding) {
            //Cambiamos de acción según para qué se acceda al formulario.
            val action = if (bookId != -1) "actualizar" else "añadir"
            buttonSave.text = action.replaceFirstChar { it.uppercaseChar() }

            buttonSave.setOnClickListener {
                //Evaluamos los campos obligatorios para ver si son válidos.
                val isTitleValid = validateField(editTextTitle, textInputLayoutTitle, "Título")
                val isAuthorValid = validateField(editTextAuthor, textInputLayoutAuthor, "Autor")
                val isGenreValid =
                    validateField(autoCompleteTextViewGenre, textInputLayoutGenre, "Género")

                if (isTitleValid && isAuthorValid && isGenreValid) {

                    //Creamos un mensaje personalizado según la acción que queremos realizar.
                    val message =
                        "¿Seguro que quieres $action ${editTextTitle.text.toString()}?"
                    val spannable = SpannableString(message)
                    val startIndex = message.indexOf(editTextTitle.text.toString())
                    val endIndex = startIndex + editTextTitle.text.toString().length

                    //Establecemos el título del libro en el mensaje en negrita e itálica
                    spannable.setSpan(
                        StyleSpan(Typeface.BOLD_ITALIC),
                        startIndex,
                        endIndex,
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                    )

                    //Mostramos un mensaje para confirmar si queremos añadir el libro.
                    MaterialAlertDialogBuilder(this@AddBookActivity)
                        .setTitle("${action.replaceFirstChar { it.uppercaseChar() }} libro")
                        .setMessage(spannable)
                        .setPositiveButton("Aceptar") { _, _ ->
                            lifecycleScope.launch {
                                //Evaluamos si el autor ya existe en la BD o se debe de crear un nuevo registro.
                                val authorId = getValidExistingAuthorId(editTextAuthor)
                                //Obtenemos el ID del género en base al nombre que hemos seleccionado en el menú desplegable.
                                val genreId = genreViewModel.getGenreIdByGenreName(
                                    autoCompleteTextViewGenre.text.toString().trim()
                                )
                                // Creamos un objeto libro con el ID si «bookId» existe en nuestra BD, sino lo hacemos sin dicho campo porque será un nuevo registro.
                                val book = getFilledBook(authorId, bookId, genreId)


                                if (bookId != -1) {
                                    //Si el «bookId» existe, significa que tenemos un registro coincidente en la BD, por lo que lo actualizamos.
                                    bookViewModel.updateBook(book)
                                    //Hacemos limpieza de los autores que no tengan referencias en la tabla Book.
                                    bookViewModel.deleteAuthorsWithNoBooks()
                                } else {
                                    //Si el «bookId» es -1 significa que es un nuevo registro, por lo que tenemos que hacer «INSERT».
                                    bookViewModel.insertBook(
                                        book
                                    )
                                }
                            }
                            //Volvemos a la pantalla anterior, para que el usuario no se quede en el formulario.
                            finish()
                        }
                        .setNegativeButton("Cancelar") { dialog, _ ->
                            dialog.dismiss()
                        }.show()

                }
            }
        }
    }

    private fun getFilledBook(authorId: Int, bookId: Int, genreId: Int): Book {
        with(binding) {

            //Obtenemos un objeto libro con los campos tratados.
            val book = Book(
                //Eliminamos los espacios de delante y detrás.
                bookCoverUrl = editTextBookCoverUrl.text.toString().trim(),
                //Eliminamos espacios dobles o superiores, además de los anteriores y posteriores.
                title = editTextTitle.text.toString().trim().replace(Regex("\\s+"), " "),
                authorId = authorId,
                genreId = genreId,
                //Obtenemos el estado de la lectura del campo seleccionado en el formulario, si devuelve nulo lo establecemos de forma predefinida a 'Pendiente'.
                readingStatus = labelToReadingStatus(autoCompleteTextViewReadingStatus.text.toString())
                    ?: ReadingStatus.TO_READ,
                startDate = editTextStartDate.text.toString(),
                endDate = editTextEndDate.text.toString(),
                summary = editTextSummary.text.toString(),
                //Si el campo solo tiene espacios o es nulo, devolvemos nulo a la BD, sino el número convertido a Float.
                rating = if (editTextRating.text.isNullOrBlank()) null else editTextRating.text.toString()
                    .toFloat()
            )

            //En el caso de que el «bookId» ya exista, hacemos una copia del objeto book añadiendo este campo, sino devolvemos el original.
            return if (bookId != -1) book.copy(bookId = bookId) else book
        }
    }


    private fun String.removeAccents(): String {
        //Creamos un método de extensión de la clase String para eliminar los acentos de una cadena.
        return Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("\\p{InCombiningDiacriticalMarks}+".toRegex(), "")
    }


    private suspend fun getValidExistingAuthorId(editTextAuthor: TextInputEditText): Int {
        //Obtenemos el valor tratado para la BD.
        val inputForDatabase = editTextAuthor.text.toString().trim().replace(Regex("\\s+"), " ")
        //Le quitamos al valor tratado los acentos y lo convertimos a minúsculas para las comparaciones.
        val normalizedInput = inputForDatabase.lowercase().removeAccents()

        val authorsList = authorViewModel.getNormalizedAuthorsNameList()

        val matchingAuthor = authorsList.find { author ->
            //Comparamos el nombre de los autores en minúsculas, sin acentos, sin espacios previos y posteriores y sin más de un espacio entre palabras para comprobar si ya existe en la BD.
            author.normalizedAuthorName.trim().removeAccents()
                .replace(Regex("\\s+"), " ") == normalizedInput
        }

        return if (matchingAuthor != null) {
            //Si existe en la BD, le asignamos su ID.
            val author = Author(
                authorId = matchingAuthor.authorId,
                authorName = inputForDatabase
            )
            authorViewModel.updateAuthor(author)
            matchingAuthor.authorId
        } else {
            //Si no existe, creamos un nuevo registro.
            authorViewModel.insertAuthorAndGetId(inputForDatabase)
        }

    }

    private fun validateField(
        autoComplete: AutoCompleteTextView,
        textInputLayout: TextInputLayout,
        fieldName: String
    ): Boolean {
        //Validamos que los campos obligatorios no estén vacíos o rellenos por espacios en los AutoCompleteTextView.
        return if (autoComplete.text.toString().trim().isEmpty()) {
            textInputLayout.error = "$fieldName obligatorio"
            textInputLayout.isErrorEnabled = true
            false
        } else {
            textInputLayout.error = null
            textInputLayout.isErrorEnabled = false
            true
        }
    }

    private fun validateField(
        editText: EditText,
        textInputLayout: TextInputLayout,
        fieldName: String,
    ): Boolean {
        //Validamos que los campos obligatorios no estén vacíos o rellenos por espacios en los EditText.
        return if (editText.text.toString().trim().isEmpty()) {
            textInputLayout.error = "$fieldName obligatorio"
            textInputLayout.isErrorEnabled = true
            false
        } else {
            textInputLayout.error = null
            textInputLayout.isErrorEnabled = false
            true
        }
    }

    private fun setupRatingTextCheckListener() {
        with(binding) {
            editTextRating.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun onTextChanged(
                    s: CharSequence?,
                    start: Int,
                    before: Int,
                    count: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {
                    val input = s.toString()

                    //Evitamos que el usuario pueda introducir punto como primer valor.
                    if (input.startsWith('.')) {
                        s?.delete(0, 1)
                    } else {
                        val value = input.toDoubleOrNull()
                        if (value != null) {
                            //Si el valor es superior a 10 o tiene más de dos decimales, mostramos un mensaje de error y limpiamos el campo para que no se pueda introducir un valor inválido.
                            if (value !in 0.00..10.00 ||
                                !hasTwoDecimalPlacesOrLess(input)
                            ) {
                                editTextRating.error = "Introduce un valor entre 0.00 y 10.00"
                                editTextRating.setText("")
                            } else {
                                //Si se rellena el campo correctamente, eliminamos el mensaje de error.
                                editTextRating.error = null
                            }
                        }
                    }
                }
            })
        }
    }

    fun hasTwoDecimalPlacesOrLess(input: String): Boolean {
        //Evaluamos si una cadena tiene más de dos decimales.
        return input.matches(Regex("""\d+(\.\d{0,2})?"""))
    }

    private fun setupDatePickersDialogs() {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        //Configuramos la acción del clic de la fecha de inicio.
        binding.editTextStartDate.setOnClickListener {

            val year = startCalendar.get(Calendar.YEAR)
            val month = startCalendar.get(Calendar.MONTH)
            val day = startCalendar.get(Calendar.DAY_OF_MONTH)

            //Creamos la ventana emergente para seleccionar la fecha mediante un calendario.
            DatePickerDialog(
                this@AddBookActivity,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->

                    startCalendar.set(selectedYear, selectedMonth, selectedDay)
                    val startDateFormatted = dateFormat.format(startCalendar.time)
                    binding.editTextStartDate.setText(startDateFormatted)

                }, year, month, day
            ).apply {
                //Solo permitimos escoger días previos a la fecha de finalización.
                if (!binding.editTextEndDate.text.isNullOrEmpty()) {
                    val dateText = binding.editTextEndDate.text.toString()
                    val maxDate = getCalendarWithoutLeadingZeroes(dateText)
                    //La fecha máxima será la fecha que hayamos escogido como fecha de finalización.
                    datePicker.maxDate = maxDate.timeInMillis
                }

                //Añadimos un botón para eliminar la fecha al calendario.
                setButton(
                    DialogInterface.BUTTON_NEUTRAL,
                    "Eliminar",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            binding.editTextStartDate.text?.clear()
                            //Reiniciamos la fecha al día actual.
                            resetDateToToday(startCalendar)
                        }
                    })

                //Mostramos el calendario para la fecha de inicio.
                show()
            }
        }

        //Configuramos la acción del clic de la fecha de finalización.
        binding.editTextEndDate.setOnClickListener {
            val year = endCalendar.get(Calendar.YEAR)
            val month = endCalendar.get(Calendar.MONTH)
            val day = endCalendar.get(Calendar.DAY_OF_MONTH)

            //Creamos la ventana emergente para seleccionar la fecha mediante un calendario.
            DatePickerDialog(
                this@AddBookActivity,
                DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                    endCalendar.set(selectedYear, selectedMonth, selectedDay)
                    val endDateFormatted = dateFormat.format(endCalendar.time)
                    binding.editTextEndDate.setText(endDateFormatted)

                }, year, month, day
            ).apply {

                //Solo permitimos escoger días posteriores a la fecha de inicio.
                if (!binding.editTextStartDate.text.isNullOrEmpty()) {
                    val dateText = binding.editTextStartDate.text.toString()
                    val minDate = getCalendarWithoutLeadingZeroes(dateText)
                    //La fecha mínima será la fecha que hayamos escogido como fecha de inicio.
                    datePicker.minDate = minDate.timeInMillis
                }

                //Añadimos un botón para eliminar la fecha al calendario.
                setButton(
                    DialogInterface.BUTTON_NEUTRAL,
                    "Eliminar",
                    object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface?, which: Int) {
                            binding.editTextEndDate.text?.clear()
                            //Reiniciamos la fecha al día actual.
                            resetDateToToday(endCalendar)
                        }
                    })

                //Mostramos el calendario para la fecha de finalización.
                show()
            }

        }
    }

    private fun getCalendarWithoutLeadingZeroes(dateText: String): Calendar {
        //Obtenemos una instancia del calendario sin ceros antepuestos.
        val date = dateText
        val day = date.slice(0..1).replace(Regex("^0+"), "").toInt()
        val month = date.slice(3..4).replace(Regex("^0+"), "").toInt() - 1
        val year = date.slice(6..9).toInt()

        val cal = Calendar.getInstance().apply {
            set(year, month, day)
        }
        return cal
    }

    private fun resetDateToToday(calendar: Calendar) {
        //Reiniciamos la fecha del calendario a la actual.
        val cal = Calendar.getInstance()
        calendar.set(
            cal.get(Calendar.YEAR),
            cal.get(Calendar.MONTH),
            cal.get(Calendar.DAY_OF_MONTH)
        )
    }

    private fun setupGenreSpinner() {
        lifecycleScope.launch {
            genreViewModel.genres.collect { genres ->
                genreList = genres
                //Obtenemos una lista de los nombres de los géneros.
                val genreNames = genres.map { genre -> genre.genreName }

                //Creamos el adaptador para mostrar los nombres de la lista.
                val adapter = ArrayAdapter(
                    this@AddBookActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    genreNames
                )

                //Asignamos el adaptador.
                binding.autoCompleteTextViewGenre.setAdapter(adapter)
            }
        }
    }
}