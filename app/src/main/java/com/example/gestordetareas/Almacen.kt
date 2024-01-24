package com.example.gestordetareas

import com.amplifyframework.datastore.generated.model.Usuario


object Almacen {

    var usu = Usuario.builder().id("").rol(Rutas.rolProgramador).nombreCompleto("").email("").tareasFinalizadas(0).build()

}