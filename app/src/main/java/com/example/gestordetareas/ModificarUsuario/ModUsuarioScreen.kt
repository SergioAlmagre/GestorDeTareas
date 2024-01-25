package com.example.gestordetareas.ModificarUsuario

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.gestordetareas.ElementosComunes.BotonCancelar
import com.example.gestordetareas.ElementosComunes.BotonGuardarDatos
import com.example.gestordetareas.ListaUsuarios.ListadoUsuariosViewModel
import com.example.gestordetareas.Rutas
import com.example.gestordetareas.Usuario.UsuarioViewModel

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


    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            Text("FOTO", fontSize = 150.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())

            NombreMod(nombre){
                usuarioViewModel.cambiarNombre(it)
            }
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

            BotonGuardarDatos(navController,Rutas.listadoUsuarios, usuarioViewModel, listadoUsuariosViewModel)
            BotonCancelar(navController, Rutas.listadoUsuarios)
        }
    }
}




@Composable
fun radioButtonsRol(usuarioViewModel: UsuarioViewModel) {
    var usu = usuarioViewModel.obtenerUsuarioActual()
    Log.i("Sergio", "Rol: ${usu.toString()}")
    var soChoosed by remember {
        mutableStateOf(usu!!.rol)
    }
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = soChoosed == 0, onClick = {
                soChoosed = 0
                usuarioViewModel.cambiarRol(0)
            })
            Text(text = "Administrador")

        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = soChoosed == 1, onClick = {
                soChoosed = 1
                usuarioViewModel.cambiarRol(1)
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
@Composable
fun TareasFinalizadasTextBox(usuarioViewModel: UsuarioViewModel, onTextChanged: (String) -> Unit){
    var numeroTareas by remember {
        mutableStateOf(usuarioViewModel.obtenerUsuarioActual()!!.tareasFinalizadas.toString())
    }
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