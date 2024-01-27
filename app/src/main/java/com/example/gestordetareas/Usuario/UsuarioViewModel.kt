package com.example.gestordetareas.Usuario

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.generated.model.Usuario
import com.example.gestordetareas.ElementosComunes.InterVentana
import java.util.concurrent.CompletableFuture

class UsuarioViewModel {

    private var _usuarioActual = MutableLiveData<Usuario?>()
    var usuarioActual: LiveData<Usuario?> = _usuarioActual

    private val _rol = MutableLiveData<Int>()
    val rol: LiveData<Int> = _rol

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

    private val _fotoPerfil = MutableLiveData<String>()
    val fotoPerfil : LiveData<String> = _fotoPerfil

    private val _tareasFinalizadas = MutableLiveData<Int>()
    val tareasFinalizadas : LiveData<Int> = _tareasFinalizadas


    fun cambiarNombre(nombre:String){
        _nombreCompleto.value = nombre
    }

    fun cambiarId(it: String) {
        this._id.value = it
    }

    fun establecerUsuarioActual(usuario: Usuario) {
        _usuarioActual.value = usuario
        Log.i("Sergio", "Usuario actualizado: ${_usuarioActual.value.toString()}")
    }

    fun obtenerUsuarioActual(): Usuario? {
        return _usuarioActual.value
    }

    fun cambiarRol(it: Int) {
        this._rol.value = it
    }

    fun cambiarNombreUsuarioActual(nombre: String) {
        _usuarioActual.value?.nombreCompleto ?: nombre
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

    fun cambiarTareasFinalizadas(it: Int) {
        this._tareasFinalizadas.value = it
    }

    fun cambiarFotoPerfil(it: String) {
        this._fotoPerfil.value = it
    }

    fun getTareasFinalizadas(): Int {
        return _tareasFinalizadas.value!!
    }

    fun guardarModificarUsuario(usuario: Usuario): CompletableFuture<Boolean> {
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

    fun buildUsuarioActualByEmail(email: String){
        Amplify.DataStore.query(
            Usuario::class.java,
            Where.matches(Usuario.EMAIL.eq(email)),
            { usuarios ->
                if (usuarios.hasNext()) {
                    val usuario = usuarios.next()
                    Log.i("Sergio", "Usuario encontrado: $usuario")
                    // Log.i("Sergio", "Rol del usuario: ${usuario.rol}")
                    InterVentana.usuarioActivo = usuario
                    Log.i("Sergio", "Usuario activo: ${InterVentana.usuarioActivo.toString()}")
                } else {
                    Log.i("Sergio", "Usuario no encontrado para el email: $email")
                }
            },
            { exception ->
                Log.e("Sergio", "Error al buscar el usuario por email: $email", exception)
            }
        )
    }


    fun gemelearUsuarioActualVM(){
        InterVentana.usuario!!.id = _usuarioActual.value!!.id!!
        InterVentana.usuario!!.rol = _usuarioActual.value!!.rol!!
        InterVentana.usuario!!.nombreCompleto = _usuarioActual.value!!.nombreCompleto!!
        InterVentana.usuario!!.email = _usuarioActual.value!!.email!!
        InterVentana.usuario!!.fotoPerfil = _usuarioActual.value!!.fotoPerfil!!
        InterVentana.usuario!!.tareasFinalizadas = _usuarioActual.value!!.tareasFinalizadas!!
    }

    fun asignarUsuarioActualToAtributosSueltos(){
        cambiarRol(usuarioActual.value!!.rol)
        cambiarNombre(usuarioActual.value!!.nombreCompleto)
        cambiarEmail(usuarioActual.value!!.email)
        cambiarId(usuarioActual.value!!.id)
        cambiarTareasFinalizadas(usuarioActual.value!!.tareasFinalizadas)
        cambiarFotoPerfil("usuarioActual.value!!.fotoPerfil") // REVISAR ORIGEN DE DATOS PORQUE PILLA UN NULL SI USO USUARIOACTUAL.VALUE.....
    }

    fun obtenerUsuarioVacio(): Usuario {
        return Usuario.builder()
            .id("")
            .rol(1)
            .nombreCompleto("")
            .email("")
            .fotoPerfil("")
            .tareasFinalizadas(0)
            .build()
    }

    fun getUsuarioByAtributosSueltos(): Usuario {
        return Usuario.builder()
            .id(id.value!!)
            .rol(rol.value!!)
            .nombreCompleto(nombreCompleto.value!!)
            .email(email.value!!)
            .fotoPerfil(fotoPerfil.value!!)
            .tareasFinalizadas(tareasFinalizadas.value!!)
            .build()
    }

    fun limpiarAtributosSueltos(){
        this._rol.value = 1
        this._nombreCompleto.value = ""
        this._email.value = ""
        this._password.value = ""
        this._pwd.value = ""
        this._id.value = ""
        this._fotoPerfil.value = ""
        this._tareasFinalizadas.value = 0
    }







}