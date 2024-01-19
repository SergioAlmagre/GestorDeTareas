package com.example.gestordetareas.Login

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.example.a2_practicamvvm.Rutas
import com.example.gestordetareas.Listado.ListadoTareasViewModel
import com.example.gestordetareas.Principal.PrincipalViewModel

import kotlinx.coroutines.launch

@Composable
fun Login(
    navController: NavHostController,
    principalViewModel: PrincipalViewModel,
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

    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            Text("Login", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

            IrPrincipalButton(){
                navController.navigate(Rutas.Principal)
            }
            Nombre(nombre){
                loginViewModel.setNombre(it)
            }
            Email(email) {
                loginViewModel.onRegistroCambiado(email = it, pass = password)
            }
            Spacer(modifier = Modifier.size(4.dp))
            Password(password) {
                loginViewModel.onRegistroCambiado(email = email, pass = it)
            }
            Registrar(isLoginEnable){
                Log.e("Fernando","${nombre}, ${password}, ${email}")
                try {
                    Amplify.Auth.signUp(
                        email,
                        password,
                        AuthSignUpOptions.builder()
                            .userAttribute(AuthUserAttributeKey.email(), email)
                            .userAttribute(AuthUserAttributeKey.name(), nombre).build(),
                        { result ->
                            //Las corrutinas son necesarias para que las variables se actualicen y puedan ser usadas en el finally.
                            coroutineScope.launch {
                                if (result.isSignUpComplete) {
                                    Log.i("Fernando", "Signup ok")
                                    loginViewModel.setRegistroCorrecto(1)
                                    Toast.makeText(context, "Registro correcto", Toast.LENGTH_SHORT).show()
                                } else {
                                    Log.i("Fernando", "Registro correcto, falta confirmación")
                                    loginViewModel.setRegistroCorrecto(2)
                                    Toast.makeText(context, "Registro correcto, falta confirmación", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        { error ->
                            coroutineScope.launch {
                                Log.e("Fernando", "Error en el registro fallido: ", error)
                                loginViewModel.setRegistroCorrecto(3)
                                Toast.makeText(context, "Error grave en el registro", Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
                catch (e:Exception){
                }
                finally {
                    if (isRegistroCorrecto == 1) {

                    }
                    if (isRegistroCorrecto == 2) {

                    }
                    if (isRegistroCorrecto == 3) {

                    }
                }
            }
            Login(isLoginEnable){
                //Bloque de Login
                Log.e("Fernando","${nombre}, ${password}, ${email}")
                try {
                    //Callbacks
                    Amplify.Auth.signIn(
                        email,
                        password,
                        { result ->
                            //Las corrutinas son necesarias para que funcione el Toast. En este ejemplo no uso variables como en el registro y llamo directamente al Toast.
                            coroutineScope.launch {
                                if (result.isSignedIn) {
                                    Log.i("Fernando", "Login correcto")
                                    navController.navigate(Rutas.Principal)
                                    Toast.makeText(context, "Login correcto", Toast.LENGTH_SHORT).show()
                                } else {
                                    Log.e("Fernando", "Algo ha fallado en el login")
                                    Toast.makeText(context, "Algo ha fallado en el login", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        { error ->
                            coroutineScope.launch {
                                Log.e("Fernando", error.toString())
                                Toast.makeText(context, error.message,Toast.LENGTH_SHORT).show()
                            }
                        }
                    )
                }
                catch (e:Exception){
                }
                finally {
                    if (isRegistroCorrecto == 1) {
                        Toast.makeText(context, "Registro correcto", Toast.LENGTH_SHORT).show()
                        loginViewModel.disableLogin()
                        loginViewModel.enableLogout()
                    }
                    if (isRegistroCorrecto == 2) {
                        Toast.makeText(context, "Algo ha fallado en el registro", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                //Bloque de confirmación de login (comentar / descomentar).
//                Amplify.Auth.confirmSignUp(
//
//                    "faranzabe@cifpvirgendegracia.com", "325464",
//
//                    { result ->
//
//                        if (result.isSignUpComplete) {
//
//                            Log.i("Fernando", "Confirm signUp succeeded")
//
//                        } else {
//
//                            Log.i("Fernando","Confirm sign up not complete")
//
//                        }
//
//                    },
//
//                    { Log.e("Fernando", "Failed to confirm sign up", it) }
//
//                )

                //Bloque para reenviar el código de confirmación
//                Amplify.Auth.resendUserAttributeConfirmationCode(
//                    AuthUserAttributeKey.email(),
//                    { Log.i("AuthDemo", "Code was sent again: $it") },
//                    { Log.e("AuthDemo", "Failed to resend code", it) }
//                )
            }
            Logout(isLogoutEnable){
                val options = AuthSignOutOptions.builder()
                    .globalSignOut(true)
                    .build()

                Amplify.Auth.signOut(options) { signOutResult ->
                    coroutineScope.launch {
                        if (signOutResult is AWSCognitoAuthSignOutResult.CompleteSignOut) {
                            Log.i("Fernando", "Logout correcto")
                            Toast.makeText(context,"Logout ok",Toast.LENGTH_SHORT).show()
                        } else if (signOutResult is AWSCognitoAuthSignOutResult.PartialSignOut) {
                        } else if (signOutResult is AWSCognitoAuthSignOutResult.FailedSignOut) {
                            Log.e("Fernando", "Algo ha fallado en el logout")
                            Toast.makeText(context,"Algo ha fallado en el logout",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }

            CerrarSesion()
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
    TextField(
        value = nombre,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFFAFAFA)),
        placeholder = { Text(text = "Nick") },
        label = {Text("Introduce un nick")},
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        colors = TextFieldDefaults.textFieldColors(
//            textColor = Color(0xFFB2B2B2),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
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
            .background(color = Color(0xFFFAFAFA)),
        placeholder = { Text(text = "Email") },
        label = {Text("Introduce un correo")},
        maxLines = 1,
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        colors = TextFieldDefaults.textFieldColors(
//            textColor = Color(0xFFB2B2B2),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Password(password: String, onTextChanged: (String) -> Unit) {
    var showPassword by remember { mutableStateOf(value = false) }
    TextField(
        value = password,
        onValueChange = { onTextChanged(it) },
        label = {Text("Introduce una clave")},
        modifier = Modifier
            .fillMaxWidth()
            .background(color = Color(0xFFFAFAFA)),
        placeholder = { Text("Password") },
        colors = TextFieldDefaults.textFieldColors(
//            textColor = Color(0xFFB2B2B2),
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        singleLine = true,
        maxLines = 1,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        trailingIcon = {
            if (showPassword) {
                IconButton(onClick = { showPassword = false }) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = "hide_password"
                    )
                }
            } else {
                IconButton(
                    onClick = { showPassword = true }) {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = "hide_password"
                    )
                }
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
fun CerrarSesion(){
    val activity = LocalContext.current as Activity
    Button(modifier = Modifier.fillMaxWidth(), onClick = { activity.finish() }) {
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
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Login")
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