package edu.cibertec.controldeasistencia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class EmpleadoAdapter(
    private val lista: List<Empleado>
) : RecyclerView.Adapter<EmpleadoAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombreCompleto: TextView = itemView.findViewById(R.id.tvNombreCompleto)
        val tvDni: TextView = itemView.findViewById(R.id.tvDni)
        val tvTelefono: TextView = itemView.findViewById(R.id.tvTelefono)
        val tvId: TextView = itemView.findViewById(R.id.tvId)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_empleado_gestion, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val empleado = lista[position]

        holder.tvNombreCompleto.text = "${empleado.nombres} ${empleado.apellidos}"
        holder.tvDni.text = "DNI: ${empleado.dni}"
        holder.tvTelefono.text = "Teléfono: ${empleado.telefono}"
        holder.tvId.text = "ID: ${empleado.id}"
    }
}