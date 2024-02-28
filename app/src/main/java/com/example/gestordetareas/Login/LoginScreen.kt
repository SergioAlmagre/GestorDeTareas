package com.example.gestordetareas.Login

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.core.Amplify
import com.example.gestordetareas.ElementosComunes.Rutas
import com.example.gestordetareas.ListadoTareas.ListadoTareasViewModel
import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.gestordetareas.CrearCuenta.CrearCuentaViewModel
import com.example.gestordetareas.ElementosComunes.InterVentana
import com.example.gestordetareas.R
import com.example.gestordetareas.Usuario.UsuarioViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay


import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun Login(
    crearCuentaViewModel: CrearCuentaViewModel,
    usuarioViewModel: UsuarioViewModel,
    navController: NavHostController,
    listadoTareasViewModel: ListadoTareasViewModel,
    loginViewModel: LoginViewModel
) {
    var context = LocalContext.current
    val nombre:String by loginViewModel.nombre.observeAsState("")
    val email:String by loginViewModel.email.observeAsState(initial = "")
    val password:String by loginViewModel.pwd.observeAsState("")
    val isLoginEnable: Boolean by loginViewModel.isLoginEnable.observeAsState(initial = false) //Habilitar el botón de login
    val isLogoutEnable: Boolean by loginViewModel.isLogoutEnable.observeAsState(initial = true) //Para habilitar el botón de logout
    val isLogoutOk: Boolean by loginViewModel.isLogoutOk.observeAsState(initial = false) //Cuando el logout es correcto
    //var isRegistroCorrecto by remember { mutableStateOf(value = 0) }
    val isRegistroCorrecto:Int by loginViewModel.isRegistroCorrecto.observeAsState(initial = 0)
    var isLogoutCorrecto by remember { mutableStateOf(value = 0) }
    val coroutineScope = rememberCoroutineScope()
    val isLoginOk:Boolean by loginViewModel.isLoginOk.observeAsState(initial = false)

    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.bk),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Column {

            Spacer(modifier = Modifier.size(40.dp))
            Text("Bienvenid@", fontSize = 40.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp))
            Spacer(modifier = Modifier.size(40.dp))
            Email(email) {
                loginViewModel.onRegistroCambiado(email = it, pass = password)
            }
            Spacer(modifier = Modifier.size(4.dp))
            Password(password) {
                loginViewModel.onRegistroCambiado(email = email, pass = it)
            }
            Spacer(modifier = Modifier.size(20.dp))
            Login(isLoginEnable){
                //Bloque de Login
                Log.e("Sergio","${nombre}, ${password}, ${email}")
                try {
                    //Callbacks
                    Amplify.Auth.signIn(
                        email,
                        password,
                        { result ->
                            //Las corrutinas son necesarias para que funcione el Toast. En este ejemplo no uso variables como en el registro y llamo directamente al Toast.
                            coroutineScope.launch {
                                if (result.isSignedIn) {


                                        Log.i("Sergio", "Login correcto_LoginScreen")
                                        usuarioViewModel.buildUsuarioActualByEmail(email)
                                        loginViewModel.setRegistroCorrecto(1)
                                        loginViewModel.setIsLoginOk(true)


                                } else {
                                    Log.e("Sergio", "Algo ha fallado en el login")
                                    Toast.makeText(context, "Algo ha fallado en el login", Toast.LENGTH_SHORT).show()
                                }
                            }

                        },
                        { error ->
                            coroutineScope.launch {
                                Log.e("Sergio error", error.toString())
                                Toast.makeText(context, error.message,Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
                catch (e:Exception){
                }
                finally {
                    if (isRegistroCorrecto == 1) {
                        Toast.makeText(context, "Registro correcto Login", Toast.LENGTH_SHORT).show()
                        loginViewModel.disableLogin()
                        loginViewModel.enableLogout()

                        if(isLoginOk){
                            usuarioViewModel.establecerUsuarioActual(InterVentana.usuarioActivo!!)
                            usuarioViewModel.asignarUsuarioActualToAtributosSueltos()

                            if (InterVentana.usuarioActivo!!.rol == Rutas.rolAdministrador) {
                                navController.navigate(Rutas.eleccionAdministrador)
                                Log.i("Sergio", "Usuario activo Login: ${InterVentana.usuarioActivo!!}")
                            }
                            if (InterVentana.usuarioActivo!!.rol == Rutas.rolProgramador) {
                                listadoTareasViewModel.getTareasNoFinalizadas()
                                navController.navigate(Rutas.listadoTareas)
                                Log.i("Sergio", "Usuario activo Login: ${InterVentana.usuarioActivo!!}")
                            }
                        }

                    }
                    if (isRegistroCorrecto == 2) {
                        Toast.makeText(context, "Algo ha fallado en el registro", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
            }



            Spacer(modifier = Modifier.size(200.dp))
//            CerrarSesionYAplicacion(context = context)
            crearCuenta {
                crearCuentaViewModel.limpiarAtributosSueltos()
                navController.navigate(Rutas.crearCuenta)
            }

        }

    }
}

@Composable
fun Logout(isLogoutEnable: Boolean, onClickAction: (Boolean) -> Unit){
    Button(
        onClick = {
            onClickAction(true);
        },
        enabled = isLogoutEnable,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Logout")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Nombre(nombre: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = nombre,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color = Color(0xFFE0E1EB)),
        placeholder = { Text(text = "Nombre y apellidos") },
        label = {Text("Introduce tu nombre y apellidos")},
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        textStyle = LocalTextStyle.current.copy(color = Color(0xFF000000)),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Email(email: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color = Color(0xFFE0E1EB)),
        placeholder = { Text(text = "Email") },
        label = {Text("Introduce tu email")},
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        textStyle = LocalTextStyle.current.copy(color = Color(0xFF000000)),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Transparent,
            unfocusedBorderColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(password: String, onTextChanged: (String) -> Unit) {
    var showPassword by remember {mutableStateOf (value = false)}

    TextField(
        value = password,
        onValueChange = { onTextChanged(it) },
        label = { Text("Introduce una contraseña") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .background(color = Color(0xFFFAFAFA))
            .clip(MaterialTheme.shapes.medium),
        placeholder = { Text("Password") },
        colors = TextFieldDefaults.textFieldColors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            IconButton(
                onClick = { showPassword = !showPassword }
            ) {
                Icon(
                    imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = "toggle_password_visibility"
                )
            }
        },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}

@Composable
fun CerrarAplicacion(){
    val activity = LocalContext.current as Activity
    Button(modifier = Modifier.fillMaxWidth(), onClick = { activity.finish() }) {
        Text(text = "Cerrar aplicación")
    }
}

@Composable
fun CerrarSesionYAplicacion(context: Context){
    val activity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()

    Button(modifier = Modifier.fillMaxWidth(), onClick = {
        val options = AuthSignOutOptions.builder()
            .globalSignOut(true)
            .build()

        Amplify.Auth.signOut(options) { signOutResult ->
            coroutineScope.launch {
                if (signOutResult is AWSCognitoAuthSignOutResult.CompleteSignOut) {
                    Log.i("Fernando", "Logout correcto")
                    Toast.makeText(context,"Logout ok", Toast.LENGTH_SHORT).show()
                } else if (signOutResult is AWSCognitoAuthSignOutResult.PartialSignOut) {
                } else if (signOutResult is AWSCognitoAuthSignOutResult.FailedSignOut) {
                    Log.e("Fernando", "Algo ha fallado en el logout")
                    Toast.makeText(context,"Algo ha fallado en el logout", Toast.LENGTH_SHORT).show()
                }
            }
        }
//        activity.finish()
         }) {
        Text(text = "Cerrar aplicación")
    }
}

@Composable
fun Registrar(isRegistroEnable : Boolean, onClickAction: (Boolean) -> Unit){
    Button(
        onClick = {
            onClickAction(true);
        },
        enabled = isRegistroEnable,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Registrar")
    }
}

@Composable
fun Login(isRegistroEnable : Boolean, onClickAction: (Boolean) -> Unit){
    Button(
        onClick = {
            onClickAction(true);
        },
        enabled = isRegistroEnable,
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
            .width(200.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Login")
    }
}

@Composable
fun crearCuenta(onClickAction: (Boolean) -> Unit){
    Button(
        onClick = {
            onClickAction(true);
        },
        modifier = Modifier
            .padding(60.dp)
            .width(300.dp)
            .height(50.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Crear una cuenta")
    }
}

@Composable
fun IrPrincipalButton(onClickAction: (Boolean) -> Unit) {
    Button(
        onClick = {
            onClickAction(true);
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Ir a la principal")
    }
}