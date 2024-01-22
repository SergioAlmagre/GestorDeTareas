package com.example.gestordetareas.Tarea

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavHostController
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Tarea
import com.example.gestordetareas.ElementosComunes.BotonCancelar
import com.example.gestordetareas.ListadoTareas.ListadoTareasViewModel
import com.example.gestordetareas.Rutas
import com.example.gestordetareas.Usuario.UsuarioViewModel
import kotlinx.coroutines.launch


@Composable
fun CrearTarea(
    navController: NavHostController,
    tareaViewModel: TareaViewModel,
    listadoTareasViewModel: ListadoTareasViewModel
) {
    val showDialog: Boolean by tareaViewModel.showDialog.observeAsState(false)
    var context = LocalContext.current

    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            Text(
                "Bienvenido",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            //Body( navController, principalViewModel, listadoViewModel)
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
//                AddTareaDialog(
//                    principalViewModel,
//                    showDialog,
//                    onDismiss = { principalViewModel.onDialogClose() },
//                    onTareaAdded = {
//                        principalViewModel.onDialogClose()
//                        listadoTareasViewModel.onTareaCreated(it)
//                    })
//            }
                Spacer(modifier = Modifier.size(300.dp))
                BotonCancelar(navController = navController, ruta = Rutas.listadoTareas)

            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTareaDialog(
    usuarioViewModel: UsuarioViewModel,
    tareaViewModel: TareaViewModel,
    show: Boolean,
    onDismiss: () -> Unit,
    onTareaAdded: (Tarea) -> Unit
) {
    val idUser: String by usuarioViewModel.id.observeAsState("")
    val descripcion: String by tareaViewModel.descripcion.observeAsState("")
    val estimacionHoras: Double by tareaViewModel.estimacionHoras.observeAsState(0.0)
    val horasInvertidas: Double by tareaViewModel.horasInvertidas.observeAsState(0.0)
    val dificultad: String by tareaViewModel.dificultad.observeAsState("")
    val estaFinalizada: Boolean by tareaViewModel.estaFinalizada.observeAsState(false)
    val estaAsignada: Boolean by tareaViewModel.estaAsignada.observeAsState(false)


    //En lugar de Dialog se puede usar Popup.
    if (show) {
        Dialog(
            onDismissRequest = { onDismiss() },

            //properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false) ) {
            properties = DialogProperties(
                dismissOnBackPress = false,
                dismissOnClickOutside = false
            )
        ) {
            Column(
                modifier = Modifier
//                .height(450.dp)
                    .background(Color.White)
                    .padding(20.dp)
            ) {
//                TituloDialogo(texto = "Nueva tarea")
                Spacer(modifier = Modifier.size(10.dp))
                Text("Descripción:")
                TextField(
                    value = descripcion,
                    onValueChange = { tareaViewModel.cambiarDescripcion(it) },
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text("Estimación de horas:")
                TextField(
                    value = estimacionHoras.toString(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    onValueChange = { tareaViewModel.cambiarEstimacionHoras(it) },
                    singleLine = true,
                    maxLines = 1
                )
                Spacer(modifier = Modifier.size(10.dp))
                Text("Dificultad:")

//                principalViewModel.cambiarDificultad(dificualtadList())

                Spacer(modifier = Modifier.size(10.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.size(40.dp))
                    Text("¿Completada?")
                    Checkbox(
                        checked = estaFinalizada,
                        onCheckedChange = { tareaViewModel.cambiarEsFinalizada(it) },
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                    Text("S/N")
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp)
                        .padding(10.dp)
                ) {
                    Spacer(modifier = Modifier.size(20.dp))
                    Button(
                        onClick = {

                            onDismiss()
                        }) {
                        Text("Cancelar")
                    }
                    Spacer(modifier = Modifier.size(30.dp))
                    Button(onClick = {
//                        val t = Tarea(descripcion,dificultad,dificultad,estimacionHoras,horasInvertidas,estaAsignada,estaFinalizada, idUser)
                        tareaViewModel.crearTarea(
                            descripcion,
                            dificultad,
                            estimacionHoras,
                            horasInvertidas,
                            estaAsignada,
                            estaFinalizada
                        )
//                        onTareaAdded(t)

                    }) {
                        Text("Añadir")
                    }
                }
            }
        }
    }
}




@Composable
fun IrListadoButton(onClickAction: (Boolean) -> Unit) {
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
        Text(text = "Ir al listado")
    }
}



@Composable
fun dificualtadList(): String {
    var soChoosed by remember {
        mutableStateOf("")
    }
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = soChoosed == "XS", onClick = { soChoosed = "XS" })
            Text(text = "XS")
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = soChoosed == "S", onClick = { soChoosed = "S" })
            Text(text = "S")
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = soChoosed == "M", onClick = { soChoosed = "M" })
            Text(text = "M")
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = soChoosed == "L", onClick = { soChoosed = "L" })
            Text(text = "L")
        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = soChoosed == "XL", onClick = { soChoosed = "XL" })
            Text(text = "XL")
        }
    }
    return soChoosed
}




//    @Composable
//    fun FloatingActionButtonabDialog(tareaViewModel: TareaViewModel) {
//        val miniFabSize = 40.dp
//        FloatingActionButton(
//            onClick = {
//                tareaViewModel.onShowDialogClick()
//            },
//            elevation = FloatingActionButtonDefaults.elevation(16.dp),
//            modifier = Modifier
//                .size(miniFabSize)
//                .fillMaxWidth()
//        ) {
//            Icon(Icons.Filled.Add, contentDescription = "Añadir usuario")
//        }
//    }


//    @Composable
//    fun TituloDialogo(texto: String) {
//        Text(
//            text = texto,
//            fontWeight = FontWeight.SemiBold,
//            fontSize = 20.sp,
//            modifier = Modifier.fillMaxWidth(),
//            textAlign = TextAlign.Center
//        )
//    }


