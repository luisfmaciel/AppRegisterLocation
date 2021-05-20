package br.edu.infnet.luis_felipe_maciel_dr4_tp1

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.registros_card.view.*

class RegistrosAdapter(var listaRegistros: List<String> = listOf()):
    RecyclerView.Adapter<RegistrosAdapter.RegistrosViewholder>() {

    class RegistrosViewholder(itemView: View):
        RecyclerView.ViewHolder(itemView) {
        val nomeArquivo: TextView = itemView.tv_nome_arquivo
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): RegistrosViewholder {
        val card = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.registros_card, parent, false)

        return RegistrosViewholder(card)
    }

    override fun getItemCount() = listaRegistros.size

    override fun onBindViewHolder(holder: RegistrosViewholder,
                                 position: Int) {
        val arquivo = listaRegistros[position]
        holder.nomeArquivo.text = arquivo
        holder.itemView.setOnClickListener {
            FILE_NAME = arquivo
            it.context.startActivity(Intent(it.context, ExibeArquivoActivity::class.java))
        }
    }

    fun mudarDados(registros: List<String>){
        listaRegistros = registros
        notifyDataSetChanged()
    }
}
