package com.example.gestordetareas.CrearCuenta

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavHostController
import com.amplifyframework.AmplifyException
import com.amplifyframework.auth.AuthUserAttributeKey
import com.amplifyframework.auth.options.AuthSignUpOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Usuario
import com.example.gestordetareas.ElementosComunes.BotonCancelar
import com.example.gestordetareas.ElementosComunes.InterVentana
import com.example.gestordetareas.ElementosComunes.Rutas
import com.example.gestordetareas.R
import com.example.gestordetareas.Usuario.UsuarioViewModel
import com.google.accompanist.coil.rememberCoilPainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearUsuario(
    navController: NavHostController,
    usuarioViewModel: UsuarioViewModel,
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
//            Text("FOTO", fontSize = 150.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            ImagenUsuario(usuarioViewModel) {

            }
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

            Box(modifier = Modifier
                .fillMaxWidth(),
                contentAlignment = Alignment.Center) {
                RegistrarYEnviarCodVerificacion(isLoginEnable){
                    Log.e("Sergio","${nombre}, ${password}, ${email}")
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
                                        Toast.makeText(context, "Código enviado! revisa tu email", Toast.LENGTH_SHORT).show()
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
                                                // ATENTO A ESTA PARTE, POSIBLES FALLOS
                                                if(InterVentana.usuarioActivo!!.rol == 0){
                                                    navController.navigate(Rutas.listadoUsuarios)
                                                    Log.i("Sergio", "Login correcto")
                                                    Toast.makeText(context, "Login correcto", Toast.LENGTH_SHORT).show()
                                                }
                                                if(InterVentana.usuarioActivo!!.rol == 1){
                                                    navController.navigate(Rutas.listadoTareas)
                                                    Log.i("Sergio", "Login correcto")
                                                    Toast.makeText(context, "Login correcto", Toast.LENGTH_SHORT).show()
                                                }
                                            } else {
                                                Log.e("Sergio", "Algo ha fallado en el login")
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

                                    var usu = Usuario.builder().rol(Rutas.rolProgramador).nombreCompleto(nombre).email(email).tareasFinalizadas(0).fotoPerfil(Rutas.imagenPerfilDefault).build()

                                    usuarioViewModel.insertarUsuario(
                                        usu).thenAccept { success ->
                                        if (success) {
                                            GlobalScope.launch(Dispatchers.Main) {//Para que se ejecute en el hilo principal
                                                navController.navigate(Rutas.listadoTareas)
                                            }

                                        } else {
                                                                                    // Error al guardar el usuario
                                        }
                                    }
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
            if(InterVentana.usuarioActivo != null){
                BotonCancelar(navController, Rutas.listadoUsuarios)
            }else{
                BotonCancelar(navController, Rutas.login)
            }


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
fun RegistrarYEnviarCodVerificacion(isRegistroEnable : Boolean, onClickAction: (Boolean) -> Unit){
    Button(
        onClick = {
            onClickAction(true);
        },
        enabled = isRegistroEnable,
        modifier = Modifier
            .width(250.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Solicitar código de verificación")
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

@Composable
fun ImagenUsuario(
    usuarioViewModel: UsuarioViewModel,
    onImageSelected: (String) -> Unit
) {
    val context = LocalContext.current
    var isGalleryOpen by remember { mutableStateOf(false) }
    val fotoPerfil: String by usuarioViewModel.fotoPerfil.observeAsState("")
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    selectedImageUri = fotoPerfil.toUri()
    val esGuardar: Boolean by usuarioViewModel.esGuardar.observeAsState(false)


    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            selectedImageUri = it
            onImageSelected(it.toString())
            usuarioViewModel.cambiarSelectedImageUri(it)
        }
    }

    DisposableEffect(isGalleryOpen) {
        if (isGalleryOpen) {
            // Lanza la actividad de la galería utilizando ACTION_OPEN_DOCUMENT
            launcher.launch("image/*")

            // Marca el estado como cerrado después de seleccionar la imagen
            isGalleryOpen = false
        }

        // Limpia el efecto cuando el compositor se desmonta
        onDispose { }
    }

    Surface(
        modifier = Modifier
            .size(250.dp)
            .fillMaxWidth()
            .offset(x = 70.dp)
            .clip(CircleShape)
            .clickable {
                // Cambia el estado para abrir la galería
                isGalleryOpen = true
            },
    ) {
        val archivo = selectedImageUri?.let { uri ->
            usuarioViewModel.getRealPathFromURI(context, uri)?.let { ruta ->
                File(ruta)
            }
        }

        Box(
            modifier = Modifier
                .size(250.dp)
                .padding(8.dp)
        ) {
            if (selectedImageUri != null && !selectedImageUri.toString().isNullOrBlank()){
                Log.i("Sergio", "Imagen: " + selectedImageUri.toString())
                // Si hay una URI de imagen seleccionada, muestra la imagen
                Image(
                    painter = rememberCoilPainter(
                        request = selectedImageUri.toString(),
                        fadeIn = true
                    ),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(250.dp)
                )
//
//                if(esGuardar){
//                    usuarioViewModel.cambiarFotoPerfil(selectedImageUri.toString())
//                    usuarioViewModel.subirFotoDePerfil(context,selectedImageUri.toString().toUri())
//                    usuarioViewModel.cambiarEsGuardar(false)
//                    Log.i("Sergio", "Imagen: " + selectedImageUri.toString())
//                }

                // para probar a subir la imagen a lo bruto pero no la sube ni da error, probablemente sea algún problema de permisos
//                archivo?.let {
//                    Amplify.Storage.uploadFile(
//                        "fotosperfil/${usuarioViewModel.id}",  // Ruta en el bucket de AWS
//                        archivo,
//                        { result ->
//                            // Éxito al subir el archivo, obtén la URL de la imagen en AWS
//                            Amplify.Storage.getUrl(
//                                result.key,
//                                { url ->
//                                    // Éxito al obtener la URL, llama a la función de callback con la URL
//                                    url.toString()
//                                },
//                                { error ->
//                                    // Manejar error al obtener la URL
//                                    Log.e("ObtenerUrlAWS", "Error al obtener la URL de la imagen en AWS: $error")
//                                }
//                            )
//                        },
//                        { error ->
//                            // Manejar error al subir el archivo
//                            Log.e("SubidaAWS", "Error al subir la imagen a AWS: $error")
//                        }
//                    )
//                }

            } else {
                // Si no hay una imagen seleccionada, muestra la foto de perfil por defecto
                Log.i("Sergio", "Dentro del else de imagen por defecto: " + selectedImageUri.toString())
                Image(
                    painter = rememberCoilPainter(
                        request = Rutas.imagenPerfilDefault,
                        fadeIn = true
                    ),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(250.dp)
                )
            }
        }
    }
}
