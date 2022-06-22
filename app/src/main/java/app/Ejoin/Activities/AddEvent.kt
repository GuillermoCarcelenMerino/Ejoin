package app.Ejoin.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.view.View
import android.widget.*
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.Evento
import app.Ejoin.R
import com.bumptech.glide.Glide
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import utilities.Constants
import utilities.PreferencesManager
import java.io.ByteArrayOutputStream
import java.io.FileNotFoundException

class AddEvent : AppCompatActivity() {
    private lateinit var userPreferences : PreferencesManager
    private lateinit var imagen : ImageView
    private lateinit var imagenText : TextView
    private lateinit var nombre : EditText
    private lateinit var lugar : EditText
    private lateinit var dia : EditText
    private lateinit var mes : EditText
    private lateinit var precio : EditText
    private lateinit var maxUser : EditText
    private lateinit var latitud : EditText
    private lateinit var longitud : EditText
    private lateinit var descripcion : EditText
    private lateinit var categoria : Spinner
    private lateinit var imagenUrl : String
    private var imagenSelected = false
    private val db = Firebase.firestore
    private lateinit var uri : Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_event)
        userPreferences = PreferencesManager(this)
        initComponents()
        findViewById<Button>(R.id.AñadirEvento).setOnClickListener {
            x->addEvento()
        }
    }

    private fun initComponents() {
        this.imagen=findViewById(R.id.AddImage)
        imagen.setOnClickListener {
            this.selecionarImagen()
        }
        this.imagenText=findViewById(R.id.AddImageText)
        this.nombre=findViewById(R.id.addNombreEvento)
        this.lugar=findViewById(R.id.addLugar)
        this.dia=findViewById(R.id.addDia)
        this.mes=findViewById(R.id.addMes)
        this.precio=findViewById(R.id.addPrecio)
        this.maxUser=findViewById(R.id.addMaxUsuarios)
        this.latitud=findViewById(R.id.addLatitud)
        this.longitud=findViewById(R.id.addLongitud)
        this.descripcion=findViewById(R.id.addDescripcion)
        this.categoria=findViewById(R.id.addCategoria)
    }


    fun addEvento() {

         if(datosRellenos())
         {
             var storage = Firebase.storage

             val storageRef = storage.reference
             var file = uri
             val riversRef = storageRef.child("eventos/"+nombre.id+userPreferences.getString(Constants.EMAIL)!!)
             var uploadTask = riversRef.putFile(file!!)

             // Register observers to listen for when the download is done or if it fails
             uploadTask.addOnSuccessListener { taskSnapshot ->

                 taskSnapshot.storage.downloadUrl.addOnCompleteListener {
                     imagenUrl = it.result.toString()


                     var evento: Evento




                     var users: ArrayList<String> = arrayListOf()
                     var empresa = userPreferences.getString(Constants.EMAIL)!!
                     users.add(empresa)
                     evento = Evento(
                         nombre.text.toString(),
                         empresa,
                         dia.text.toString()+ "/"+mes.text.toString(),
                         categoria.selectedItem.toString(),
                         descripcion.text.toString(),
                         precio.text.toString().toDouble(),
                         lugar.text.toString(),
                         GeoPoint(latitud.text.toString().toDouble(),longitud.text.toString().toDouble()),
                         maxUser.text.toString().toInt(),
                         users,
                         imagenUrl
                     )
                     db.collection(Constants.EVENTOSDB).add(evento).addOnSuccessListener {
                         evento.setId(it.id)
                         it.update("id",it.id)
                         var chat = Chat()
                         chat.id=it.id
                         chat.users=users
                         chat.evento=true
                         chat.photo=imagenUrl
                         db.collection(Constants.CHATS).document(it.id).set(chat)
                         db.collection(Constants.USUARIOS).whereEqualTo(Constants.EMAIL,empresa).get()
                             .addOnSuccessListener {
                                 it.documents[0].reference.collection(Constants.CHATS).document(chat.id).set(chat)
                                 finish()
                             }

                     }.addOnFailureListener {
                         Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()

                     }
                 }


             }






         }


    }

    private fun datosRellenos(): Boolean {
        var valido = true
        if(nombre.text.isEmpty() || nombre.text.isBlank())
            valido=false
        if (lugar.text.isEmpty() || lugar.text.isBlank())
            valido=false
        if (dia.text.isEmpty() || dia.text.isBlank())
            valido=false
        if (mes.text.isEmpty() || mes.text.isBlank())
            valido=false
        if (precio.text.isEmpty() || precio.text.isBlank())
            valido=false
        if (maxUser.text.isEmpty() || maxUser.text.isBlank())
            valido=false
        if (latitud.text.isEmpty() || latitud.text.isBlank())
            valido=false
        if (longitud.text.isEmpty() || longitud.text.isBlank())
            valido=false
        if(!imagenSelected)
            valido = false
        return valido

    }

    private fun selecionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen de perfil"), 1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Procesar el resultado
        if (data != null) {
             uri = data.data!!
            try{
                var x = contentResolver.openInputStream(uri!!)
                var bitmap = BitmapFactory.decodeStream(x)
                imagen.setImageBitmap(bitmap)
                imagenSelected=true
                this.imagenText.visibility=View.GONE
            }catch (e : FileNotFoundException){
                Toast.makeText(this, "Error al seleccionar la foto", Toast.LENGTH_LONG).show()            }

        }


    }


}

