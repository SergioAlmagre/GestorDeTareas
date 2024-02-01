package com.example.app_mvvm_aws.Imagenes

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplifyframework.core.Amplify
import com.amplifyframework.storage.options.StoragePagedListOptions

class ImagenViewModel : ViewModel() {
    private val _imagenesS3 : ArrayList<String> by mutableStateOf(arrayListOf())
    val imagenesS3 : ArrayList<String> = _imagenesS3

//    private val _imagenesS3 : MutableLiveData<ArrayList<String>> = MutableLiveData(arrayListOf())
//    val imagenesS3: LiveData<ArrayList<String>> get() = _imagenesS3

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _imagenSeleccionada = MutableLiveData<Boolean>()
    val imagenSeleccionada: LiveData<Boolean> = _imagenSeleccionada

    private val _ficheroImagenSeleccionado = MutableLiveData<String>()
    val ficheroImagenSeleccionado: LiveData<String> = _ficheroImagenSeleccionado

    fun cargaFicherosdeS3() {
        val options = StoragePagedListOptions.builder()
            .setPageSize(1000)
            .build()

        _imagenesS3.clear()

        Amplify.Storage.list("", options,
            { result ->
                result.items.forEach { item ->
                    Log.i("MyAmplifyApp", "Item: ${item.key}")
                    if (item!=null && item.key.isNotEmpty()) {
                        _imagenesS3.add(item.key)
                    }
                }
                Log.i("MyAmplifyApp", "Next Token: ${result.nextToken}")
            },
            { Log.e("MyAmplifyApp", "List failure", it) }
        )
    }

    fun addNombreRV(nombreFichero: String) {
        _imagenesS3.add(nombreFichero)
    }

    fun setImagenSeleccionada(valor:Boolean){
        _imagenSeleccionada.value = valor
    }

    fun setFicheroImagenSeleccionado(valor : String){
        _ficheroImagenSeleccionado.value = valor
    }
}
