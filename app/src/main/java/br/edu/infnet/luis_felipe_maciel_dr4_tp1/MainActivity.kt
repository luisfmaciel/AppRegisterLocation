package br.edu.infnet.luis_felipe_maciel_dr4_tp1

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private val REQUEST_PERMISSION_CODE = 1009
    private val GRANTED = PackageManager.PERMISSION_GRANTED
    private val FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION
    private val EXTERNAL = Manifest.permission.WRITE_EXTERNAL_STORAGE
    private var LATITUDE = ""
    private var LONGITUDE = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun actionEscreverExterno(view: View){
        when {
            checkSelfPermission(EXTERNAL) == GRANTED -> writeInAExternalFile()
            shouldShowRequestPermissionRationale(EXTERNAL) -> showDialogPermission(
                "É preciso liberar o acesso ao armazenamento externo!",
                arrayOf(EXTERNAL)
            )
            else -> requestPermissions(
                arrayOf(EXTERNAL),
                REQUEST_PERMISSION_CODE
            )
        }
    }

    private fun writeInAExternalFile(){
        val nomeArquivo = "${obterDataEHora()}.crd"
        showSnackbar(nomeArquivo)
        val file = File(getExternalFilesDir(null), nomeArquivo)
        if (file.exists()) {
            file.delete()
        } else {
            try {
                FileOutputStream(file).let {
                    it.write("Lat: $LATITUDE Log: $LONGITUDE".toByteArray())
                    listaRecyclerView.add(nomeArquivo)
                    it.close()
                }
            } catch (e: Exception) {
                showSnackbar("${e.message}")
            }
        }
    }

    private fun getCurrentLocation() {
        val locationManager =
                getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(
                LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER)
        if (!isGPSEnabled && !isNetworkEnabled) {
            Log.d("Permissao", "Ative os serviços necessários")
        } else {
            when {
                isGPSEnabled -> {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                2000L, 0f, locationListener)
                    } catch(ex: SecurityException) {
                        Log.d("Permissao", "Security Exception")
                    }
                }
                isNetworkEnabled -> {
                    try {
                        locationManager.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                2000L, 0f, locationListener)
                    } catch(ex: SecurityException) {
                        Log.d("Permissao", "Security Exception")
                    }
                }
            }
        }
    }

    private val locationListener = object : LocationListener {
        override fun onLocationChanged(location: Location) {
            LATITUDE = location.latitude.toString()
            LONGITUDE = location.longitude.toString()
            tv_latitude.text = LATITUDE
            tv_longitude.text = LONGITUDE
        }

        override fun onProviderDisabled(provider: String) {
            showSnackbar("$provider off")
        }

        override fun onProviderEnabled(provider: String) {
            showSnackbar("$provider on")
        }
    }

    fun actionLocalizacao(view: View){
        val permissionACL = checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        val permissionAFL = checkSelfPermission(FINE_LOCATION)

        if (permissionACL == GRANTED || permissionAFL == GRANTED)
            getCurrentLocation()
        else {
            if (shouldShowRequestPermissionRationale(FINE_LOCATION))
                showDialogPermission(
                    "Para usar este recurso, " +
                            "é necessário conceder permissão para a localização.",
                    arrayOf(FINE_LOCATION)
                )
            else
                requestPermissions(arrayOf(FINE_LOCATION), REQUEST_PERMISSION_CODE)
        }
    }

    fun actionExibirRegistros(view: View) {
        startActivity(Intent(this, RegistrosActivity::class.java))
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            REQUEST_PERMISSION_CODE -> {
                permissions.forEachIndexed { index, permission ->
                    if (grantResults[index] == GRANTED)
                        when (permission) {
                            FINE_LOCATION -> getCurrentLocation()
                            EXTERNAL -> writeInAExternalFile()
                            //READ_EXTERNAL -> readAExternalFile()
                        }
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
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

    private fun obterDataEHora(): String {
        val date = Calendar.getInstance().time

        val dateTimeFormat = SimpleDateFormat("dd-MM-yyyy_HH-mm-ss", Locale.getDefault())
        return dateTimeFormat.format(date)
    }

    private fun showSnackbar(message: String, duration: Int = Snackbar.LENGTH_LONG) {
        Snackbar
            .make(
                root_layout,
                message,
                duration
            ).show()
    }
}