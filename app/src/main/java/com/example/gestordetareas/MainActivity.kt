package com.example.gestordetareas

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.amplifyframework.AmplifyException
import com.amplifyframework.api.aws.AWSApiPlugin
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.AWSDataStorePlugin
import com.example.a2_practicamvvm.Rutas
import com.example.gestordetareas.CrearCuenta.CrearCuenta
import com.example.gestordetareas.CrearCuenta.CrearCuentaViewModel
import com.example.gestordetareas.Listado.Listado
import com.example.gestordetareas.Listado.ListadoViewModel
import com.example.gestordetareas.Login.Login
import com.example.gestordetareas.Principal.Principal
import com.example.gestordetareas.Principal.PrincipalViewModel
import com.example.gestordetareas.Usuario.UsuarioViewModel
import com.example.gestordetareas.ui.theme.GestorDeTareasTheme

import com.example.gestordetareas.Login.LoginViewModel

class MainActivity : ComponentActivity() {
    val princiaplVM = PrincipalViewModel()
    val listadoVM = ListadoViewModel()
    val usuarioVM = UsuarioViewModel()
    val loginVM = LoginViewModel()
    val crearCuentaVM = CrearCuentaViewModel()

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
                    NavHost(navController = navController, startDestination = Rutas.CrearCuenta){

                        composable(Rutas.CrearCuenta){
                            CrearCuenta(navController, princiaplVM, listadoVM, loginVM, crearCuentaVM)
                        }

                        composable(Rutas.Login){
                            Login(navController, princiaplVM, listadoVM, loginVM)
                        }
                        composable(Rutas.Principal){
                            Principal(navController, princiaplVM, listadoVM)
                        }
                        composable(Rutas.Listado){
                            Listado(navController, princiaplVM, listadoVM)
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

                    //Cognito
//                    val email = "sergioalmagre@gmail.com"
//                    val pass = "Chubac@2024"
//                        Conexion.registrarUsuario(email, pass)
//                        Conexion.confirmar(email, pass,201819)
//                        Conexion.loginUsuario(email, pass)

                }
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