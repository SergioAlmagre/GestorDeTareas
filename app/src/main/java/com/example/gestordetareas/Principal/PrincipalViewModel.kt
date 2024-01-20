package com.example.gestordetareas.Principal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where

class PrincipalViewModel : ViewModel(){

    private val _descripcion =  MutableLiveData<String>()
    val descripcion : LiveData<String> = _descripcion

    private val _estimacionHoras =  MutableLiveData<Double>()
    val estimacionHoras : LiveData<Double> = _estimacionHoras

    private val _horasInvertidas = MutableLiveData<Double>()
    val horasInvertidas : LiveData<Double> = _horasInvertidas

    private val _dificultad = MutableLiveData<String>()
    val dificultad: LiveData<String> = _dificultad

    private val _estaAsignada = MutableLiveData<Boolean>()
    val estaAsignada : LiveData<Boolean> = _estaAsignada

    private val _estaFinalizada =  MutableLiveData<Boolean>()
    val estaFinalizada : LiveData<Boolean> = _estaFinalizada

    private val _showDialog = MutableLiveData<Boolean>()
    val showDialog: LiveData<Boolean> = _showDialog

    fun onDialogClose() {
        this._descripcion.value = ""
        this._estimacionHoras.value = 0.0
        this._dificultad.value = ""
        this._estaFinalizada.value = false
        _showDialog.value = false
    }

    fun onShowDialogClick() {
        _showDialog.value = true
    }

    fun cambiarDescripcion(it: String) {
        this._descripcion.value = it
    }


    fun cambiarEstimacionHoras(e: String) {
        val doubleValue = e.toDoubleOrNull() ?: 0.0
        this._estimacionHoras.value = doubleValue
    }

    fun cambiarDificultad(it: String) {
        this._dificultad.value = it
    }

    fun cambiarEsFinalizada(it: Boolean) {
        this._estaFinalizada.value = it
    }

    fun listarTareas() {
        var tareas = ArrayList<com.amplifyframework.datastore.generated.model.Tarea>()
        tareas.clear()
        Amplify.DataStore.query(
            com.amplifyframework.datastore.generated.model.Tarea::class.java,
            { items ->
                while (items.hasNext()) {
                    val item = items.next()
                    Log.i("Sergio", "Queried item: ${item.toString()}")
                    tareas.add(item)
                }
            },
            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
        )
    }

    fun listarTareas2(){
        Amplify.DataStore.query(
            com.amplifyframework.datastore.generated.model.Tarea::class.java, Where.matches(com.amplifyframework.datastore.generated.model.Tarea.ESTIMACION_HORAS.gt(4)),
            { goodTasks ->
                while (goodTasks.hasNext()) {
                    val tarea = goodTasks.next()
                    Log.i("Sergio", "Tareas: $tarea")
                }
            },
            { Log.e("Sergio", "Query failed", it) }
        )
    }

    fun crearTarea(descripcion: String, dificultad: String, estimacionHoras: Double, horasInvertidas:Double, estaAsignada:Boolean, estaFinalizada: Boolean) {
        val t = com.amplifyframework.datastore.generated.model.Tarea.builder().descripcion(descripcion).estimacionHoras(estimacionHoras).horasInvertidas(horasInvertidas).dificultad(dificultad).estaFinalizada(estaFinalizada).estaAsignada(estaAsignada).build()
        Amplify.DataStore.save(
            t,
            { success -> Log.i("Sergio", "Saved task: " + success.item().toString()) },
            { error -> Log.e("Sergio", "Could not save item to DataStore", error) }
        )
    }
}