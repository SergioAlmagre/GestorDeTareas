package com.example.gestordetareas.ListaUsuarios

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AllInclusive
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.NewLabel
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.Usuario
import com.example.gestordetareas.ElementosComunes.InterVentana
import com.example.gestordetareas.R
import com.example.gestordetareas.ElementosComunes.Rutas
import com.example.gestordetareas.ListadoTareas.ListadoTareasViewModel
import com.example.gestordetareas.Usuario.UsuarioViewModel
import com.google.accompanist.coil.rememberCoilPainter
//import com.example.gestordetareas.ObjetosGemelos.Usuario
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoUsuarios(navController: NavController, listadoUsuariosViewModel: ListadoUsuariosViewModel, usuarioViewModel: UsuarioViewModel, listadoTareasViewModel: ListadoTareasViewModel){
    // Variables necesarias para ModalNavigationDrawer y ModalDrawerSheet
    var snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val opcs = generarOpcionesMenuUsuarios()
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
                        selectedItemMiOpcion = it

                        when (selectedItemMiOpcion.opcion) {
                            "Volver" -> {
                                navController.navigate(Rutas.eleccionAdministrador)
                            }
                            "Todos" -> {
                                listadoUsuariosViewModel.getUsers()
                            }
                            "Top (+) tareas realizadas" -> {
                                listadoUsuariosViewModel.getTop3MasTareas()
                            }
                            "Top (-) tareas realizadas" -> {
                                listadoUsuariosViewModel.getTop3MenosTareas()
                            }
                            "Crear usuario" -> {
                                usuarioViewModel.limpiarAtributosSueltos()
                                navController.navigate(Rutas.crearCuenta)
                            }
                            "Mis datos" -> {
                                usuarioViewModel.establecerUsuarioActual(InterVentana.usuarioActivo!!)
                                usuarioViewModel.asignarUsuarioActualToAtributosSueltos()
                                navController.navigate(Rutas.modUsuario)
                            }
                            "Cerrar sesión" -> {
                                listadoUsuariosViewModel.cerrarSesión()
                                act.finish()
                            }
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
                MiToolBar("                     Usuarios", drawerState, expanded, {
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
            RVUsuariosSticky(listadoUsuariosViewModel, navController, usuarioViewModel)
            }
        }
    }
    )
}




@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RVUsuariosSticky(listadoUsuariosViewModel: ListadoUsuariosViewModel, navController: NavController, usuarioViewModel: UsuarioViewModel) {
    val context = LocalContext.current
    var scope = rememberCoroutineScope()
    val esOrdenado: Boolean by listadoUsuariosViewModel.esOrdenado.observeAsState(false)

    if(esOrdenado){
        val usuariosAgrupados = listadoUsuariosViewModel.usuarios.groupBy { it.nombreCompleto.first().toUpperCase() }
        Log.i("Sergio_usuariosMostrar", usuariosAgrupados.toString())

        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            usuariosAgrupados.forEach { (inicial, listaUsuarios) ->
                stickyHeader {
                    Text(
                        text = inicial.toString(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = Color.White),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
                items(listaUsuarios) {
                    ItemUsuarioLista(u = it) { usu, tipo ->
                        //Llamada a la función lambda clickable del card en ItemUsuario.
                        if (tipo == 1) {//Click
                            Log.e("Fernando", "Click pulsado")
//                        Almacen.usu = usu
                            Log.i("Sergio", "Usuario seleccionado antes: $usu")
                            usuarioViewModel.establecerUsuarioActual(usu)
                            usuarioViewModel.asignarUsuarioActualToAtributosSueltos()
//                        usuarioViewModel.gemelearUsuarioActualVM()

                            navController.navigate(Rutas.modUsuario)
                            Log.i("Sergio", "Usuario seleccionado despues: $usu")
//                        Toast.makeText(context, "Usuario seleccionado: $usu", Toast.LENGTH_SHORT).show()
                        }
                        if (tipo == 2) {//Long click
                            listadoUsuariosViewModel.borrarUsuario(usu)
                            Log.e("Fernando", "Long click pulsado y borrado")
                        }
                        if (tipo == 3) {//Long click
                            Log.e("Fernando", "Double click pulsado")
                        }
                    }
                }
            }
        }

    }else{
        val listaUsuarios = listadoUsuariosViewModel.usuarios

        LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            items(listaUsuarios) { usuario ->
                // Código para mostrar cada usuario en la lista
                ItemUsuarioLista(u = usuario) { usu, tipo ->
                    //Llamada a la función lambda clickable del card en ItemUsuario.
                    if (tipo == 1) {//Click
                        Log.e("Fernando", "Click pulsado")
                        usuarioViewModel.establecerUsuarioActual(usu)
                        usuarioViewModel.asignarUsuarioActualToAtributosSueltos()
                        navController.navigate(Rutas.modUsuario)
                    }
                    if (tipo == 2) {//Long click
                        listadoUsuariosViewModel.borrarUsuario(usu)
                        Log.e("Fernando", "Long click pulsado y borrado")
                    }
                    if (tipo == 3) {//Long click
                        Log.e("Fernando", "Double click pulsado")
                    }
                }
            }
        }
//        listadoUsuariosViewModel.cambiarEsOrdenado(true)



    }






}








@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemUsuarioLista(u: Usuario, onItemSeleccionado: (Usuario, Int) -> Unit) {
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
    ) {
        Row(
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
                        onItemSeleccionado(u, 1)
                    }
                )//Para que funcione lo anterior se debe comentar lo de 'clickable'
//            .clickable {
//                    onItemSeleccionado(u,1) //Función lamda llamada desde RVUsuarios.
//            }
                .padding(top = 1.dp, bottom = 1.dp, start = 1.dp, end = 1.dp)
        ){
            Row(
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth()
            ) {
//                if (u.fotoPerfil == Rutas.imagenPerfilDefault) {
//                    Image(
//                        painter = painterResource(id = R.drawable.usuariorobot),
//                        contentDescription = "Avatar",
//                        modifier = Modifier
//                            .size(100.dp)
//                            .padding(8.dp)
//                            .clip(CircleShape)
//                    )
//                }
                Image(
                    painter = rememberCoilPainter(
                        request = u.fotoPerfil.toString(),
                        fadeIn = true
                    ),
                    contentDescription = "Avatar",
                    modifier = Modifier.size(100.dp)
                )
                // Centro el nombre en el Row
                Text(
                    text = u.nombreCompleto,
                    modifier = Modifier
                        .padding(6.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
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
                        onItemSeleccionado(u, 2)

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



fun generarOpcionesMenuUsuarios() : ArrayList<OpcionMenu> {
    var titulos = listOf("Volver", "Todos", "Top (+) tareas realizadas", "Top (-) tareas realizadas", "Crear usuario",  "Mis datos", "Cerrar sesión")
    var iconos =  listOf(Icons.Default.ArrowBack, Icons.Default.AllInclusive ,Icons.Default.ArrowUpward, Icons.Default.ArrowDownward , Icons.Default.NewLabel, Icons.Default.House, Icons.Default.ExitToApp)
    var opciones= ArrayList<OpcionMenu>()
    for(i in 0..titulos.size-1){
        opciones.add(OpcionMenu(titulos.get(i), iconos.get(i)))
    }
    return opciones
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun MiToolBar(
    title: String,
    drawerState : DrawerState,
    expanded : Boolean,
    onNavigationClick: (String) -> Unit,
    onMarkerClick: (String) -> Unit,
    onShareClick: (String) -> Unit,
    onSettingClick: (String) -> Unit
) {
    var scope = rememberCoroutineScope()
    val context = LocalContext.current
    var exp by remember { mutableStateOf(expanded) }

    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = Color(0xFFFFF2CA),
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black,
            navigationIconContentColor = Color.Black,
            scrolledContainerColor = Color.Black
        ),
        modifier = Modifier.background(color = Color.Blue),
        title = { Text(text = title) },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
                onNavigationClick("Menu")
            }) {
                Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menú desplegable")
            }
        },
        actions = {
//            IconButton(onClick = { onMarkerClick("M") }) {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
//                    contentDescription = "Leer después"
//                )
//            }
//            IconButton(onClick = { onShareClick("S") }) {
//                Icon(imageVector = Icons.Filled.Share, contentDescription = "Compartir")
//            }
//
//            IconButton(onClick = {
//                exp = true
//                onSettingClick("...")
//            }) {
//                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "Ver más")
//            }

//            MiDropDown(exp, {
//                exp = it
//            }){
//                Toast.makeText(context, "Seleccionado $it",Toast.LENGTH_SHORT).show()
//            }
        }
    )

}




data class OpcionMenu(val opcion: String, val icono: ImageVector)