package com.example.gestordetareas.ElementosComunes

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.amplifyframework.datastore.generated.model.Usuario
import com.example.gestordetareas.Rutas
import com.example.gestordetareas.Usuario.UsuarioViewModel


@Composable
fun BotonCancelar(navController: NavController, ruta:String) {
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

@Composable
fun BotonAceptar(navController: NavController, ruta:String) {
    Button(
        onClick = { navController.navigate(ruta) },
        modifier = Modifier
            .width(250.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Aceptar")
    }
}

@Composable
fun BotonGuardarDatos(navController: NavController, ruta:String, usuarioViewModel: UsuarioViewModel) {

    var usuarioActual = usuarioViewModel.obtenerUsuarioActual()
    usuarioViewModel.cambiarId(usuarioActual!!.id)
    usuarioViewModel.cambiarNombre(usuarioActual.nombreCompleto)

    Button(
        onClick = {
            var usu = Usuario.builder().id(usuarioViewModel.id.value).rol(usuarioViewModel.rol.value).nombreCompleto(usuarioViewModel.nombreCompleto.value).email(usuarioActual.email).tareasFinalizadas(usuarioActual.tareasFinalizadas).build()
            usuarioViewModel.guardarModificarUsuario(usu)
            Log.i("Sergio", "Boton guardar:" + usuarioViewModel.toString())
            Log.i("Sergio", "Boton guardar:" + usu)
            navController.navigate(ruta)
                  },
        modifier = Modifier
            .width(250.dp),
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
            disabledContentColor = Color.White
        )
    ) {
        Text(text = "Guardar datos")
    }
}

