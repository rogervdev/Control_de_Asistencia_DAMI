package edu.cibertec.controldeasistencia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class GestionEmpleadosActivity : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper
    lateinit var recycler: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gestion_empleados)

        dbHelper = DatabaseHelper(this)

        recycler = findViewById(R.id.recyclerEmpleados)
        recycler.layoutManager = LinearLayoutManager(this)

        cargarEmpleados()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onResume() {
        super.onResume()
        cargarEmpleados()
    }

    private fun cargarEmpleados() {
        val lista = dbHelper.obtenerEmpleados()
        recycler.adapter = EmpleadoAdapter(lista)
    }
}