package edu.cibertec.controldeasistencia

data class AsistenciaItem(
    val id: Int,
    val nombreCompleto: String,
    val fecha: String,
    val hora: String,
    val tipo: String
)
