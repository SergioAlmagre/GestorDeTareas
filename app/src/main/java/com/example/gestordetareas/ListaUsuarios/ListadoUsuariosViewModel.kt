package com.example.gestordetareas.ListaUsuarios

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignOutOptions
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

    fun geTop3MasTareas() {
        _usuarios.clear()
        Amplify.DataStore.query(
            Usuario::class.java,
            { usuarios ->

                _usuarios.sortedByDescending { it.tareasFinalizadas }

                // Obtener los primeros 5 usuarios (o menos si hay menos de 5 usuarios)
                val primerosCincoUsuarios = _usuarios.take(3)

                // Hacer algo con los usuarios obtenidos
                for (usuario in primerosCincoUsuarios) {
                    Log.i("Sergio", "Usuario: ${usuario.nombreCompleto}, Tareas Realizadas: ${usuario.tareasFinalizadas}")
                }
            },
            { exception ->
                // Manejar excepciones si es necesario
                Log.e("Sergio", "Error al consultar usuarios", exception)
            }
        )
    }

    fun geTop3MenosTareas() {
        _usuarios.clear()
        Amplify.DataStore.query(
            Usuario::class.java,
            { usuarios ->

                _usuarios.sortedBy{ it.tareasFinalizadas }

                // Obtener los primeros 5 usuarios (o menos si hay menos de 5 usuarios)
                val primerosCincoUsuarios = _usuarios.take(3)

                // Hacer algo con los usuarios obtenidos
                for (usuario in primerosCincoUsuarios) {
                    Log.i("Sergio", "Usuario: ${usuario.nombreCompleto}, Tareas Realizadas: ${usuario.tareasFinalizadas}")
                }
            },
            { exception ->
                // Manejar excepciones si es necesario
                Log.e("Sergio", "Error al consultar usuarios", exception)
            }
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

    fun cerrarSesión(){
        val options = AuthSignOutOptions.builder()
            .globalSignOut(true)
            .build()

        Amplify.Auth.signOut(options) { signOutResult ->

            if (signOutResult is AWSCognitoAuthSignOutResult.CompleteSignOut) {
                Log.i("Fernando", "Logout correcto")

            } else if (signOutResult is AWSCognitoAuthSignOutResult.PartialSignOut) {
            } else if (signOutResult is AWSCognitoAuthSignOutResult.FailedSignOut) {
                Log.e("Fernando", "Algo ha fallado en el logout")

            } else {

            }
        }
    }



}