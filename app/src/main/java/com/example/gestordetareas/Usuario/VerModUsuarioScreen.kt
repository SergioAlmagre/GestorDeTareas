package com.example.gestordetareas.Usuario

import android.net.Uri
import android.os.StrictMode
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.gestordetareas.ElementosComunes.InterVentana
import com.example.gestordetareas.ElementosComunes.Rutas
import com.example.gestordetareas.ListaUsuarios.ListadoUsuariosViewModel
import com.example.gestordetareas.R
import com.google.accompanist.coil.rememberCoilPainter
import java.io.File


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModificarUsuario(
    navController: NavHostController,
    usuarioViewModel: UsuarioViewModel,
    modUsuarioViewModel: ModUsuarioViewModel,
    listadoUsuariosViewModel: ListadoUsuariosViewModel
) {
    var context = LocalContext.current
    val nombre: String by usuarioViewModel.nombreCompleto.observeAsState("")
    val rol: Int by usuarioViewModel.rol.observeAsState(0)
    val tareasFinalizadas: Int by usuarioViewModel.tareasFinalizadas.observeAsState(0)
    val id: String by usuarioViewModel.id.observeAsState("")
    val fotoPerfil: String by usuarioViewModel.fotoPerfil.observeAsState("")
    val email: String by usuarioViewModel.email.observeAsState("")

    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            ImagenUsuario(usuarioViewModel){

            }
            NombreMod(nombre){
                usuarioViewModel.cambiarNombre(it)
            }
            if(InterVentana.usuarioActivo!!.rol == Rutas.rolProgramador){
                Text(text = "Rol", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                radioButtonsRolNoEdit(usuarioViewModel)
                Spacer(modifier = Modifier.size(4.dp))
                Spacer(modifier = Modifier.size(20.dp))
                Box(modifier = Modifier
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center) {

                }
                Spacer(modifier = Modifier.size(60.dp))
                Row {

                    Spacer(modifier = Modifier.size(10.dp))

                }

                Spacer(modifier = Modifier.size(40.dp))
                Text(text = "Tareas finalizadas", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

                TareasFinalizadasTextBoxNoEdit(usuarioViewModel)
            }
            if(InterVentana.usuarioActivo!!.rol == Rutas.rolAdministrador){
                Text(text = "Rol", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
                radioButtonsRol(usuarioViewModel)
                Spacer(modifier = Modifier.size(4.dp))
                Spacer(modifier = Modifier.size(20.dp))
                Box(modifier = Modifier
                    .fillMaxWidth(),
                    contentAlignment = Alignment.Center) {

                }
                Spacer(modifier = Modifier.size(60.dp))
                Row {

                    Spacer(modifier = Modifier.size(10.dp))

                }

                Spacer(modifier = Modifier.size(40.dp))
                Text(text = "Tareas finalizadas", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

                TareasFinalizadasTextBox(usuarioViewModel){
                    usuarioViewModel.cambiarTareasFinalizadas(it.toInt())
                }
            }

            BotonGuardarDatosModUsu(navController,
                Rutas.listadoUsuarios, usuarioViewModel, listadoUsuariosViewModel)
            if(InterVentana.usuarioActivo!!.rol == Rutas.rolProgramador){
                BotonCancelarVerModUsuarios(navController, Rutas.listadoTareas)
            }
            if (InterVentana.usuarioActivo!!.rol == Rutas.rolAdministrador){
                BotonCancelarVerModUsuarios(navController, Rutas.listadoUsuarios)
            }

        }
    }
}

//@Composable
//fun ImagenUsuario(usuarioViewModel: UsuarioViewModel, onImageClick: () -> Unit) {
//    val userId: String by usuarioViewModel.id.observeAsState("")
//    val fotoPerfil: String by usuarioViewModel.fotoPerfil.observeAsState("")
//    val imageUrl = getImageUrlFromAWS(userId)
//
//    Box(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(8.dp),
//        contentAlignment = Alignment.TopCenter
//    ) {
//        val painter: Painter = rememberCoilPainter(request = imageUrl, fadeIn = true)
//
//        if (fotoPerfil == "fotodefault") {
//            Image(
//                painter = painterResource(id = R.drawable.usuariorobot),
//                contentDescription = "Avatar",
//                modifier = Modifier
//                    .size(250.dp)
//                    .clip(CircleShape)
//                    .clickable { onImageClick() }
//            )
//        } else {
//            Image(
//                painter = painterResource(id = R.drawable.usuariorobot),
////                painter = painter,
//                contentDescription = "Avatar",
//                modifier = Modifier
//                    .size(250.dp)
//                    .clip(CircleShape)
//                    .clickable { onImageClick() }
//            )
//        }
//    }
//}



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
            if (selectedImageUri != null) {
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
                Image(
                    painter = painterResource(id = R.drawable.usuariorobot),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(250.dp)
                )
            }
        }
    }
}





@Composable
fun radioButtonsRol(usuarioViewModel: UsuarioViewModel) {
//    var usu = usuarioViewModel.obtenerUsuarioActual()
//    Log.i("Sergio", "Rol: ${usu.toString()}")

    val rolChoosed: Int by usuarioViewModel.rol.observeAsState(0)
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = rolChoosed == 0, onClick = {
                usuarioViewModel.cambiarRol(0)
            })
            Text(text = "Administrador")

        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = rolChoosed == 1, onClick = {
                usuarioViewModel.cambiarRol(1)
            })
            Text(text = "Programador")
        }
    }
}

@Composable
fun radioButtonsRolNoEdit(usuarioViewModel: UsuarioViewModel) {
//    var usu = usuarioViewModel.obtenerUsuarioActual()
//    Log.i("Sergio", "Rol: ${usu.toString()}")
    val rolChoosed: Int by usuarioViewModel.rol.observeAsState(0)

    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = rolChoosed == 0, onClick = {

            })
            Text(text = "Administrador")

        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = rolChoosed == 1, onClick = {

            })
            Text(text = "Programador")
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NombreMod(nombre: String, onTextChanged: (String) -> Unit) {
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


@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable // RESVISAR ORIGEN DE DATOS!!!!!!!!!
fun TareasFinalizadasTextBox(usuarioViewModel: UsuarioViewModel, onTextChanged: (String) -> Unit){
    val numeroTareas: Int by  usuarioViewModel.tareasFinalizadas.observeAsState(0)

    OutlinedTextField(
        value = numeroTareas.toString(),
        onValueChange = { onTextChanged(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color = Color(0xFFE0E1EB)),
        placeholder = { Text(text = "Tareas finalizadas") },
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

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun TareasFinalizadasTextBoxNoEdit(usuarioViewModel: UsuarioViewModel){
    val numeroTareas: Int by  usuarioViewModel.tareasFinalizadas.observeAsState(0)

    OutlinedTextField(
        value = numeroTareas.toString(),
        onValueChange = {  },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(color = Color(0xFFE0E1EB)),
        placeholder = { Text(text = "Tareas finalizadas") },
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
fun BotonGuardarDatosModUsu(navController: NavController, ruta:String, usuarioViewModel: UsuarioViewModel, listadoUsuariosViewModel: ListadoUsuariosViewModel) {
//    var usuarioActual = usuarioViewModel.obtenerUsuarioActual()
//    usuarioViewModel.cambiarId("1")     ///RESVISAR ORIGEN DE DATOS!!!!!!!!!
//    usuarioViewModel.cambiarNombreUsuarioActual("Sergio Núñez Bautista") ///RESVISAR ORIGEN DE DATOS!!!!!!!!!
    val context = LocalContext.current
    val selectedImageUri: Uri? by usuarioViewModel.selectedImageUri.observeAsState(null)

    Button(
        onClick = {


//            usuarioViewModel.subirFotoDePerfil(context,fotoPerfil.toUri())

            usuarioViewModel.subirFotoDePerfil(context,selectedImageUri.toString().toUri())
            usuarioViewModel.cambiarFotoPerfil(selectedImageUri.toString())

            usuarioViewModel.cambiarEsGuardar(true)
            var usu = usuarioViewModel.getUsuarioByAtributosSueltos()
            usuarioViewModel.guardarModificarUsuario(usu)
            listadoUsuariosViewModel.getUsers()
            Log.i("Sergio", "Boton guardar:" + usuarioViewModel.toString())
            Log.i("Sergio", "Boton guardar:" + usu)
            navController.navigate(ruta)
        },
        modifier = Modifier
            .fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Guardar datos")
    }
}



@Composable
fun BotonCancelarVerModUsuarios(navController: NavController, ruta:String) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { navController.navigate(ruta) },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Cancelar")
    }
}




//@Composable
//fun ReenviarCodigoVerificacion() {
//    val context = LocalContext.current
//    val coroutineScope = rememberCoroutineScope()
//
//    ClickableText(
//        text = AnnotatedString("Reenviar código de verificación"),
//        onClick = {
//            // Bloque para reenviar el código de confirmación
//            Amplify.Auth.resendUserAttributeConfirmationCode(
//                AuthUserAttributeKey.email(),
//                { Log.i("AuthDemo", "Code was sent again: $it") },
//                { Log.e("AuthDemo", "Failed to resend code", it) }
//            )
//
//            // Mostrar Toast con un delay para dar tiempo a que Amplify procese la solicitud
//            coroutineScope.launch {
//                delay(500) // Ajusta el tiempo de espera según sea necesario
//                Toast.makeText(context, "Código de verificación reenviado", Toast.LENGTH_SHORT).show()
//            }
//        },
//        modifier = Modifier
//            .padding(bottom = 8.dp),
//        style = TextStyle(fontSize = 13.sp)
//    )
//}