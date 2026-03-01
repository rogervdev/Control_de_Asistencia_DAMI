package edu.cibertec.controldeasistencia

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PanelEmpleadoActivity : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper
    lateinit var tvNombre: TextView
    lateinit var tvEstado: TextView
    lateinit var tvUltimaMarcacion: TextView
    lateinit var btnAccion: Button
    lateinit var containerHistorial: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_panel_empleado)

        dbHelper = DatabaseHelper(this)

        tvNombre = findViewById(R.id.tvNombreCompleto)
        tvEstado = findViewById(R.id.tvEstado)
        tvUltimaMarcacion = findViewById(R.id.tvUltimaMarcacion)
        btnAccion = findViewById(R.id.btnAccionAsistencia)
        containerHistorial = findViewById(R.id.containerHistorial)

        val dni = intent.getStringExtra("dni") ?: ""
        val hora = intent.getStringExtra("hora") ?: ""

        val empleado = dbHelper.obtenerEmpleadoPorDni(dni)

        if (empleado != null) {

            // Mostrar nombre completo correctamente
            tvNombre.text = "Hola, ${empleado.nombres} ${empleado.apellidos}"

            val ultimoRegistro = dbHelper.obtenerUltimoRegistro(dni)

            if (ultimoRegistro != null && ultimoRegistro.tipo == "ENTRADA") {

                tvEstado.text = "Estado: Presente 🟢"
                tvUltimaMarcacion.text = "Entrada registrada: ${ultimoRegistro.hora}"

                btnAccion.visibility = View.VISIBLE
                btnAccion.text = "Marcar Salida"

            } else {

                tvEstado.text = "Estado: No presente 🔴"
                tvUltimaMarcacion.text = "Última marcación: $hora"

                btnAccion.visibility = View.GONE
            }

            cargarHistorial(dni)

        } else {

            tvNombre.text = "Empleado no registrado"
            tvEstado.text = ""
            tvUltimaMarcacion.text = ""
            btnAccion.visibility = View.GONE
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarHistorial(dni: String) {

        val historial = dbHelper.obtenerUltimosRegistros(dni)

        containerHistorial.removeAllViews()

        for (registro in historial) {
            val tv = TextView(this)
            tv.text = "${registro.fecha} - ${registro.tipo} (${registro.hora})"
            tv.textSize = 14f
            tv.setPadding(0, 8, 0, 8)
            containerHistorial.addView(tv)
        }
    }
}