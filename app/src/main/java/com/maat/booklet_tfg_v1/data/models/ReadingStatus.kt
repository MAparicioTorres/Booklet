package com.maat.booklet_tfg_v1.data.models

//Creamos un tipo de dato personalizado para nuestra BD.
enum class ReadingStatus(val label: String) {
    //Agregamos etiquetas para darles nombre.
    TO_READ("Pendiente"),
    READING("Leyendo"),
    READ("Leído");

    override fun toString(): String {
        //Sobreescribimos el «toString()» para devolver su etiqueta y no la entrada del enum.
        return label
    }
}

