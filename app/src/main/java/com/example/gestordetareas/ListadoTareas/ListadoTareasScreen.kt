package com.example.gestordetareas.ListadoTareas

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Tarea
import com.example.gestordetareas.Rutas
import com.example.gestordetareas.ListaUsuarios.ListadoUsuariosViewModel
import com.example.gestordetareas.ListaUsuarios.MiToolBar
import com.example.gestordetareas.ListaUsuarios.RVUsuariosSticky
import com.example.gestordetareas.ListaUsuarios.generarOpcionesMenu
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoTareas(navController: NavController, listadoTareasViewModel: ListadoTareasViewModel){
    // Variables necesarias para ModalNavigationDrawer y ModalDrawerSheet
    var snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val opcs = generarOpcionesMenu()
    var selectedItemMiOpcion by remember { mutableStateOf(opcs[0]) }
    var scope = rememberCoroutineScope()
    var expanded by remember { mutableStateOf(false) }

    val act = LocalContext.current as Activity
    ModalNavigationDrawer(drawerState = drawerState, drawerContent = {
        ModalDrawerSheet {
            Spacer(Modifier.height(12.dp))

            opcs.forEach {
                NavigationDrawerItem(
                    icon = { Icon(it.icono, contentDescription = it.opcion) },
                    label = { Text(it.opcion) },
                    selected = it.opcion == selectedItemMiOpcion.opcion,
                    onClick = {
                        scope.launch {
                            drawerState.close()
                        }
                        selectedItemMiOpcion = it //Aquí obtenemos el seleccionado.
//                            OpcionElegida.eleccion = it.opcion

                        if (selectedItemMiOpcion.opcion == "Manual") {


                        }
                        if (selectedItemMiOpcion.opcion == "Statistics") {


                        }
                        if (selectedItemMiOpcion.opcion == "Summary") {


                        }
                        if (selectedItemMiOpcion.opcion == "Exit") {
                            act.finish()
                        }

                    },
                    modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                )
            }
        }
    }, content = {

        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                MiToolBar("                    Tareas", drawerState, expanded, {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                },
                    {
                        //val job = GlobalScope.launch(Dispatchers.Main) {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Pulsado marcador $it",
                                actionLabel = "Close",
                                duration = SnackbarDuration.Indefinite
                            )
                            //Si en tiempo ponemos short se oculta en un breve tiempo.
                        }
                        //Si en tiempo ponemos short se oculta en un breve tiempo.
                        Toast.makeText(context, "Pulsado marker $it", Toast.LENGTH_SHORT).show()
                    },
                    {

                        Toast.makeText(context, "Pulsado share $it", Toast.LENGTH_SHORT).show()
                    }
                ) {
                    Toast.makeText(context, "Pulsado menú puntos $it", Toast.LENGTH_SHORT)
                        .show()
                }
            },
        )
        { padding ->

            Column(modifier = Modifier.padding(padding)) {


                TareasList(listadoTareasViewModel = listadoTareasViewModel)


            }
        }
    }
    )
}






//
//@Composable
//fun Listado(
//    navController: NavHostController,
//    principalViewModel: PrincipalViewModel,
//    listadoTareasViewModel: ListadoTareasViewModel
//) {
//    var context = LocalContext.current
//    Box(
//        Modifier
//            .fillMaxSize()
//            .padding(8.dp)
//    ) {
//        Column {
//            Text("Listado", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
//            Body( navController, principalViewModel, listadoTareasViewModel)
////            CerrarSesion(context)
//        }
//
//    }
//}
//
//@Composable
//fun Body(navController: NavHostController, principalViewModel: PrincipalViewModel, listadoTareasViewModel: ListadoTareasViewModel) {
//    Column {
//        //Text(listadoViewModel.usuarios.toString())
//        TareasList(listadoTareasViewModel = listadoTareasViewModel)
//        IrPrincipalButton(){
//            navController.navigate(Rutas.Principal)
//        }
//    }
//}

@Composable
fun TareasList(listadoTareasViewModel: ListadoTareasViewModel) {
    listadoTareasViewModel.getTareas()
    val tareas = listadoTareasViewModel.tareas
    val context = LocalContext.current
    val showDialogBorrar: Boolean by listadoTareasViewModel.showDialogBorrar.observeAsState(false)

    LazyColumn {
        items(tareas) { tarea ->
            ItemTareaLista(t = tarea){ tar, tipo -> //Llamada a la función lamda clickable del card en ItemTarea
                if (tipo == 1) {//Click
                    Log.e("Fernando","Click pulsado")
                    Toast.makeText(context, "Usuario sel: $tar", Toast.LENGTH_SHORT).show()
                }
                if (tipo == 2){//Long click
                    listadoTareasViewModel.dialogOpen()
                    listadoTareasViewModel.tareaBorrar(tar)
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
                listadoTareasViewModel.dialogClose()
                Log.e("Sergio",listadoTareasViewModel.tarBorrar.toString())
                listadoTareasViewModel.onItemRemove(listadoTareasViewModel.tarBorrar)
            },
            onDismiss = {
                listadoTareasViewModel.dialogClose()
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
                    Log.e("Fernando", "Doble click pulsado")
                    isDoubleClick = true
                    onItemSeleccionado(t, 3)
                },
                onLongClick = {
                    Log.e("Fernando", "LongPress pulsado")
                    isLongClick = true
                    onItemSeleccionado(t, 2)
                },
                onClick = {
                    Log.e("Fernando", "Click pulsado")
                    isClick = true
                    onItemSeleccionado(t, 1)
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
                    Log.e("Fernando", "Doble click pulsado")
                },
                onLongClick = {
                    Log.e("Fernando", "LongPress pulsado")
                    isLongClick = true
                },
                onClick = {
                    Log.e("Fernando", "Click pulsado")
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

@Composable
fun CerrarSesion(context: Context) {
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
                    Toast.makeText(context, "Logout ok", Toast.LENGTH_SHORT).show()
                } else if (signOutResult is AWSCognitoAuthSignOutResult.PartialSignOut) {
                } else if (signOutResult is AWSCognitoAuthSignOutResult.FailedSignOut) {
                    Log.e("Fernando", "Algo ha fallado en el logout")
                    Toast.makeText(context, "Algo ha fallado en el logout", Toast.LENGTH_SHORT)
                        .show()
                }
            }
        }

        activity.finish()
    }) {
        Text(text = "Cerrar aplicación")

    }
}