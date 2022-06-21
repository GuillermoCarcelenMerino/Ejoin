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
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
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

    private var imagenSelected = false
    private val db = Firebase.firestore
    private lateinit var bitmap : Bitmap


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


        var evento: Evento

        var previewBitmap = Bitmap.createScaledBitmap(bitmap,60,60,false)
        var byteArray = ByteArrayOutputStream()
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArray)
        var bytes = byteArray.toByteArray()
        var photo= Base64.encodeToString(bytes, Base64.DEFAULT)


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
            photo
        )
        db.collection(Constants.EVENTOSDB).add(evento).addOnSuccessListener {
            evento.setId(it.id)
            it.update("id",it.id)
            var chat = Chat()
            chat.id=it.id
            chat.users=users
            chat.evento=true
            chat.photo=photo
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
            val uri = data.data
            try{
                var x = contentResolver.openInputStream(uri!!)
                var bitmap = BitmapFactory.decodeStream(x)
                imagen.setImageBitmap(bitmap)
                this.bitmap=bitmap
                imagenSelected=true
                this.imagenText.visibility=View.GONE
            }catch (e : FileNotFoundException){
                Toast.makeText(this, "Error al seleccionar la foto", Toast.LENGTH_LONG).show()            }

        }


    }


}