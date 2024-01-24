package com.example.gestordetareas.ListaUsuarios

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Usuario


class ListadoUsuariosViewModel {

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog


    private val _usuarios = mutableStateListOf<Usuario>()
    val usuarios: List<Usuario> = _usuarios

    fun dialogClose() {
        _showDialog.value = false
    }

    fun dialogOpen() {
        _showDialog.value = true
    }

    fun borrarUsuario(u: Usuario) {
        _usuarios.remove(u)
        Amplify.DataStore.delete(u,
            { success -> Log.i("Sergio", "Deleted item: ${success.toString()}") },
            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
        )
    }

    fun getUsers() {
        _usuarios.clear()  // Utiliza la variable de instancia, no una nueva variable local

        Amplify.DataStore.query(
            com.amplifyframework.datastore.generated.model.Usuario::class.java,
            { items ->
                while (items.hasNext()) {
                    val item = items.next()
                    Log.i("Sergio", "Queried item: ${item.toString()}")
                    _usuarios.add(item)
                }
            },
            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
        )
    }



}