package app.Ejoin.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Adapter.RecyclerUsuarios
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.Evento
import app.Ejoin.DataClasses.Message
import app.Ejoin.DataClasses.Usuarios
import app.Ejoin.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import utilities.Constants
import utilities.PreferencesManager

class DatosEventos : AppCompatActivity() {
    private lateinit var evento : Evento
    private lateinit var nombreEvento  : TextView
    private lateinit var categoriaEvento : TextView
    private lateinit var empresa  : TextView
    private lateinit var fecha  : TextView
    private lateinit var lugar : TextView
    private lateinit var numParticipantes  : TextView
    private lateinit var detalles  : TextView
    private lateinit var precio  : TextView
    private lateinit var inscribirse  : Button
    private lateinit var photo  : ImageView

    private lateinit var userPreferences : PreferencesManager


    //Datos para el recycler de usuarios
    private val db = Firebase.firestore
    var usuarios : MutableList<Usuarios> = mutableListOf()
    private lateinit var adapter : RecyclerUsuarios
    private lateinit var recyclerView : RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_eventos)
        obetenerDatosIntent()
        cargarDatos()
        cargarRecycler()

    }

    private fun cargarRecycler() {
        db.collection(Constants.USERBD).whereIn(Constants.EMAIL, evento.getusuarios())
            .get()
            .addOnSuccessListener {
               // for (doc in it.documents){
                it.documents.forEach {

                    var usuario  = Usuarios(
                        it.getString(Constants.EMAIL)!!,
                        it.getString(Constants.USERID)!! ,
                        it.getString(Constants.USERPHOTO)!!,
                        it.getString(Constants.NOMBREUSUARIO)!! ,
                        it.getBoolean(Constants.ESEMPRESA)!!)

                    usuarios.add(usuario)
                }
                initRecycler()
            }
            .addOnFailureListener {
            }
    }

    private fun initRecycler() {
        recyclerView = findViewById(R.id.participantes)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = RecyclerUsuarios(usuarios as ArrayList<Usuarios>)
        recyclerView.adapter = adapter

        //adapter.notifyDataSetChanged()
    }
    private fun cargarDatos() {
        nombreEvento = findViewById(R.id.eventoNombre)
        categoriaEvento = findViewById(R.id.eventoCategoria)
        empresa = findViewById(R.id.eventoEmpresa)
        fecha = findViewById(R.id.eventoFecha)
        lugar = findViewById(R.id.eventoLugar)
        numParticipantes = findViewById(R.id.numParticipantes)
        detalles = findViewById(R.id.eventoDetalles)
        precio = findViewById(R.id.eventoPrecio)
        inscribirse=findViewById(R.id.inscribirse)
        photo= findViewById(R.id. fotoEvento)
        inscribirse()
        nombreEvento.text = evento.getNombreEvento()
        categoriaEvento.text = evento.getCategoria()
        empresa.text = evento.getEmpresa()
        fecha.text = evento.getFecha()
        lugar.text = evento.getLugar()
        numParticipantes.text = evento.getusuarios().size.toString() +"/" + evento.getMaxUsuarios()
        detalles.text= evento.getDetalles()
        precio.text = evento.getPrecio().toString()
        photo.setImageBitmap(evento.photoBitmap())

    }

    private fun obetenerDatosIntent() {
        var bundle = intent.extras
        if (bundle != null) {
            evento = Evento(
                bundle.get(Constants.EVENTOID) as String,
                bundle.get(Constants.NOMBREVENTO) as String,
                bundle.get(Constants.EMAIL) as String,
                bundle.get(Constants.FECHA) as String,
                bundle.get(Constants.CATEGORIA) as String,
                bundle.get(Constants.DETALLES) as String,
                bundle.get(Constants.PRECIO) as Number,
                bundle.get(Constants.LUGAR) as String,

                GeoPoint(
                    bundle.get(Constants.ALTITUD) as Double,
                    bundle.get(Constants.LONGITUD) as Double
                ),
                bundle.get(Constants.MAXUSUARIOS) as Number,
                bundle.get(Constants.USUARIOS) as ArrayList<String>,
                bundle.getString(Constants.USERPHOTO) as String

                )
        }
    }

    private fun inscribirse() {
        userPreferences = PreferencesManager(this)

        var usuarioEmail = userPreferences.getString(Constants.EMAIL)
        var maxUsuarios = evento.getMaxUsuarios()
        var inscritos = evento.getusuarios()
        if(inscritos.size<maxUsuarios.toInt() && !inscritos.contains(usuarioEmail)) {
            if (usuarioEmail != null) {

                inscribirse.setOnClickListener {

                    startActivityForResult(Intent(this, PasarelaPagoJava::class.java).apply {
                        putExtra(Constants.PRECIO, evento.getPrecio())
                    }, 1)


                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== RESULT_OK){
            var usuarioEmail = userPreferences.getString(Constants.EMAIL)
            var maxUsuarios = evento.getMaxUsuarios()
            var inscritos = evento.getusuarios()

                    var nuevosusuarios : MutableList<String> = inscritos.toMutableList()
                    nuevosusuarios.add(usuarioEmail!!)
                    evento.setUsuarios(nuevosusuarios as ArrayList<String>)
                    db.collection(Constants.EVENTOSDB)
                        .document(evento.getId()!!)
                        .update(Constants.USUARIOS,nuevosusuarios)
                        .addOnSuccessListener {
                            numParticipantes.text=nuevosusuarios.size.toString() + "/"+maxUsuarios
                            var usuarioNuevo = Usuarios(usuarioEmail,
                                userPreferences.getString(Constants.USERID)!!,
                                userPreferences.getString(Constants.USERPHOTO)!!,
                                userPreferences.getString(Constants.NOMBREUSUARIO)!!,
                                userPreferences.getBoolean(Constants.ESEMPRESA))

                            usuarios.add(usuarioNuevo)
                            adapter= RecyclerUsuarios(usuarios as ArrayList<Usuarios>)
                            recyclerView.adapter = adapter
                        }
                        .addOnFailureListener {  }

            db.collection(Constants.CHATS).document(evento.getId()!!).get().addOnSuccessListener {
                var usuario = userPreferences.getString(Constants.EMAIL)
               var chat = it.toObject(Chat::class.java)
                var userChat = ArrayList(chat!!.users)
                userChat.add(usuario)
                chat.users=userChat
                for (user in chat.users)
                {
                    if(user!=usuario)
                    //update chats from userOrigin
                    db.collection(Constants.USERBD).whereEqualTo(Constants.EMAIL,user).get().addOnSuccessListener {
                        it.documents.get(0)
                            .reference
                            .collection("chats")
                            .document(chat.id)
                            .update("users",chat.users)
                            .addOnSuccessListener {
                                print(it)
                            }
                            .addOnFailureListener {
                                Toast.makeText(this,"No pudo inscribirse al chat",Toast.LENGTH_SHORT).show()
                            }
                    }
                    else{
                        db.collection(Constants.USERBD).whereEqualTo(Constants.EMAIL,user).get().addOnSuccessListener {
                            it.documents.get(0)
                                .reference
                                .collection("chats")
                                .document(chat.id)
                                .set(chat)
                                .addOnSuccessListener {
                                    print(it)
                                }
                                .addOnFailureListener {
                                    Toast.makeText(this,"No pudo inscribirse al chat",Toast.LENGTH_SHORT).show()
                                }
                        }
                    }
                }

                it.reference.update("users",userChat).addOnSuccessListener {
                    print("hola")
                }
            }
            }
    }


}