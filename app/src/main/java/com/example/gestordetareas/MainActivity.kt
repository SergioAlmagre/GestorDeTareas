package com.example.gestordetareas

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.example.gestordetareas.CrearCuenta.CrearUsuario
import com.example.gestordetareas.CrearCuenta.CrearCuentaViewModel
import com.example.gestordetareas.EleccionAdministrador.botonesSeleccion
import com.example.gestordetareas.ElementosComunes.Rutas
import com.example.gestordetareas.ListaUsuarios.ListadoUsuarios
import com.example.gestordetareas.ListaUsuarios.ListadoUsuariosViewModel

import com.example.gestordetareas.ListadoTareas.ListadoTareas
import com.example.gestordetareas.ListadoTareas.ListadoTareasViewModel
import com.example.gestordetareas.Login.Login

import com.example.gestordetareas.Usuario.UsuarioViewModel
import com.example.gestordetareas.ui.theme.GestorDeTareasTheme

import com.example.gestordetareas.Login.LoginViewModel
import com.example.gestordetareas.Usuario.ModUsuarioViewModel
import com.example.gestordetareas.Usuario.ModificarUsuario
import com.example.gestordetareas.Tarea.TareaViewModel
import com.example.gestordetareas.Tarea.VerTarea

class MainActivity : ComponentActivity() {
    val listadoTareasVM = ListadoTareasViewModel()
    val usuarioVM = UsuarioViewModel()
    val tareaVM = TareaViewModel()
    val loginVM = LoginViewModel()
    val crearCuentaVM = CrearCuentaViewModel()
    val listadoUsuariosVM = ListadoUsuariosViewModel()
    val modificarUsuarioVM = ModUsuarioViewModel()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GestorDeTareasTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val navController = rememberNavController()
                    LaunchedEffect(true) {
                        listadoUsuariosVM.getUsers()
                    }

                    NavHost(navController = navController, startDestination = Rutas.login){

                        composable(Rutas.verTarea){
                            VerTarea(listadoTareasVM ,listadoUsuariosVM ,usuarioVM, navController, tareaVM)
                        }

                        composable(Rutas.listadoTareas){
                            ListadoTareas(tareaVM ,modificarUsuarioVM ,usuarioVM,navController, listadoTareasVM)
                        }

                        composable(Rutas.listadoUsuarios){
                            ListadoUsuarios(navController, listadoUsuariosVM, usuarioVM)
                        }

                        composable(Rutas.eleccionAdministrador){
                            botonesSeleccion(listadoUsuariosVM,listadoTareasVM,navController)
                        }

                        composable(Rutas.crearCuenta){
                            CrearUsuario(navController,usuarioVM, crearCuentaVM )
                        }

                        composable(Rutas.login){
                            Login(crearCuentaVM, usuarioVM ,navController, listadoTareasVM, loginVM)
                        }

                        composable(Rutas.modUsuario){
                            ModificarUsuario(navController, usuarioVM, modificarUsuarioVM,listadoUsuariosVM)
                        }

                    }
                    try{
                        Amplify.addPlugin(AWSApiPlugin()) // UNCOMMENT this line once backend is deployed
                        Amplify.addPlugin(AWSCognitoAuthPlugin())
                        Amplify.addPlugin(AWSDataStorePlugin())
//                        Amplify.addPlugin(AWSS3StoragePlugin())
                        Amplify.configure(applicationContext)
                        Log.i("Sergio", "Initialized Amplify")
                    } catch (e: AmplifyException) {
                        Log.e("Sergio", "Could not initialize Amplify", e)
                    }

                    // Cerrar sesión al iniciar la aplicación
                    LaunchedEffect(true) {
                        signOut()
                    }
                }
            }
        }
    }
    private fun signOut() {
        val options = AuthSignOutOptions.builder()
            .globalSignOut(true)
            .build()

        Amplify.Auth.signOut(options) { signOutResult ->
            if (signOutResult is AWSCognitoAuthSignOutResult.CompleteSignOut) {
                Log.i("Sergio", "Logout correcto")
            } else if (signOutResult is AWSCognitoAuthSignOutResult.FailedSignOut) {
                Log.e("Sergio", "Algo ha fallado en el logout")
            }
        }
    }

}



@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GestorDeTareasTheme {
        Greeting("Android")
    }
}