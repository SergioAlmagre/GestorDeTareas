package com.example.gestordetareas.Tarea

import android.util.Log
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
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.amplifyframework.datastore.generated.model.Tarea
import com.example.gestordetareas.ElementosComunes.BotonCancelar
import com.example.gestordetareas.ElementosComunes.InterVentana
import com.example.gestordetareas.ListaUsuarios.ListadoUsuariosViewModel
import com.example.gestordetareas.ElementosComunes.Rutas
import com.example.gestordetareas.ListadoTareas.ListadoTareasViewModel
import com.example.gestordetareas.Usuario.UsuarioViewModel
import kotlinx.coroutines.launch


@Composable
fun VerTarea(
    listadoTareasViewModel: ListadoTareasViewModel,
    listadoUsuariosViewModel: ListadoUsuariosViewModel,
    usuarioViewModel: UsuarioViewModel,
    navController: NavHostController,
    tareaViewModel: TareaViewModel
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
                "Tarea",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TareaVerBody(listadoTareasViewModel ,listadoUsuariosViewModel ,usuarioViewModel ,navController , tareaViewModel = tareaViewModel, show = showDialog) {

                }

            }
            Spacer(modifier = Modifier.size(300.dp))
            BotonCancelar(navController = navController, ruta = Rutas.listadoTareas)

        }
    }
}


@Composable
fun TareaVerBody(
    listadoTareasViewModel: ListadoTareasViewModel,
    listadoUsuariosViewModel: ListadoUsuariosViewModel,
    usuarioViewModel: UsuarioViewModel,
    navController: NavController,
    tareaViewModel: TareaViewModel,
    show: Boolean,
    onDismiss: () -> Unit
){
    val idTarea: String by tareaViewModel.idTarea.observeAsState("")
    val idUser: String by usuarioViewModel.id.observeAsState("")
    val descripcion: String by tareaViewModel.descripcion.observeAsState("")
    val estimacionHoras: Double by tareaViewModel.estimacionHoras.observeAsState(0.0)
    val horasInvertidas: Double by tareaViewModel.horasInvertidas.observeAsState(0.0)
    val dificultad: String by tareaViewModel.dificultad.observeAsState("")
    val estaFinalizada: Boolean by tareaViewModel.estaFinalizada.observeAsState(false)
    val estaAsignada: Boolean by tareaViewModel.estaAsignada.observeAsState(false)
    val coroutineScope = rememberCoroutineScope()
    var isExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .width(400.dp)
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {

            if(InterVentana.usuarioActivo!!.rol == Rutas.rolAdministrador){
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            isExpanded = true
                        }
                    }
                ) {
                    Icon(Icons.Default.PersonAdd, contentDescription = "Asiganar tarea")
                }
                if(tareaViewModel.estaAsignadaONull(estaAsignada)){
                    Text("Asignada a: ")
                    Text(usuarioViewModel.nombreCompleto.value.toString())
                }else{
                    Text("Tarea sin asignar")
                }
                DropDownUsuariosVer(
                    listadoUsuariosViewModel = listadoUsuariosViewModel,
                    isExpanded = isExpanded,
                    setExpanded = { isExpanded = it },
                    setSelected = {}
                )
            }
            if(InterVentana.usuarioActivo!!.rol == Rutas.rolProgramador){
                if (tareaViewModel.estaAsignadaONull(estaAsignada)) {
                    IconButton(
                        onClick = {
                            Log.i("Sergio", "Se ha pulsado el botón para desasignar la tarea")
                            tareaViewModel.setIdUsuarioAsignado(null)
                            tareaViewModel.cambiarEstaAsignada(false)
                        }
                    ) {
                        Icon(Icons.Default.Cancel, contentDescription = "Desasignar tarea")
                    }
                    Text("Dejar asignación de esta tarea")
                } else {
                    IconButton(
                        onClick = {
                            tareaViewModel.setIdUsuarioAsignado(InterVentana.usuarioActivo!!.id)
                            Log.i("Sergio", "Se ha pulsado el botón para asignar la tarea")
                            tareaViewModel.cambiarEstaAsignada(true)
                        }
                    ) {
                        Icon(Icons.Default.PersonAdd, contentDescription = "Asignar tarea")
                    }
                    Text("Asignarme esta tarea")
                }
            }
        }
        Spacer(modifier = Modifier.size(10.dp))
        Text("Descripción:")
        TextField(
            value = descripcion,
            onValueChange = { tareaViewModel.cambiarDescripcion(it) },
            singleLine = false,
            maxLines = 8,
            minLines = 8,
            modifier = Modifier.width(400.dp)
        )
        Spacer(modifier = Modifier.size(10.dp))
        Text("Dificultad:")
        tareaViewModel.cambiarDificultad(dificualtadListVer(tareaViewModel))
        Spacer(modifier = Modifier.size(10.dp))

        Spacer(modifier = Modifier.size(10.dp))
        Text("Horas estimadas:")
        TextField(
            value = estimacionHoras.toString(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            onValueChange = { tareaViewModel.cambiarEstimacionHoras(it)},
            singleLine = true,
            modifier = Modifier.width(400.dp)
        )
        Text("Horas invertidas:")
        TextField(
            value = horasInvertidas.toString(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            onValueChange = { tareaViewModel.cambiarHorasInvertidas(it)},
            singleLine = true,
            modifier = Modifier.width(400.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                .fillMaxWidth()
                .height(70.dp))
        {
            Spacer(Modifier.size(70.dp))
            Text("Estado")
            Spacer(Modifier.size(70.dp))
            Text("Finalizar")
        }

        Row(verticalAlignment = Alignment.CenterVertically)

        {
            Text("                    ")
            mostrarPorcentaje(tareaViewModel)
            Text("                    ")
            var n = usuarioViewModel.getTareasFinalizadas()
            Checkbox(
                checked = estaFinalizada,
                onCheckedChange = { tareaViewModel.cambiarEsFinalizada(it)
                    usuarioViewModel.cambiarTareasFinalizadas(n+1)
                    },
                modifier = Modifier.align(Alignment.CenterVertically)
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
                .padding(10.dp)
        ) {
            Spacer(modifier = Modifier.size(40.dp))
            Button(
                onClick = {
                    navController.navigate(Rutas.listadoTareas)
                    onDismiss()
                }) {
                Text("Cancelar")
            }
            Spacer(modifier = Modifier.size(50.dp))

            Button(onClick = {

                val t = tareaViewModel.getTareaByAtributosSueltos()

                if(InterVentana.usuarioActivo!!.rol == Rutas.rolProgramador){
                    listadoTareasViewModel.getTareasNoFinalizadas()
                }
                if(InterVentana.usuarioActivo!!.rol == Rutas.rolAdministrador){
                    listadoTareasViewModel.getTodasLasTareas()
                }

                tareaViewModel.guardarModificarTarea(t)

                if(estaFinalizada){
                    usuarioViewModel.guardarModificarUsuario(InterVentana.usuarioActivo!!)
                }

                navController.navigate(Rutas.listadoTareas)
                onDismiss()
            }) {
                Text("Guardar datos")
            }
        }
    }
}

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun DropDownUsuariosVerTarea(listadoUsuariosViewModel: ListadoUsuariosViewModel, isExpanded: Boolean, setExpanded: (Boolean) -> Unit, setSelected:(String)->Unit) {
//    var expanded by remember { mutableStateOf(false) }
////    var usuarios = listOf<String>("Álvaro", "José", "Lorenzo", "Ramón", "Sergio", "María")
//    val usuarios = listadoUsuariosViewModel.usuarios.map { it.nombreCompleto }
//    Column(modifier = Modifier.padding(10.dp)) {
//        DropdownMenu(expanded = isExpanded, onDismissRequest = {
//            setExpanded(false)
//        })
//        {
//            usuarios.forEach {
//                DropdownMenuItem(text = { Text(text = it) }, onClick = {
//                    setExpanded (false)
//                    setSelected(it)
//                })
//            }
//        }
//    }
//}




@Composable
fun dificualtadListVer(tareaViewModel: TareaViewModel): String {
//    var diChoosed by remember {
//        mutableStateOf("")
//    }
    var diChoosed: String = tareaViewModel.dificultad.value.toString()

    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = diChoosed == "XS", onClick = { diChoosed = "XS" })

            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = diChoosed == "S", onClick = { diChoosed = "S" })

            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = diChoosed == "M", onClick = { diChoosed = "M" })

            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = diChoosed == "L", onClick = { diChoosed = "L" })

            Spacer(modifier = Modifier.width(10.dp))
            RadioButton(selected = diChoosed == "XL", onClick = { diChoosed = "XL" })

        }
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Spacer(modifier = Modifier.width(25.dp))
            Text(text = "XS")
            Spacer(modifier = Modifier.width(40.dp))
            Text(text = "S")
            Spacer(modifier = Modifier.width(50.dp))
            Text(text = "M")
            Spacer(modifier = Modifier.width(50.dp))
            Text(text = "L")
            Spacer(modifier = Modifier.width(40.dp))
            Text(text = "XL")
        }

    }
    return diChoosed
}

@Composable
fun mostrarPorcentaje(tareaViewModel: TareaViewModel){
    val horas: Double by tareaViewModel.horasInvertidas.observeAsState(0.0)
    val estimacionHoras: Double by tareaViewModel.estimacionHoras.observeAsState(0.0)

    if(tareaViewModel.esPorcenajeYHorasValido(horas,estimacionHoras)){
        Text(tareaViewModel.calcularPorcentaje(tareaViewModel.horasInvertidas.value!!.toDouble(),tareaViewModel.estimacionHoras.value!!.toDouble()))
    }else{
        Text("0%", fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownUsuariosVer(listadoUsuariosViewModel: ListadoUsuariosViewModel, isExpanded: Boolean, setExpanded: (Boolean) -> Unit, setSelected:(String)->Unit) {
    var expanded by remember { mutableStateOf(false) }
    val usuarios = listadoUsuariosViewModel.usuarios.map { it.nombreCompleto }
    Column(modifier = Modifier.padding(10.dp)) {
        DropdownMenu(expanded = isExpanded, onDismissRequest = {
            setExpanded(false)
        })
        {
            usuarios.forEach {
                DropdownMenuItem(text = { Text(text = it) }, onClick = {
                    setExpanded(false)
                    setSelected(it)
                })
            }
        }
    }
}

//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun AddTareaDialog(
//    usuarioViewModel: UsuarioViewModel,
//    tareaViewModel: TareaViewModel,
//    show: Boolean,
//    onDismiss: () -> Unit,
//    onTareaAdded: (Tarea) -> Unit
//) {
//    val idUser: String by usuarioViewModel.id.observeAsState("")
//    val descripcion: String by tareaViewModel.descripcion.observeAsState("")
//    val estimacionHoras: Double by tareaViewModel.estimacionHoras.observeAsState(0.0)
//    val horasInvertidas: Double by tareaViewModel.horasInvertidas.observeAsState(0.0)
//    val dificultad: String by tareaViewModel.dificultad.observeAsState("")
//    val estaFinalizada: Boolean by tareaViewModel.estaFinalizada.observeAsState(false)
//    val estaAsignada: Boolean by tareaViewModel.estaAsignada.observeAsState(false)
//
//
//    //En lugar de Dialog se puede usar Popup.
//    if (show) {
//        Dialog(
//            onDismissRequest = { onDismiss() },
//
//            //properties = DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false) ) {
//            properties = DialogProperties(
//                dismissOnBackPress = false,
//                dismissOnClickOutside = false
//            )
//        ) {
//            Column(
//                modifier = Modifier
//                    .width(400.dp)
////                .height(450.dp)
//                    .background(Color.White)
//                    .padding(20.dp)
//            ) {
////                TituloDialogo(texto = "Nueva tarea")
//                Spacer(modifier = Modifier.size(10.dp))
//                Text("Descripción:")
//                TextField(
//                    value = descripcion,
//                    onValueChange = { tareaViewModel.cambiarDescripcion(it) },
//                    singleLine = true,
//                    maxLines = 1
//                )
//                Spacer(modifier = Modifier.size(10.dp))
//                Text("Estimación de horas:")
//                TextField(
//                    value = estimacionHoras.toString(),
//                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//                    onValueChange = { tareaViewModel.cambiarEstimacionHoras(it) },
//                    singleLine = true,
//                    maxLines = 1
//                )
//                Spacer(modifier = Modifier.size(10.dp))
//                Text("Dificultad:")
//
////                principalViewModel.cambiarDificultad(dificualtadList())
//
//                Spacer(modifier = Modifier.size(10.dp))
//                Row(
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Spacer(modifier = Modifier.size(40.dp))
//                    Text("¿Completada?")
//                    Checkbox(
//                        checked = estaFinalizada,
//                        onCheckedChange = { tareaViewModel.cambiarEsFinalizada(it) },
//                        modifier = Modifier.align(Alignment.CenterVertically)
//                    )
//                    Text("S/N")
//                }
//
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(70.dp)
//                        .padding(10.dp)
//                ) {
//                    Spacer(modifier = Modifier.size(20.dp))
//                    Button(
//                        onClick = {
//
//                            onDismiss()
//                        }) {
//                        Text("Cancelar")
//                    }
//                    Spacer(modifier = Modifier.size(30.dp))
//                    Button(onClick = {
////                        val t = Tarea(descripcion,dificultad,dificultad,estimacionHoras,horasInvertidas,estaAsignada,estaFinalizada, idUser)
//                        tareaViewModel.crearTarea(
//                            descripcion,
//                            dificultad,
//                            estimacionHoras,
//                            horasInvertidas,
//                            estaAsignada,
//                            estaFinalizada
//                        )
////                        onTareaAdded(t)
//
//                    }) {
//                        Text("Añadir")
//                    }
//                }
//            }
//        }
//    }
//}






//@Composable
//fun IrListadoButton(onClickAction: (Boolean) -> Unit) {
//    Button(
//        onClick = {
//            onClickAction(true);
//        },
//        modifier = Modifier.fillMaxWidth(),
//        colors = ButtonDefaults.buttonColors(
//            contentColor = Color.White,
//            disabledContentColor = Color.White
//        )
//    ) {
//        Text(text = "Ir al listado")
//    }
//}



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


