package edu.cibertec.controldeasistencia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AsistenciaAdapter(
    private var lista: List<AsistenciaItem>
) : RecyclerView.Adapter<AsistenciaAdapter.ViewHolder>() {

    fun actualizarLista(nuevaLista: List<AsistenciaItem>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvHora: TextView = itemView.findViewById(R.id.tvHora)
        val tvTipo: TextView = itemView.findViewById(R.id.tvTipo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_asistencia, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = lista.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = lista[position]
        holder.tvNombre.text = item.nombreCompleto
        holder.tvHora.text = "Hora: ${item.hora}"
        holder.tvTipo.text = "Tipo: ${item.tipo}"
    }
}