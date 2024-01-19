package com.example.gestordetareas.EleccionAdministrador

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun botonesSeleccion(){
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Button(
            onClick = { /* TODO */ },
            modifier = Modifier
                .fillMaxWidth(1f)
                .fillMaxHeight(0.5f)
                .clip(RoundedCornerShape(0.dp))
        ) {
            Text("U\nS\nU\nA\nR\nI\nO\ns",
                fontSize = 50.sp)

        }
        Card(
            onClick = { /* Acción al hacer clic */ },
            modifier = Modifier
                .padding(10.dp)
                .width(400.dp)
                .height(1000.dp)
        ) {
            Box(modifier = Modifier
                .background(Color(0xFFFFEB8B))
                .fillMaxWidth()
                .fillMaxHeight(),
                contentAlignment = androidx.compose.ui.Alignment.Center) {
                Text("U\n\nS\n\nU\n  \nA\n  \nR\n  \n I\n  \nO\n  \ns",
                    Modifier.padding(10.dp),
                    fontSize = 40.sp)
            }
        }
    }
}

