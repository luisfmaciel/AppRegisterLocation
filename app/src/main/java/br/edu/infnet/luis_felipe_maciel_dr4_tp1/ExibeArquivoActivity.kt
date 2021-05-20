package br.edu.infnet.luis_felipe_maciel_dr4_tp1

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_exibe_arquivo.*
import kotlinx.android.synthetic.main.activity_registros.*
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.lang.Exception

class ExibeArquivoActivity : AppCompatActivity() {

    private val READ_EXTERNAL = Manifest.permission.READ_EXTERNAL_STORAGE
    private val REQUEST_PERMISSION_CODE = 1009
    private val GRANTED = PackageManager.PERMISSION_GRANTED

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exibe_arquivo)
        actionLerExterno()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        super.onBackPressed()
        finish()
        return true
    }

    fun actionLerExterno(){
        if (FILE_NAME != null) {
            when {
                checkSelfPermission(READ_EXTERNAL) == GRANTED -> readAExternalFile()
                shouldShowRequestPermissionRationale(READ_EXTERNAL) -> showDialogPermission(
                    "É preciso liberar o acesso ao armazenamento externo!",
                    arrayOf(READ_EXTERNAL)
                )
                else -> requestPermissions(
                    arrayOf(READ_EXTERNAL),
                    REQUEST_PERMISSION_CODE
                )
            }
            FILE_NAME = null
        }
    }

    private fun readAExternalFile(){
        val file = File(getExternalFilesDir(null), FILE_NAME!!)
        if (file.exists())
            try {
                BufferedReader(FileReader(file)).let {
                    tv_exibe_coordenada.text = it.readText()
                    tv_nome_arquivo_exibe.text = FILE_NAME
                }
            } catch (e: Exception){
                showSnackbar("${e.message}")
            }
        else
            showSnackbar("Arquivo não existe")
    }

    private fun showDialogPermission(
        message: String, permissions: Array<String>
    ) {
        val alertDialog = AlertDialog
            .Builder(this)
            .setTitle("Permissões")
            .setMessage(message)
            .setPositiveButton("Ok") { dialog, _ ->
                requestPermissions(
                    permissions,
                    REQUEST_PERMISSION_CODE)
                dialog.dismiss()
            }.setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
        alertDialog.show()
    }

    private fun showSnackbar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar
            .make(
                root_exibe_layout,
                message,
                duration
            ).show()
    }

}