package com.example.gestordetareas.ListaUsuarios

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.generated.model.Tarea
import com.amplifyframework.datastore.generated.model.Usuario
import com.example.gestordetareas.ElementosComunes.InterVentana
import com.example.gestordetareas.ListadoTareas.ListadoTareasViewModel
import com.example.gestordetareas.Usuario.UsuarioViewModel
import kotlin.collections.filter
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ListadoUsuariosViewModel {

    private val myScope = CoroutineScope(Dispatchers.Main)

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    private val _usuarios = mutableStateListOf<Usuario>()
    val usuarios: List<Usuario> = _usuarios

    private val _esOrdenado = MutableLiveData<Boolean>()
    val esOrdenado: LiveData<Boolean> = _esOrdenado

    fun dialogClose() {
        _showDialog.value = false
    }

    fun dialogOpen() {
        _showDialog.value = true
    }

    fun cambiarEsOrdenado(boolean: Boolean){
        _esOrdenado.value = boolean
    }


    fun borrarUsuario(u: Usuario) {
        _usuarios.remove(u)
        Amplify.DataStore.delete(u,
            { success -> Log.i("Sergio", "Deleted item: ${success.toString()}") },
            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
        )
    }

    fun getTop3MasTareas() {
//        myScope.launch { // Problemas con la visualización de los usuarios
//            getUsers() // La idea es obtenerlos todos primero porque si pasas de los mas a los menos la fuente _usuarios solo tiene 3 usuarios
            _esOrdenado.value = false
            _usuarios.sortBy { it.tareasFinalizadas }

            // Orden para obtener los usuarios con menos tareas completadas primero
            _usuarios.reverse()

            // Tomar los primeros 3 usuarios (los que tienen menos tareas completadas)
            val topUsuarios = _usuarios.take(3)

            // Limpiar la lista de usuarios antes de agregar los nuevos usuarios
            _usuarios.clear()

            // Agregar los usuarios con menos tareas completadas al ranking
            _usuarios.addAll(topUsuarios)
//        }
    }


    fun getTop3MenosTareas() {
//        getUsers()
        _esOrdenado.value = false
        _usuarios.sortBy { it.tareasFinalizadas }

        _usuarios

        val topUsuarios = _usuarios.take(3)

        _usuarios.clear()

        _usuarios.addAll(topUsuarios)
    }









    fun obtenerUsuarioById(id: String): Usuario {
        var usuario = Usuario.builder().build()
        Amplify.DataStore.query(
            Usuario::class.java,
            Where.matches(Usuario.ID.eq(id)),
            { usuarios ->
                if (usuarios.hasNext()) {
                    val usuario = usuarios.next()
                    Log.i("SergioTopBusquedaPorId", "Usuario encontrado: $usuario")
                } else {
                    Log.i("Sergio", "Usuario no encontrado para el email: $id")
                }
            },
            { exception ->
                Log.e("Sergio", "Error al buscar el usuario por email: $id", exception)
            }
        )
        return usuario
    }









    fun getUsers() {
        _esOrdenado.value = true
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

    fun cerrarSesión(){
        val options = AuthSignOutOptions.builder()
            .globalSignOut(true)
            .build()

        Amplify.Auth.signOut(options) { signOutResult ->

            if (signOutResult is AWSCognitoAuthSignOutResult.CompleteSignOut) {
                Log.i("Sergio", "Logout correcto")

            } else if (signOutResult is AWSCognitoAuthSignOutResult.PartialSignOut) {
            } else if (signOutResult is AWSCognitoAuthSignOutResult.FailedSignOut) {
                Log.e("Sergio", "Algo ha fallado en el logout")

            } else {

            }
        }
    }



}