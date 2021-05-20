package br.edu.infnet.luis_felipe_maciel_dr4_tp1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_registros.*

class RegistrosActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registros)

        configurarRecyclerView()
        subscribe()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        finish()
        return true
    }

    private fun configurarRecyclerView(){
        recyclerView_registros.layoutManager = LinearLayoutManager(this)
        recyclerView_registros.adapter = RegistrosAdapter()
    }

    private fun subscribe() {
        val adapter = recyclerView_registros.adapter
        if (adapter is RegistrosAdapter) {
            adapter.mudarDados(listaRecyclerView)
        }
    }


}