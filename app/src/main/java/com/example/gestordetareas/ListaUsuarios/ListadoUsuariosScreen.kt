package com.example.gestordetareas.ListaUsuarios

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.Usuario
import com.example.gestordetareas.R
//import com.example.gestordetareas.Usuario.Usuario
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListadoUsuarios(navController: NavController){
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


            RVUsuariosSticky(listadoUsuariosViewModel = ListadoUsuariosViewModel())


            }
        }
    }
    )
}





@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RVUsuariosSticky(listadoUsuariosViewModel: ListadoUsuariosViewModel) {
    val context = LocalContext.current

    listadoUsuariosViewModel.getUsers()

    val usuariosAgrupados = listadoUsuariosViewModel.usuarios.groupBy { it.nombreCompleto }
    Log.i("Sergio_usuariosMostrar", usuariosAgrupados.toString())

    LazyColumn(verticalArrangement = Arrangement.spacedBy(4.dp)) {

        usuariosAgrupados.forEach { (nombreCompleto, listaUsuarios) ->
            stickyHeader {
                Text(
                    text = nombreCompleto,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.LightGray),
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
                        Toast.makeText(context, "Usuario seleccionado: $usu", Toast.LENGTH_SHORT).show()
                    }
                    if (tipo == 2) {//Long click
                        Log.e("Fernando", "Long click pulsado")
                    }
                    if (tipo == 3) {//Long click
                        Log.e("Fernando", "Double click pulsado")
                    }
                }
            }
        }
    }
}






/**
 * Para mostrar la RV en columnas o en Grid.
 * En este ejemplo, el Modifier.pointerInput se utiliza para detectar gestos táctiles y el evento
 * detectTransformGestures para calcular la distancia del gesto. Si la distancia del gesto supera
 * un umbral, se considera como un "long click". El comportamiento real del "long click" se puede
 * personalizar según tus necesidades. En este caso, simplemente imprime un mensaje en el registro.
 */
@Composable
fun ItemUsuario(u : Usuario, onItemSeleccionado:(Usuario)->Unit){
    var isLongClick by remember { mutableStateOf(false) }
    var context = LocalContext.current

    Card(border = BorderStroke(2.dp, Color.Blue),
        modifier = Modifier
            //.fillMaxWidth()
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        Log.e("Fernando", "Press pulsado")
                        onItemSeleccionado(u)
                    },
                    onTap = {
                        Log.e("Fernando", "Tap pulsado")
                    },
                    onLongPress = {
                        Log.e("Fernando", "LongPress pulsado")
                        isLongClick = true
                    },
                    onDoubleTap = {
                        Log.e("Fernando", "DoubleTap pulsado")
                    }
                )
            } //Para que funcione pointerInput, debe estar comentado 'clickable'
//            .clickable {
//                onItemSeleccionado(u) //Función lamda llamada desde RVUsuarios.
//            }
            .padding(top = 1.dp, bottom = 1.dp, start = 1.dp, end = 1.dp)
    )
    {
        Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Avatar",
            modifier = Modifier
                .size(50.dp)
                .padding(8.dp)
        )
        Text(text = u.nombreCompleto, modifier = Modifier
            .align(CenterHorizontally)
            .padding(2.dp))

        Button(onClick = {onItemSeleccionado(u) }) {
            Text(text = "Seleccionar")
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ItemUsuarioLista(u: Usuario, onItemSeleccionado:(Usuario, Int)->Unit){
    var isLongClick by remember { mutableStateOf(false) }
    var isClick by remember { mutableStateOf(false) }
    var isDoubleClick by remember { mutableStateOf(false) }
    var context = LocalContext.current

    Card(border = BorderStroke(2.dp, Color.Blue),
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onDoubleClick = {
                    Log.e("Sergio", "Doble click pulsado")
                    isDoubleClick = true
                    onItemSeleccionado(u, 3)
                },
                onLongClick = {
                    Log.e("Sergio", "LongPress pulsado")
                    isLongClick = true
                    onItemSeleccionado(u, 2)
                },
                onClick = {
                    Log.e("Sergio", "Click pulsado")
                    isClick = true
                    onItemSeleccionado(u, 1)
                }
            )//Para que funcione lo anterior se debe comentar lo de 'clickable'
//            .clickable {
//                    onItemSeleccionado(u,1) //Función lamda llamada desde RVUsuarios.
//            }
            .padding(top = 1.dp, bottom = 1.dp, start = 1.dp, end = 1.dp)
    )
    {
        if(u.fotoPerfil.isEmpty()){
            Image(painter = painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Avatar",
                modifier = Modifier
                    .size(50.dp)
                    .padding(8.dp)
            )
        }
        Text(text = u.nombreCompleto, modifier = Modifier
            .align(CenterHorizontally)
            .padding(2.dp)
            .combinedClickable(
                onDoubleClick = {
                    Log.e("Sergio", "Doble click pulsado")
                },
                onLongClick = {
                    Log.e("Sergio", "LongPress pulsado")
                    isLongClick = true
                },
                onClick = {
                    Log.e("Sergio", "Click pulsado")
                }
            ))
        Button(onClick = {onItemSeleccionado(u,1) }) {
            Text(text = "Seleccionar")
        }
    }
}


fun generarOpcionesMenu() : ArrayList<OpcionMenu> {
    var titulos = listOf("Volver", "Todas", "Realizadas", "Sin realizar", "Sin asignar", "De usuario")
    var iconos =  listOf(Icons.Default.Home, Icons.Default.Search, Icons.Default.ArrowForward, Icons.Default.ExitToApp, Icons.Default.ExitToApp, Icons.Default.ExitToApp)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MiDropDown(isExpanded: Boolean, setExpanded: (Boolean) -> Unit, setSelected:(String)->Unit) {
//    var selectedText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var usuarios = listOf<String>("Álvaro", "José", "Lorenzo", "Ramón", "Sergio", "María")

    Column(modifier = Modifier.padding(10.dp)) {
        DropdownMenu(expanded = isExpanded, onDismissRequest = {
            setExpanded(false)
        })
        {
            usuarios.forEach {
                DropdownMenuItem(text = { Text(text = it) }, onClick = {
                    setExpanded (false)
                    setSelected(it)
                })
            }
        }
    }
}



data class OpcionMenu(val opcion: String, val icono: ImageVector)