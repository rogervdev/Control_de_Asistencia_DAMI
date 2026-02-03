package edu.cibertec.controldeasistencia

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.zxing.integration.android.IntentIntegrator
import java.time.LocalDate
import java.time.LocalTime

class MainActivity : AppCompatActivity() {

    private lateinit var btnEscanearQR: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // 🔹 Referencia al botón
        btnEscanearQR = findViewById(R.id.btnEscanearQR)

        // 🔹 Acción del botón
        btnEscanearQR.setOnClickListener {
            abrirScanner()
        }
    }

    private fun abrirScanner() {
        val integrator = IntentIntegrator(this)
        integrator.setPrompt("Escanee el código QR")
        integrator.setBeepEnabled(true)
        integrator.setOrientationLocked(true)
        integrator.initiateScan()
    }

    // 🔹 Resultado del escaneo
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {

                val fecha = LocalDate.now().toString()
                val hora = LocalTime.now().toString()

                // 👉 Aquí luego irá la BD
                Toast.makeText(
                    this,
                    "Asistencia registrada\nFecha: $fecha\nHora: $hora",
                    Toast.LENGTH_LONG
                ).show()

            } else {
                Toast.makeText(this, "Escaneo cancelado", Toast.LENGTH_SHORT).show()
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
