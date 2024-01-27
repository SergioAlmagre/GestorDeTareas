package com.example.gestordetareas.ElementosComunes


import com.example.gestordetareas.ObjetosGemelos.Tarea
import com.example.gestordetareas.ObjetosGemelos.Usuario

object InterVentana {
    var tarea:Tarea? = null
    var usuario:Usuario? = null // Usuario gemelo clase objeto normal

    var usuarioActivo:com.amplifyframework.datastore.generated.model.Usuario? = null // Usuario Amplify
    var tareaActiva:com.amplifyframework.datastore.generated.model.Tarea? = null // Tarea Amplify
}