package com.example.gestordetareas.Usuario

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class ModUsuarioViewModel {

    private val _nombre =  MutableLiveData<String>()
    val nombre : LiveData<String> = _nombre

    private val _rol =  MutableLiveData<Int>()
    val rol : LiveData<Int> = _rol

    private val _codigoVerificacion = MutableLiveData<String>()
    val codigoVerificacion : LiveData<String> = _codigoVerificacion

    private val _isRegistrarEnable = MutableLiveData<Boolean>()
    val isLoginEnable : LiveData<Boolean> = _isRegistrarEnable

    private val _isRegistroCorrecto = MutableLiveData<Int>()
    val isRegistroCorrecto : LiveData<Int> = _isRegistroCorrecto


    fun onCodigoVerificacionCambiado(codigo:String){
        _codigoVerificacion.value = codigo
    }

    fun setRegistroCorrecto(valor:Int){
        _isRegistroCorrecto.postValue(valor)
    }

    fun setNombre(nombre:String){
        _nombre.value = nombre
    }

    fun disableLogin(){
        _isRegistrarEnable.value = false
    }







}