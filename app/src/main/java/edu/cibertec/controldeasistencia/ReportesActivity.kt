package edu.cibertec.controldeasistencia
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ReportesActivity : AppCompatActivity() {

    private lateinit var asistenciaAdapter: AsistenciaAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var btnGenerar: Button
    private lateinit var etFechaInicio: TextInputEditText
    private lateinit var etFechaFin: TextInputEditText
    private lateinit var tvSinDatos: TextView
    private lateinit var containerResumen: LinearLayout
    private lateinit var tvTotalAsistencias: TextView
    private lateinit var tvTotalFaltas: TextView
    private lateinit var tvHorasExtras: TextView
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_reportes)

        recyclerView = findViewById(R.id.recyclerViewDetalle)
        btnGenerar = findViewById(R.id.btnGenerarReporte)
        etFechaInicio = findViewById(R.id.etFechaInicio)
        etFechaFin = findViewById(R.id.etFechaFin)
        tvSinDatos = findViewById(R.id.tvSinDatos)
        containerResumen = findViewById(R.id.containerResumen)
        tvTotalAsistencias = findViewById(R.id.tvTotalAsistencias)
        tvTotalFaltas = findViewById(R.id.tvTotalFaltas)
        tvHorasExtras = findViewById(R.id.tvHorasExtras)

        dbHelper = DatabaseHelper(this)

        recyclerView.layoutManager = LinearLayoutManager(this)
        asistenciaAdapter = AsistenciaAdapter(emptyList())
        recyclerView.adapter = asistenciaAdapter

        btnGenerar.setOnClickListener { generarReporte() }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun generarReporte() {
        val fechaInicio = etFechaInicio.text.toString()
        val fechaFin = etFechaFin.text.toString()

        if (fechaInicio.isEmpty() || fechaFin.isEmpty()) {
            Toast.makeText(this, "Selecciona un rango de fechas", Toast.LENGTH_SHORT).show()
            return
        }

        val listaReporte = mutableListOf<AsistenciaItem>()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        var fechaActual = sdf.parse(fechaInicio)!!
        val fechaFinDate = sdf.parse(fechaFin)!!

        while (!fechaActual.after(fechaFinDate)) {
            val fechaStr = sdf.format(fechaActual)
            listaReporte.addAll(dbHelper.obtenerAsistenciaDelDia(fechaStr))

            val cal = Calendar.getInstance()
            cal.time = fechaActual
            cal.add(Calendar.DAY_OF_MONTH, 1)
            fechaActual = cal.time
        }

        if (listaReporte.isEmpty()) {
            tvSinDatos.visibility = View.VISIBLE
            containerResumen.visibility = View.GONE
            asistenciaAdapter.actualizarLista(emptyList())
        } else {
            tvSinDatos.visibility = View.GONE
            containerResumen.visibility = View.VISIBLE

            val totalAsistencias = listaReporte.count { it.tipo.equals("Entrada", true) }
            val totalFaltas = listaReporte.count { it.tipo.equals("Falta", true) }
            val totalHorasExtras = listaReporte.count { it.tipo.equals("SalidaExtra", true) }

            tvTotalAsistencias.text = "Asistencias: $totalAsistencias"
            tvTotalFaltas.text = "Faltas: $totalFaltas"
            tvHorasExtras.text = "Horas extras: ${totalHorasExtras}h"

            asistenciaAdapter.actualizarLista(listaReporte)
        }
    }
}