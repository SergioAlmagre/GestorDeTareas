package com.example.gestordetareas.Listado

import com.example.gestordetareas.Tarea.Tarea
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.a2_practicamvvm.Rutas
import com.example.gestordetareas.Principal.CerrarSesion
import com.example.gestordetareas.Principal.PrincipalViewModel



@Composable
fun Listado(
    navController: NavHostController,
    principalViewModel: PrincipalViewModel,
    listadoViewModel: ListadoViewModel
) {
    var context = LocalContext.current
    Box(
        Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        Column {
            Text("Listado", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
            Body( navController, principalViewModel, listadoViewModel)
            CerrarSesion(context)
        }

    }
}

@Composable
fun Body(navController: NavHostController, principalViewModel: PrincipalViewModel, listadoViewModel: ListadoViewModel) {
    Column {
        //Text(listadoViewModel.usuarios.toString())
        TareasList(listadoViewModel = listadoViewModel)
        IrPrincipalButton(){
            navController.navigate(Rutas.Principal)
        }
    }
}

@Composable
fun TareasList(listadoViewModel: ListadoViewModel) {
    val tareas = listadoViewModel.tareas
    val context = LocalContext.current
    val showDialogBorrar: Boolean by listadoViewModel.showDialogBorrar.observeAsState(false)

    LazyColumn {
        items(tareas) { tarea ->
            ItemTareaLista(t = tarea){ tar, tipo -> //Llamada a la función lamda clickable del card en ItemTarea
                if (tipo == 1) {//Click
                    Log.e("Fernando","Click pulsado")
                    Toast.makeText(context, "Usuario sel: $tar", Toast.LENGTH_SHORT).show()
                }
                if (tipo == 2){//Long click
                    listadoViewModel.dialogOpen()
                    listadoViewModel.tareaBorrar(tar)
                    Log.e("Fernando","Long click pulsado")
                }
                if (tipo == 3){//Double click
                    Log.e("Fernando","Double click pulsado")
                }
            }
        }
    }
    if (showDialogBorrar) {
        MyAlertDialog(
            onConfirm = {
                listadoViewModel.dialogClose()
                Log.e("Fernando",listadoViewModel.tarBorrar.toString())
                listadoViewModel.onItemRemove(listadoViewModel.tarBorrar)
            },
            onDismiss = {
                listadoViewModel.dialogClose()
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemTareaLista(t : Tarea, onItemSeleccionado:(Tarea, Int)->Unit){
    var isLongClick by remember { mutableStateOf(false) }
    var isClick by remember { mutableStateOf(false) }
    var isDoubleClick by remember { mutableStateOf(false) }
    var context = LocalContext.current

    Card(border = BorderStroke(2.dp, Color.Blue),
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onDoubleClick = {
                    Log.e("Fernando","Doble click pulsado")
                    isDoubleClick = true
                    onItemSeleccionado(t,3)
                },
                onLongClick = {
                    Log.e("Fernando","LongPress pulsado")
                    isLongClick = true
                    onItemSeleccionado(t,2)
                },
                onClick = {
                    Log.e("Fernando","Click pulsado")
                    isClick = true
                    onItemSeleccionado(t,1)
                }
            )//Para que funcione lo anterior se debe comentar lo de 'clickable'
//            .clickable {
//                    onItemSeleccionado(u,1) //Función lamda llamada desde RVUsuarios.
//            }
            .padding(top = 1.dp, bottom = 1.dp, start = 1.dp, end = 1.dp)
    )
    {
        Text("Descripcion", style = TextStyle(fontWeight = FontWeight.Bold))
        Text(text = t.descripcion, modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(2.dp)
            .combinedClickable(
                onDoubleClick = {
                    Log.e("Fernando","Doble click pulsado")
                },
                onLongClick = {
                    Log.e("Fernando","LongPress pulsado")
                    isLongClick = true
                },
                onClick = {
                    Log.e("Fernando","Click pulsado")
                }
            ))

        Text("Estimación horas", style = TextStyle(fontWeight = FontWeight.Bold))
        Text(
            text = t.estimacionHoras.toString(),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(2.dp),
            fontSize = 12.sp
        )

        Text("Dificultad", style = TextStyle(fontWeight = FontWeight.Bold))
        Text(
            text = t.dificultad,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(2.dp),
            fontSize = 12.sp
        )
        Checkbox(
            checked = t.estaFinalizada,  onCheckedChange = {  }
        )
//        Button(onClick = {onItemSeleccionado(u,1) }) {
//            Text(text = "Seleccionar")
//        }
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


@Composable
fun MyAlertDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Warning, contentDescription = null)
                Text(text = "Confirmar borrado")
            }
        },
        text = {
            Text("¿Estás seguro de que deseas borrar este elemento?")
        },
        confirmButton = {
            Button(
                onClick = {
                    onConfirm()
                }
            ) {
                Icon(imageVector = Icons.Default.Done, contentDescription = null)
                Text("Sí")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDismiss()
                }
            ) {
                Icon(imageVector = Icons.Default.Cancel, contentDescription = null)
                Text("No")
            }
        }
    )
}