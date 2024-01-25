package com.example.gestordetareas.ListadoTareas

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Tarea
import com.amplifyframework.datastore.generated.model.Usuario
import kotlinx.coroutines.launch


class ListadoTareasViewModel : ViewModel() {
    private val _tareas = mutableStateListOf<Tarea>()
    val tareas : List<Tarea> = _tareas

    private val _showDialogBorrar = MutableLiveData<Boolean>()
    val showDialogBorrar: LiveData<Boolean> = _showDialogBorrar

    private val _tareaActual = MutableLiveData<Tarea>()
    val tareaActual: LiveData<Tarea> = _tareaActual

    lateinit var tarBorrar: Tarea

    fun dialogClose() {
        _showDialogBorrar.value = false
    }

    fun dialogOpen() {
        _showDialogBorrar.value = true
    }

    fun tareaBorrar(t: Tarea){
        //_usuBorrar.value = u
        tarBorrar = t
    }

    fun onTareaCreated(ta : Tarea) {
        _tareas.add(ta)
    }

    fun onItemRemove(t: Tarea) {
        _tareas.apply {
            remove(t)
        }
    }

    fun establecerTareaActual(tarea: Tarea) {
        _tareaActual.value = tarea
    }

    fun borrarTarea(tarea: Tarea) {
        _tareas.remove(tarea)
        Amplify.DataStore.delete(tarea,
            { success -> Log.i("Sergio", "Deleted item: ${success.toString()}") },
            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
        )
    }



    fun getTodasLasTareas() {
        _tareas.clear()
        Amplify.DataStore.query(
            com.amplifyframework.datastore.generated.model.Tarea::class.java,
            { items ->
                while (items.hasNext()) {
                    val item = items.next()
                    Log.i("Sergio", "Queried item: ${item.toString()}")
                    _tareas.add(item)
                }
            },
            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
        )
    }

    fun getTareasRealizadas() {
        _tareas.clear()
        Amplify.DataStore.query(
            com.amplifyframework.datastore.generated.model.Tarea::class.java,
            { items ->
                while (items.hasNext()) {
                    val item = items.next()
                    if(item.estaFinalizada){
                        Log.i("Sergio", "Queried item: ${item.toString()}")
                        _tareas.add(item)
                    }
                }
            },
            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
        )
    }

    fun getTareasNoFinalizadas() {
        _tareas.clear()
        Amplify.DataStore.query(
            com.amplifyframework.datastore.generated.model.Tarea::class.java,
            { items ->
                while (items.hasNext()) {
                    val item = items.next()
                    if(!item.estaFinalizada){
                        Log.i("Sergio", "Queried item: ${item.toString()}")
                        _tareas.add(item)
                    }
                }
            },
            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
        )
    }

    fun getTareasSinAsignar() {
        _tareas.clear()
        Amplify.DataStore.query(
            com.amplifyframework.datastore.generated.model.Tarea::class.java,
            { items ->
                while (items.hasNext()) {
                    val item = items.next()
                    if(!item.estaAsignada){
                        Log.i("Sergio", "Queried item: ${item.toString()}")
                        _tareas.add(item)
                    }
                }
            },
            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
        )
    }

    fun getTareasDeUsuarioPorId(id: String) {
        _tareas.clear()
        Amplify.DataStore.query(
            com.amplifyframework.datastore.generated.model.Tarea::class.java,
            { items ->
                while (items.hasNext()) {
                    val item = items.next()
                    if(item.usuarioTarea.id == id){
                        Log.i("Sergio", "Queried item: ${item.toString()}")
                        _tareas.add(item)
                    }
                }
            },
            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
        )
    }

    fun getMisTareas(id: String) {
        _tareas.clear()
        Amplify.DataStore.query(
            com.amplifyframework.datastore.generated.model.Tarea::class.java,
            { items ->
                while (items.hasNext()) {
                    val item = items.next()
                    if(item.usuarioTarea.id == id){
                        Log.i("Sergio", "Queried item: ${item.toString()}")
                        _tareas.add(item)
                    }
                }
            },
            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
        )
    }




    fun cerrarSesiónList(){
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

    fun obatenerTareaVacia(): Tarea {
        return Tarea.builder()
            .descripcion("")
            .dificultad("")
            .estimacionHoras(0.0)
            .horasInvertidas(0.0)
            .estaAsignada(false)
            .estaFinalizada(false)
            .build()
    }



}