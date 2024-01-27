package com.example.gestordetareas.EleccionAdministrador

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.gestordetareas.ElementosComunes.Rutas
import com.example.gestordetareas.ListaUsuarios.ListadoUsuariosViewModel
import com.example.gestordetareas.ListadoTareas.ListadoTareasViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun botonesSeleccion(listadoUsuariosViewModel: ListadoUsuariosViewModel,listadoTareasViewModel: ListadoTareasViewModel ,navController: NavController){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Card(
            onClick = { listadoUsuariosViewModel.getUsers()
                navController.navigate(Rutas.listadoUsuarios) },
            modifier = Modifier
                .padding(10.dp)
                .width(400.dp)
                .height(400.dp)
        ) {
            Box(modifier = Modifier
                .background(Color(0xFF8BFFDE))
                .fillMaxWidth()
                .fillMaxHeight(),
                contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("U\n\nS\n\nU\n\nA\n\nR\n\n I\n\nO\n\nS",
                    Modifier.padding(10.dp),
                    fontSize = 40.sp)
            }
        }
        Card(
            onClick = { listadoTareasViewModel.getTodasLasTareas()
                navController.navigate(Rutas.listadoTareas) },
            modifier = Modifier
                .padding(10.dp)
                .width(400.dp)
                .height(400.dp)
        ) {
            Box(modifier = Modifier
                .background(Color(0xFFFFEB8B))
                .fillMaxWidth()
                .fillMaxHeight(),
                contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("T\n\nA\n\nR\n\nE\n\nA\n\nS",
                    Modifier.padding(10.dp),
                    fontSize = 40.sp)
            }
        }

    }
}

