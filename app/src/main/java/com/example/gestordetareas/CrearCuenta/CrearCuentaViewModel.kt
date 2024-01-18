package com.example.gestordetareas.CrearCuenta

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class CrearCuentaViewModel {

    private val _nombre =  MutableLiveData<String>()
    val nombre : LiveData<String> = _nombre

    private val _email =  MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _pwd =  MutableLiveData<String>()
    val pwd : LiveData<String> = _pwd

    private val _pwd2 = MutableLiveData<String>()
    val pwd2 : LiveData<String> = _pwd2

    private val _isRegistrarEnable = MutableLiveData<Boolean>()
    val isLoginEnable : LiveData<Boolean> = _isRegistrarEnable



    fun onRegistroCambiado(email:String, pwd1:String, pwd2:String){
        _email.value = email
        _pwd.value = pwd1
        _pwd2.value = pwd2
        _isRegistrarEnable.value = enableRegistrar(email,pwd1,pwd2)
    }

    fun setNombre(nombre:String){
        _nombre.value = nombre
    }

    fun setEmail(email:String){
        _email.value = email
    }

    fun setPwd(pwd:String){
        _pwd.value = pwd
    }

    fun setPwd2(pwd2:String){
        _pwd2.value = pwd2
    }

    fun enableRegistrar(email:String, pwd1:String, pwd2:String):Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && pwd1.length > 6 && pwd1 == pwd2
    }

    fun disableLogin(){
        _isRegistrarEnable.value = false
    }
}