package com.example.gestordetareas.Login

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.generated.model.Usuario


class LoginViewModel:ViewModel() {

    private val _nombre =  MutableLiveData<String>()
    val nombre : LiveData<String> = _nombre

    private val _email =  MutableLiveData<String>()
    val email : LiveData<String> = _email

    private val _pwd =  MutableLiveData<String>()
    val pwd : LiveData<String> = _pwd

    private val _isLoginEnable = MutableLiveData<Boolean>()
    val isLoginEnable : LiveData<Boolean> = _isLoginEnable

    private val _isLogoutEnable = MutableLiveData<Boolean>()
    val isLogoutEnable : LiveData<Boolean> = _isLogoutEnable

    private val _isLogoutOk = MutableLiveData<Boolean>()
    val isLogoutOk : LiveData<Boolean> = _isLogoutOk

    private val _isLoginOk = MutableLiveData<Boolean>()
    val isLoginOk : LiveData<Boolean> = _isLoginOk

    private val _isRegistroCorrecto = MutableLiveData<Int>()
    val isRegistroCorrecto : LiveData<Int> = _isRegistroCorrecto

    fun setIsLoginOk(valor:Boolean){
        _isLoginOk.postValue(valor)
    }

    /**
     * 0 -> registro no realizado
     * 1 -> correcto
     * 2 -> algo ha fallado
     * El uso de postValue es para que se pueda actualizar desde cualqiuer hilo y se pueda llamar dentro de funciones lambda (mirar registro y login).
     */
    fun setRegistroCorrecto(valor:Int){
        _isRegistroCorrecto.postValue(valor)
    }

    fun onRegistroCambiado(email:String, pass:String){
        _email.value = email
        _pwd.value = pass
        _isLoginEnable.value = enableLogin(email,pass)
    }

    fun setNombre(nombre:String){
        _nombre.value = nombre
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

//
//    fun getRolByEmail(email: String): Int ?{
//        var rol:Int = 1
//        Amplify.DataStore.query(
//            Usuario::class.java,
//            Where.matches(Usuario.EMAIL.eq(email)),
//            { usuarios ->
//                if (usuarios.hasNext()) {
//                    val usuario = usuarios.next()
//                    Log.d("Sergio", "Usuario encontrado: $usuario")
//                    Log.d("Sergio", "Rol del usuario: ${usuario.rol}")
//                    rol = usuario.rol
//                } else {
//                    Log.d("Sergio", "Usuario no encontrado para el email: $email")
//                }
//            },
//            { exception ->
//                Log.e("Sergio", "Error al buscar el usuario por email: $email", exception)
//
//            }
//        )
//        return rol
//    }










}
