package com.example.app_mvvm_aws.Imagenes

import android.content.Context
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.amplifyframework.core.Amplify
import com.example.gestordetareas.R
import kotlinx.coroutines.launch
import java.io.File










//@Composable
//fun PantallaImagenes(navController: NavHostController, pvm: PrincipalViewModel, lvm: ListadoViewModel, logvm: LoginViewModel, imagvm: ImagenesViewModel) {
//    Box(
//        Modifier
//            .fillMaxSize()
//            .padding(8.dp)
//    ) {
//        Column {
//            Text("Imágenes", fontSize = 20.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
//            IrPrincipalButton(){
//                navController.navigate(Rutas.Principal)
//            }
////            CerrarSesionList()
////            RVImagenes(imagvm)
//            Body(imagvm)
//        }
//    }
//}

//@Composable
//fun Body(imagvm: ImagenViewModel) {
////    RVImagenes(imagvm)
//    PickImageFromGallery(imagvm)
//}
//
//@Composable
//fun PickImageFromGallery(imagvm: ImagenViewModel) {
//
//    var imageUri by remember { mutableStateOf<Uri?>(null) }
//    var imageFile by remember { mutableStateOf<File?>(null) }
//    val context = LocalContext.current
//    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
//    val coroutineScope = rememberCoroutineScope()
//
//    val launcher =
//        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
//            imageUri = uri
//            imageFile = imageUri?.let { context.getRealPathFromURI(it) }?.let { File(it) }
//        }
//
//    Column(
//        modifier = Modifier.height(450.dp),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//
//        if (imageUri != null) {
//
//            imageUri?.let {
//                if (Build.VERSION.SDK_INT < 28) {
//                    bitmap.value = MediaStore.Images.Media.getBitmap(context.contentResolver, it)
//                } else {
//                    val source = ImageDecoder.createSource(context.contentResolver, it)
//                    bitmap.value = ImageDecoder.decodeBitmap(source)
//                }
//                Log.e("Fernando", "Bitmap: ${bitmap.value.toString()}")
//                Log.e("Fernando", "Uri: ${imageUri.toString()}",)
//                bitmap.value?.let { btm ->
//                    Image(
//                        bitmap = btm.asImageBitmap(),
//                        contentDescription = null,
//                        modifier = Modifier
//                            .size(400.dp)
//                            .padding(20.dp)
//                    )
//                }
//            }
//        }
//        else {
//            PintaImagenGenerica()
//        }
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically){
//            Button(onClick = { launcher.launch("image/*") }) {
//                Text(text = "Galería")
//            }
//            Spacer(modifier = Modifier.size(10.dp))
//            Button(onClick = {
//                //Con lo siguiente me quedo solo con el nombre del archivo, para evitar que guarde toda la ruta en el bucket.
//                    Amplify.Storage.uploadFile(imageFile.toString().substringAfterLast("/"), imageFile!!,
//                        {
//                            result ->
//                            coroutineScope.launch {
//                                    Log.i("MyAmplifyApp", "Successfully uploaded: ${imageFile.toString()}")
//                                    Toast.makeText(context, "Imagen subida correctamente", Toast.LENGTH_SHORT).show()
//                                    imagvm.addNombreRV(imageFile.toString().substringAfterLast("/"))
//                                }
//                         },
//                        {
//                            error ->
//                            coroutineScope.launch {
//                                Log.i("MyAmplifyApp", "Error al subir: ${error.toString()}")
//                                Toast.makeText(context, "Error al subir: ${error.toString()}", Toast.LENGTH_SHORT).show()
//                            }
//                        }
//                    )
//            }) {
//                Text(text = "Subir imagen")
//            }
//        }
//    }
//}
//
//@Composable
//fun PintaImagenGenerica() {
//    Image(
//        painter = painterResource(id = R.drawable.ic_launcher_foreground),
//        contentDescription = null,
//        modifier = Modifier
//            .size(400.dp)
//            .padding(20.dp)
//    )
//}
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
//
///**
// * Nos servirá para convertir la uri de la galería en una ruta tipo File y poder subirla al bucket.
// */
//fun Context.getRealPathFromURI(uri: Uri): String {
//    var cursor: Cursor? = null
//    try {
//        val proj = arrayOf(MediaStore.Images.Media.DATA)
//        cursor = contentResolver.query(uri, proj, null, null, null)
//        val columnIndex = cursor!!.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
//        cursor.moveToFirst()
//        return cursor.getString(columnIndex)
//    } finally {
//        cursor?.close()
//    }
//}

//@Composable
//fun RVImagenes(imagvm: ImagenesViewModel) {
//    //val imagenes by imagvm.imagenesS3.observeAsState(initial = arrayListOf())
//    val imagenes = imagvm.imagenesS3
//    val context = LocalContext.current
//    val showDialog: Boolean by imagvm.showDialog.observeAsState(false)
//    val imagenSeleccionada: Boolean by imagvm.imagenSeleccionada.observeAsState(false)
//    val ficheroImagenSeleccionado: String by imagvm.ficheroImagenSeleccionado.observeAsState("")
//
//    LazyColumn(modifier = Modifier.height(100.dp).border(width = 1.dp, color = Color.Blue, shape = RoundedCornerShape(1.dp)))
//    {
//        items(imagenes) { fichero ->
//            ItemFicheroLista(imagvm, item = fichero){fich, tipo -> //Llamada a la función lamda clickable del card en ItemUsuario.
//                if (tipo == 1) {//Click
//                    //Log.e("Fernando","Click pulsado")
//                    Toast.makeText(context, "Fichero seleccionado: $fich", Toast.LENGTH_SHORT).show()
//
//                }
//                if (tipo == 2){//Long click
//                    //imagvm.dialogOpen()
//                    //imagvm.ficheroBorrar(fich)
//                    //listadoViewModel.onItemRemove(fich)
//                }
//                if (tipo == 3){//Double click
//                    //Log.e("Fernando","Double click pulsado")
//                }
//            }
//        }
//    }
////    if (imagenSeleccionada){
////        var res = DescargarFichero(context, ficheroImagenSeleccionado)
////        MyImageComponent(res)
////    }
//    if (showDialog) {
//
//    }
//}

//@Composable
//fun DescargarFichero(applicationContext: Context, fich: String): StorageDownloadFileOperation<*> {
//    val coroutineScope = rememberCoroutineScope()
//    val file = File("${applicationContext.filesDir}/${fich}")
//    val res = Amplify.Storage.downloadFile(fich, file,
//        {
//            Log.i("MyAmplifyApp", "Successfully downloaded: ${it.file.name}")
//            it
//        },
//        {
//            Log.e("MyAmplifyApp",  "Download Failure", it)
//            null
//        }
//    )
//    return res
//}

//
//@Composable
//fun MyImageComponent(file: File) {
//    val painter = rememberImagePainter(
//        data = file,
//        builder = {
//            crossfade(true)
//            memoryCachePolicy(CachePolicy.DISABLED)
//            diskCachePolicy(CachePolicy.DISABLED)
//            networkCachePolicy(CachePolicy.DISABLED)
//        }
//    )
//
//    Image(
//        painter = painter,
//        contentDescription = null,
//        modifier = Modifier.fillMaxSize()
//    )
//}


//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun ItemFicheroLista(
//    imagvm: ImagenesViewModel,
//    item: String,
//    onItemSeleccionado: (String, Int) -> Unit
//){
//    var isLongClick by remember { mutableStateOf(false) }
//    var isClick by remember { mutableStateOf(false) }
//    var isDoubleClick by remember { mutableStateOf(false) }
//    var context = LocalContext.current
//
//
//    Card(border = BorderStroke(2.dp, Color.Blue),
//        modifier = Modifier
//            .fillMaxWidth()
//            .combinedClickable(
//                onDoubleClick = {
//                    Log.e("Fernando", "Doble click pulsado")
//                    isDoubleClick = true
//                    onItemSeleccionado(item, 3)
//                },
//                onLongClick = {
//                    Log.e("Fernando", "LongPress pulsado $item")
//                    isLongClick = true
//                    onItemSeleccionado(item, 2)
//                },
//                onClick = {
//                    Log.e("Fernando", "Click pulsado $item")
//                    isClick = true
//                    imagvm.setFicheroImagenSeleccionado(item)
//                    imagvm.setImagenSeleccionada(true)
//                    onItemSeleccionado(item, 1)
//                }
//            )
//            .padding(top = 1.dp, bottom = 1.dp, start = 1.dp, end = 1.dp)
//    )
//    {
//    Text(text = item, modifier = Modifier
//        .align(Alignment.CenterHorizontally)
//        .padding(2.dp)
//        .combinedClickable(
//            onDoubleClick = {
//                Log.e("Fernando", "Doble click pulsado")
//            },
//            onLongClick = {
//                Log.e("Fernando", "LongPress pulsado")
//                isLongClick = true
//            },
//            onClick = {
//                Log.e("Fernando", "Click pulsado")
//            }
//        ))
////        Button(onClick = {onItemSeleccionado(u,1) }) {
////            Text(text = "Seleccionar")
////        }
//    }
//}
