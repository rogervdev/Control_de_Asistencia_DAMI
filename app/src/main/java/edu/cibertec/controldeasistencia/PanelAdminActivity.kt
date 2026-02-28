package edu.cibertec.controldeasistencia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PanelAdminActivity : AppCompatActivity() {

    lateinit var btnListaAsistencia : Button
    lateinit var btnRegistroManualAsistencia : Button

    lateinit var btnGestionarEmpleados : Button

    lateinit var btnGenerarReporte : Button

    lateinit var btnCerrarSesion : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_admin_dashboard)


        btnListaAsistencia = findViewById<Button>(R.id.btnVerAsistencia)
        btnRegistroManualAsistencia = findViewById<Button>(R.id.btnRegistrarManual)
        btnGestionarEmpleados = findViewById<Button>(R.id.btnGestionEmpleados)
        btnGenerarReporte = findViewById<Button>(R.id.btnReportes)

        btnCerrarSesion = findViewById<Button>(R.id.btnCerrarSesion)


        btnListaAsistencia.setOnClickListener {
            startActivity(Intent(this, ListaAsistenciaActivity::class.java))
        }


        btnRegistroManualAsistencia.setOnClickListener {
            startActivity(Intent(this, RegistroManualActivity::class.java))
        }

        btnGestionarEmpleados.setOnClickListener {
            startActivity(Intent(this, GestionEmpleadosActivity::class.java))
        }

        btnGenerarReporte.setOnClickListener {
            startActivity(Intent(this, ReportesActivity::class.java))
        }

        btnCerrarSesion.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }



        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}