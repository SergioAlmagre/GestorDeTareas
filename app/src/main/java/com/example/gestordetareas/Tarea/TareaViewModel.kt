package com.example.gestordetareas.Tarea

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class TareaViewModel {

    private val _descripcion =  MutableLiveData<String>()
    val descripcion : LiveData<String> = _descripcion

    private val _estimacionHoras =  MutableLiveData<Int>()
    val horas : LiveData<Int> = _estimacionHoras

    private val _horasInvertidas = MutableLiveData<Int>()
    val horasInvertidas : LiveData<Int> = _horasInvertidas

    private val _dificultad = MutableLiveData<String>()
    val dificultad: LiveData<String> = _dificultad

    private val _estaAsignada = MutableLiveData<Boolean>()
    val estaAsignada : LiveData<Boolean> = _estaAsignada

    private val _estaFinalizada =  MutableLiveData<Boolean>()
    val estaFinalizada : LiveData<Boolean> = _estaFinalizada



}