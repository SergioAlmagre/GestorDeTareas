package com.example.gestordetareas.CrearCuenta

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.example.a2_practicamvvm.Rutas
import com.example.gestordetareas.Listado.ListadoTareasViewModel

import com.example.gestordetareas.Login.LoginViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearCuenta(
    navController: NavHostController,
    listadoTareasViewModel: ListadoTareasViewModel,
    loginViewModel: LoginViewModel,
    crearCuentaViewModel: CrearCuentaViewModel
) {
    var context = LocalContext.current
    val nombre:String by crearCuentaViewModel.nombre.observeAsState("")
    val email:String by crearCuentaViewModel.email.observeAsState("")
    val password:String by crearCuentaViewModel.pwd.observeAsState("")
    val repPassword:String by crearCuentaViewModel.pwd2.observeAsState("")
    val codigoVerificacion by crearCuentaViewModel.codigoVerificacion.observeAsState("")
    val isLoginEnable: Boolean by crearCuentaViewModel.isLoginEnable.observeAsState(initial = false)
    //var isRegistroCorrecto by remember { mutableStateOf(value = 0) }
    val isRegistroCorrecto:Int by crearCuentaViewModel.isRegistroCorrecto.observeAsState(initial = 0)
    val coroutineScope = rememberCoroutineScope()

    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            Text("FOTO", fontSize = 150.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

            Nombre(nombre){
                crearCuentaViewModel.setNombre(it)
            }
            Email(email) {
                crearCuentaViewModel.onRegistroCambiado(email = it, pwd1 = password, pwd2 = repPassword)
            }
            Spacer(modifier = Modifier.size(4.dp))
            Password(password) {
                crearCuentaViewModel.onRegistroCambiado(email = email, pwd1 = it, pwd2 = repPassword)
            }
            RepetirContraseña(repPassword) {
                crearCuentaViewModel.onRegistroCambiado(email = email, pwd1 = password, pwd2 = it)
            }
            Spacer(modifier = Modifier.size(20.dp))

            RegistrarYEnviarCodVerificacion(isLoginEnable){
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
                                    Log.i("Sergio", "Signup ok")
                                    crearCuentaViewModel.setRegistroCorrecto(1)
                                    Toast.makeText(context, "Registro correcto", Toast.LENGTH_SHORT).show()

                                } else {
                                    Log.i("Sergio", "Registro correcto, falta confirmación")
                                    crearCuentaViewModel.setRegistroCorrecto(2)
                                    Toast.makeText(context, "Registro correcto, falta confirmación", Toast.LENGTH_SHORT).show()
                                }
                            }
                        },
                        { error ->
                            coroutineScope.launch {
                                Log.e("Sergio", "Error en el registro fallido: ", error)
                                crearCuentaViewModel.setRegistroCorrecto(3)
                                Toast.makeText(context, "Error grave en el registro", Toast.LENGTH_SHORT).show()
                                if (error is AmplifyException) {
                                    val cause = error.cause
                                    if (cause != null) {
                                        Log.e("Sergio", "Cause: ${cause.message}")
                                        // Puedes acceder a más detalles del error aquí según sea necesario
                                    }
                                }
                            }
                        }
                    )
                }
                catch (e:Exception){
                }
                finally {
                    if (isRegistroCorrecto == 1) {
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
                                        Log.e("Sergio", error.toString())
                                        Toast.makeText(context, error.message,Toast.LENGTH_SHORT).show()
                                    }
                                }
                            )
                        }
                        catch (e:Exception){
                        }
                    }
                    if (isRegistroCorrecto == 2) {

                    }
                    if (isRegistroCorrecto == 3) {

                    }
                }
            }
            Spacer(modifier = Modifier.size(60.dp))
            Row {
                CodigoVerificacionBox(codigoVerificacion){
                    crearCuentaViewModel.onCodigoVerificacionCambiado(it)
                }
                Spacer(modifier = Modifier.size(10.dp))
                Card(
                    onClick = {
                        Amplify.Auth.confirmSignUp(
                        email, codigoVerificacion,
                        { result ->
                            coroutineScope.launch {
                                if (result.isSignUpComplete) {
                                    Log.i("Sergio", "Confirm signUp succeeded")
                                    Toast.makeText(
                                        context,
                                        "Registro confirmado exitosamente",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Log.i("Sergio", "Confirm sign up not complete")
                                }
                            }
                        },
                        { error ->
                            coroutineScope.launch {
                                Log.e("Sergio", "Failed to confirm sign up", error)
                                Toast.makeText(
                                    context,
                                    "Error al confirmar el registro",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) /* Acción al hacer clic */ },
                    modifier = Modifier
//                        .padding(10.dp)
                        .width(180.dp)
                        .height(65.dp)
                ) {
                    Box(modifier = Modifier
                        .background(Color(0xFFE0FFDC))
                        .fillMaxWidth()
                        .fillMaxHeight(),
                        contentAlignment = Alignment.Center) {
                        Text("Validar",
                            Modifier.padding(10.dp),
                            fontSize = 20.sp)
                    }
                }
            }
            ReenviarCodigoVerificacion()
            Spacer(modifier = Modifier.size(40.dp))
            Cancelar(navController)

        }

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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepetirContraseña(password: String, onTextChanged: (String) -> Unit) {
    var showPassword by remember {mutableStateOf (value = false)}

    TextField(
        value = password,
        onValueChange = { onTextChanged(it) },
        label = { Text("Repite tu contraseña") },
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CodigoVerificacionBox(codigoVerificacion: String, onTextChanged: (String) -> Unit) {
    OutlinedTextField(
        value = codigoVerificacion,
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .padding(bottom = 10.dp)
            .width(200.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color = Color(0xFFE0E1EB)),
        placeholder = { Text(text = "Codigo de verficación") },
        label = {Text("Introduce el código")},
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


@Composable
fun Cancelar(navController: NavHostController) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { navController.navigate(Rutas.Login) },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Cancelar")
    }
}

@Composable
fun RegistrarYEnviarCodVerificacion(isRegistroEnable : Boolean, onClickAction: (Boolean) -> Unit){
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
        Text(text = "Enviar código de verificación")
    }
}

@Composable
fun ReenviarCodigoVerificacion() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    ClickableText(
        text = AnnotatedString("Reenviar código de verificación"),
        onClick = {
            // Bloque para reenviar el código de confirmación
            Amplify.Auth.resendUserAttributeConfirmationCode(
                AuthUserAttributeKey.email(),
                { Log.i("AuthDemo", "Code was sent again: $it") },
                { Log.e("AuthDemo", "Failed to resend code", it) }
            )

            // Mostrar Toast con un delay para dar tiempo a que Amplify procese la solicitud
            coroutineScope.launch {
                delay(500) // Ajusta el tiempo de espera según sea necesario
                Toast.makeText(context, "Código de verificación reenviado", Toast.LENGTH_SHORT).show()
            }
        },
        modifier = Modifier
            .padding(bottom = 8.dp),
        style = TextStyle(fontSize = 13.sp)
    )
}


