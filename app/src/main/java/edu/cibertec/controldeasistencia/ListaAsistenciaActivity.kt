package edu.cibertec.controldeasistencia

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ListaAsistenciaActivity : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper
    lateinit var recycler: RecyclerView
    lateinit var tvSinDatos: TextView
    lateinit var tvFecha: TextView
    lateinit var etBusqueda: MaterialAutoCompleteTextView
    lateinit var adapter: AsistenciaAdapter

    private var listaCompleta = listOf<AsistenciaItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_lista_asistencia)

        dbHelper = DatabaseHelper(this)

        recycler = findViewById(R.id.recyclerViewAsistencia)
        tvSinDatos = findViewById(R.id.tvSinDatos)
        tvFecha = findViewById(R.id.tvFechaActual)
        etBusqueda = findViewById(R.id.etBusqueda)

        recycler.layoutManager = LinearLayoutManager(this)

        val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())

        val fechaMostrar = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", Locale("es"))
            .format(Date())

        tvFecha.text = "Hoy, $fechaMostrar"

        cargarAsistencia(fechaActual)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarAsistencia(fecha: String) {

        listaCompleta = dbHelper.obtenerAsistenciaDelDia(fecha)

        if (listaCompleta.isEmpty()) {
            recycler.visibility = View.GONE
            tvSinDatos.visibility = View.VISIBLE
        } else {
            recycler.visibility = View.VISIBLE
            tvSinDatos.visibility = View.GONE

            adapter = AsistenciaAdapter(listaCompleta)
            recycler.adapter = adapter

            configurarAutoComplete()
        }
    }

    private fun configurarAutoComplete() {

        val nombres = listaCompleta.map { it.nombreCompleto }.distinct()

        val adapterAuto = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombres
        )

        etBusqueda.setAdapter(adapterAuto)

        // Cuando seleccionan un nombre
        etBusqueda.setOnItemClickListener { parent, _, position, _ ->

            val nombreSeleccionado = parent.getItemAtPosition(position).toString()

            val filtrada = listaCompleta.filter {
                it.nombreCompleto == nombreSeleccionado
            }

            adapter = AsistenciaAdapter(filtrada)
            recycler.adapter = adapter
        }

        // Si borran el texto, vuelve a mostrar todo
        etBusqueda.setOnDismissListener {
            if (etBusqueda.text.toString().isEmpty()) {
                recycler.adapter = AsistenciaAdapter(listaCompleta)
            }
        }
    }
}