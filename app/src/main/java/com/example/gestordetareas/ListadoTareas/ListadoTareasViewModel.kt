package com.example.gestordetareas.ListadoTareas

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Tarea


class ListadoTareasViewModel : ViewModel() {
    private val _tareas : ArrayList<Tarea> by mutableStateOf(arrayListOf())
    val tareas : ArrayList<Tarea> = _tareas

    private val _showDialogBorrar = MutableLiveData<Boolean>()
    val showDialogBorrar: LiveData<Boolean> = _showDialogBorrar

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



    fun getTareas() {
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



}