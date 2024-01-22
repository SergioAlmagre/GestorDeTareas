package com.example.gestordetareas.ListaUsuarios

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Usuario


class ListadoUsuariosViewModel {


    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _usuBorrar = MutableLiveData<Usuario?>()
    val usuBorrar: LiveData<Usuario?> = _usuBorrar
//    lateinit var usuBorrar:Usuario

    private val _usuarios: ArrayList<Usuario> by mutableStateOf(arrayListOf())
    val usuarios: ArrayList<Usuario> = _usuarios

    fun dialogClose() {
        _showDialog.value = false
    }

    fun dialogOpen() {
        _showDialog.value = true
    }

    fun usuarioBorrar(u: Usuario) {
        _usuBorrar.value = u
//        usuBorrar = u
    }


    fun getUsers() {
        usuarios.clear()  // Utiliza la variable de instancia, no una nueva variable local

        Amplify.DataStore.query(
            com.amplifyframework.datastore.generated.model.Usuario::class.java,
            { items ->
                while (items.hasNext()) {
                    val item = items.next()
                    Log.i("Sergio", "Queried item: ${item.toString()}")
                    usuarios.add(item)
                }
            },
            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
        )
    }



}