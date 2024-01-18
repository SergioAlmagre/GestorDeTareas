package com.example.gestordetareas.Conexion

import android.util.Log
import com.amplifyframework.auth.AuthException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.auth.result.AuthSignOutResult
import com.amplifyframework.core.Amplify
import com.amplifyframework.core.model.query.Where
import com.amplifyframework.datastore.generated.model.Tarea
import com.amplifyframework.datastore.generated.model.Usuario
//import com.example.gestordetareas.Usuario.Usuario

object Conexion {

     fun loginUsuario(email: String, pass: String):Int {
        var codConf = 0
        Amplify.Auth.signIn(
            email,
            pass,
            {result ->
                if (result.isSignedIn) {
                    codConf = 1
                    Log.i("Sergio", "Login correcto ${result}")
                } else {
                    Log.e("Sergio", "Algo ha fallado en el login")
                }
            },
            {error -> Log.e("Sergio", error.toString())}
        );
         return codConf
    }




     fun registrarUsuario(email: String, pass: String) {
        Amplify.Auth.signUp(
            email,
            pass,
            AuthSignUpOptions.builder().userAttribute(AuthUserAttributeKey.email(), email).build(),
            { result -> Log.i("Sergio", "Result: $result") },
            { error -> Log.e("Sergio", "Sign up failed", error) }
        );
    }

     fun confirmar(email: String, pass: String, codConf: Int) {
        Amplify.Auth.confirmSignUp(
            email, codConf.toString(),
            { result ->
                if (result.isSignUpComplete) {
                    Log.i("Sergio", "Confirm signUp succeeded")
                } else {
                    Log.i("Sergio","Confirm sign up not complete")
                }
            },
            { Log.e("Sergio", "Failed to confirm sign up", it) }
        )
    }

     fun listarUsuarios() {
        var pers = ArrayList<Usuario>()
        pers.clear()
        Amplify.DataStore.query(
            Usuario::class.java,
            { items ->
                while (items.hasNext()) {
                    val item = items.next()
                    Log.i("Sergio", "Queried item: ${item.toString()}")
                    pers.add(item)
                }
            },
            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
        )
    }


//     fun listarTareas() {
//        var tareas = ArrayList<Tarea>()
//        tareas.clear()
//        Amplify.DataStore.query(
//            Tarea::class.java,
//            { items ->
//                while (items.hasNext()) {
//                    val item = items.next()
//                    Log.i("Sergio", "Queried item: ${item.toString()}")
//                    tareas.add(item)
//                }
//            },
//            { failure -> Log.e("Sergio", "Could not query DataStore", failure) }
//        )
//    }
//
//     fun listarTareas2(){
//        Amplify.DataStore.query(
//            Tarea::class.java, Where.matches(Tarea.ESTIMACION_HORAS.gt(4)),
//            { goodTasks ->
//                while (goodTasks.hasNext()) {
//                    val tarea = goodTasks.next()
//                    Log.i("Sergio", "Tareas: $tarea")
//                }
//            },
//            { Log.e("Sergio", "Query failed", it) }
//        )
//    }
//
//     fun crearTarea() {
//        val t = Tarea.builder().id("1").esFinalizada(true).decripcion("Tarea 2").estimacionHoras(2).dificultad("XS").build()
//        Amplify.DataStore.save(
//            t,
//            { success -> Log.i("Sergio", "Saved task: " + success.item().toString()) },
//            { error -> Log.e("Sergio", "Could not save item to DataStore", error) }
//        )
//    }


}