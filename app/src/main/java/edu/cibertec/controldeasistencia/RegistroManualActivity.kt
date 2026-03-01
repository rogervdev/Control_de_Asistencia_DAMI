package edu.cibertec.controldeasistencia

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class RegistroManualActivity : AppCompatActivity() {

    lateinit var dbHelper: DatabaseHelper
    lateinit var actvEmpleado: AutoCompleteTextView
    lateinit var radioGroup: RadioGroup
    lateinit var etHora: EditText
    lateinit var btnRegistrar: Button

    private var empleadoSeleccionado: Empleado? = null
    private lateinit var listaEmpleados: List<Empleado>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_registro_manual)

        dbHelper = DatabaseHelper(this)

        actvEmpleado = findViewById(R.id.actvEmpleado)
        radioGroup = findViewById(R.id.radioGroupTipo)
        etHora = findViewById(R.id.etHora)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        cargarEmpleados()

        btnRegistrar.setOnClickListener {
            registrarAsistencia()
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun cargarEmpleados() {
        listaEmpleados = dbHelper.obtenerEmpleados()

        val nombres = listaEmpleados.map {
            "${it.nombres} ${it.apellidos}"
        }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            nombres
        )

        actvEmpleado.setAdapter(adapter)

        actvEmpleado.setOnItemClickListener { _, _, position, _ ->
            empleadoSeleccionado = listaEmpleados[position]
        }
    }

    private fun registrarAsistencia() {

        if (empleadoSeleccionado == null) {
            Toast.makeText(this, "Seleccione un empleado", Toast.LENGTH_SHORT).show()
            return
        }

        val tipo = if (radioGroup.checkedRadioButtonId == R.id.rbEntrada)
            "Entrada"
        else
            "Salida"

        val horaIngresada = etHora.text.toString().ifEmpty {
            SimpleDateFormat("HH:mm", Locale.getDefault())
                .format(Date())
        }

        val fechaActual = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            .format(Date())

        val resultado = dbHelper.insertarAsistencia(
            empleadoSeleccionado!!.id,
            fechaActual,
            horaIngresada,
            tipo
        )

        if (resultado) {
            Toast.makeText(this, "Asistencia registrada", Toast.LENGTH_SHORT).show()
            finish()
        } else {
            Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show()
        }
    }
}