package com.example.gestordetareas.ListadoTareas

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.HowToReg
import androidx.compose.material.icons.filled.NewLabel
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult
import com.amplifyframework.auth.options.AuthSignOutOptions
import com.amplifyframework.core.Amplify
import com.amplifyframework.datastore.generated.model.Tarea
import com.example.gestordetareas.ListaUsuarios.MiToolBar
import com.example.gestordetareas.ListaUsuarios.OpcionMenu
import com.example.gestordetareas.ListaUsuarios.generarOpcionesMenuUsuarios
import com.example.gestordetareas.Rutas
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoTareas(navController: NavController, listadoTareasViewModel: ListadoTareasViewModel){
    // Variables necesarias para ModalNavigationDrawer y ModalDrawerSheet
    var snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val opcs = generarOpcionesMenuTareas()
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

                        if (selectedItemMiOpcion.opcion == "Volver") {
                            navController.navigate(Rutas.eleccionAdministrador)

                        }
                        if (selectedItemMiOpcion.opcion == "Todas") {


                        }
                        if (selectedItemMiOpcion.opcion == "Realizadas") {


                        }
                        if (selectedItemMiOpcion.opcion == "Sin asignar") {
                            act.finish()
                        }
                        if (selectedItemMiOpcion.opcion == "De usuario") {


                        }
                        if (selectedItemMiOpcion.opcion == "Mis datos") {


                        }
                        if (selectedItemMiOpcion.opcion == "Cerrar sesión") {
                            listadoTareasViewModel.cerrarSesión()
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

            TareasList(listadoTareasViewModel = listadoTareasViewModel, navController)

            }
        }
    }
    )
}



@Composable
fun TareasList(listadoTareasViewModel: ListadoTareasViewModel, navController: NavController) {
    listadoTareasViewModel.getTareas()
    val tareas = listadoTareasViewModel.tareas
    val context = LocalContext.current
    val showDialogBorrar: Boolean by listadoTareasViewModel.showDialogBorrar.observeAsState(false)

    LazyColumn {
        items(tareas) { tarea ->
            ItemTareaLista(t = tarea){ tar, tipo -> //Llamada a la función lamda clickable del card en ItemTarea
                if (tipo == 1) {//Click
                    Log.e("Fernando","Click pulsado")

                    listadoTareasViewModel.establecerTareaActual(tar)
                    navController.navigate(Rutas.perfilUsuarioVistaAdministrador)
                    Toast.makeText(context, "Usuario sel: $tar", Toast.LENGTH_SHORT).show()
                }
                if (tipo == 2){//Long click

                    listadoTareasViewModel.borrarTarea(tar)
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

    // Variable para mostrar o no el diálogo de confirmación
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 1.dp, bottom = 1.dp, start = 1.dp, end = 1.dp)
            .combinedClickable(
                onLongClick = {
                    Log.e("Fernando", "LongPress pulsado")
                    isLongClick = true
                    showDialog = true

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
        Text(text = t.descripcion, modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .padding(10.dp),
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
            )
    }
    // Diálogo de confirmación
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
                // Ocultar el diálogo cuando el usuario toca fuera de él
                showDialog = false
            },
            title = {
                Text(text = "Confirmación")
            },
            text = {
                Text(text = "¿Estás seguro de que deseas realizar esta acción?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        // Realizar la acción después de la confirmación
                        onItemSeleccionado(t, 2)

                        // Ocultar el diálogo
                        showDialog = false
                    }
                ) {
                    Text("Confirmar")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        // Cancelar la acción y cerrar el diálogo
                        showDialog = false
                    }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}







fun generarOpcionesMenuTareas() : ArrayList<OpcionMenu> {
    var titulos = listOf("Volver", "Todas", "Realizadas", "Sin realizar", "Sin asignar", "De usuario",  "Mis datos", "Cerrar sesión")
    var iconos =  listOf(Icons.Default.ArrowBack, Icons.Default.AllInclusive ,Icons.Default.Done, Icons.Default.Work, Icons.Default.HowToReg, Icons.Default.Search, Icons.Default.House, Icons.Default.ExitToApp)
    var opciones= ArrayList<OpcionMenu>()
    for(i in 0..titulos.size-1){
        opciones.add(OpcionMenu(titulos.get(i), iconos.get(i)))
    }
    return opciones
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
}



//
//@Composable
//fun IrPrincipalButton(onClickAction: (Boolean) -> Unit) {
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
//        Text(text = "Ir a la principal")
//    }
//}




//@Composable
//fun CerrarSesion(context: Context) {
//    val activity = LocalContext.current as Activity
//    val coroutineScope = rememberCoroutineScope()
//
//    Button(modifier = Modifier.fillMaxWidth(), onClick = {
//        val options = AuthSignOutOptions.builder()
//            .globalSignOut(true)
//            .build()
//
//        Amplify.Auth.signOut(options) { signOutResult ->
//            coroutineScope.launch {
//                if (signOutResult is AWSCognitoAuthSignOutResult.CompleteSignOut) {
//                    Log.i("Fernando", "Logout correcto")
//                    Toast.makeText(context, "Logout ok", Toast.LENGTH_SHORT).show()
//                } else if (signOutResult is AWSCognitoAuthSignOutResult.PartialSignOut) {
//                } else if (signOutResult is AWSCognitoAuthSignOutResult.FailedSignOut) {
//                    Log.e("Fernando", "Algo ha fallado en el logout")
//                    Toast.makeText(context, "Algo ha fallado en el logout", Toast.LENGTH_SHORT)
//                        .show()
//                }
//            }
//        }
//
//        activity.finish()
//    }) {
//        Text(text = "Cerrar aplicación")
//
//    }
//}







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