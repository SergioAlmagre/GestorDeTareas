package com.example.gestordetareas.Usuario

import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Usuario
import java.util.concurrent.CompletableFuture

class UsuarioViewModel {
    private val _nombreCompleto = MutableLiveData<String>()
    val nombreCompleto: LiveData<String> = _nombreCompleto

    private val _email = MutableLiveData<String>()
    val email: LiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _pwd =  MutableLiveData<String>()
    val pwd : LiveData<String> = _pwd

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable : LiveData<Boolean> = _isLoginEnable

    private val _isLogoutEnable = MutableLiveData<Boolean>()
    val isLogoutEnable : LiveData<Boolean> = _isLogoutEnable

    private val _isLogoutOk = MutableLiveData<Boolean>()
    val isLogoutOk : LiveData<Boolean> = _isLogoutOk

    private val _isRegistroCorrecto = MutableLiveData<Int>()
    val isRegistroCorrecto : LiveData<Int> = _isRegistroCorrecto

    private val _id = MutableLiveData<String>()
    val id : LiveData<String> = _id


    fun cambiarNombre(it: String) {
        this._nombreCompleto.value = it
    }


    fun cambiarEmail(it: String) {
        this._email.value = it
    }

    fun cambiarPassword(it: String) {
        this._password.value = it
    }

    fun enableLogin(email:String, pass:String):Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(email).matches() && pass.length > 6
    }

    fun disableLogin(){
        _isLoginEnable.value = false
    }

    fun enableLogout(){
        _isLogoutEnable.value = true
    }

    fun disableLogout(){
        _isLogoutEnable.value = false
    }

    fun enableLogoutOk(){
        _isLogoutOk.value = true
    }

    fun disableLogoutOk(){
        _isLogoutOk.value = false
    }


    fun insertarUsuario(usuario: Usuario): CompletableFuture<Boolean> {
        val completableFuture = CompletableFuture<Boolean>()

        Amplify.DataStore.save(
            usuario,
            { success ->
                println("Usuario guardado correctamente")
                completableFuture.complete(true)
            },
            { error ->
                println("Error al guardar el usuario: $error")
                completableFuture.complete(false)
            }
        )

        return completableFuture
    }




}