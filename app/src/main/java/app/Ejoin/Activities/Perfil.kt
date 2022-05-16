package app.Ejoin.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Adapter.RecyclerEventos
import app.Ejoin.DataClasses.Evento
import app.Ejoin.DataClasses.Usuarios
import app.Ejoin.R
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikhaellopez.circularimageview.CircularImageView
import utilities.Constants

class Perfil : AppCompatActivity() {
    private lateinit var nombre : TextView
    private lateinit var correo : TextView
    private lateinit var foto : CircularImageView

    private  var usuario = Usuarios()
    //Datos para el recycler de usuarios
    private val db = Firebase.firestore
    var eventos : MutableList<Evento> = mutableListOf()
    private lateinit var adapter : RecyclerEventos
    private lateinit var recyclerView : RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)

        obtenerDatosUsuario()
        cargarEventos()
    }

    private fun obtenerDatosUsuario() {
        var datosUsuario=intent.extras
        if(datosUsuario!=null){
            usuario.setName(datosUsuario.getString(Constants.NOMBREUSUARIO)!!)
            usuario.setEmail(datosUsuario.getString(Constants.EMAIL)!!)
            usuario.setPhoto(datosUsuario.getString(Constants.USERPHOTO)!!)
        }
        nombre = findViewById(R.id.nombrePerfil)
        correo = findViewById(R.id.correoPerfil)
        foto = findViewById(R.id.fotoPerfilAjustes)
        nombre.text=usuario.getName()
        correo.text=usuario.getEmail()
        foto.setImageBitmap(usuario.photoBitmap())
    }



    private fun cargarEventos() {

        db.collection(Constants.EVENTOSDB).whereArrayContains(Constants.USUARIOS,usuario.getEmail()).get()
            .addOnSuccessListener {
                for (doc in it.documents){
                    var evento : Evento = Evento(doc.id,
                        doc.get("nombreEvento") as String,
                        doc.get("empresa") as String,
                        doc.get("fecha") as String,
                        doc.get("categoria") as String,
                        doc.get("detalles") as String,
                        doc.get("precio") as Number,
                        doc.get("lugar") as String,
                        doc.get("cordenada") as GeoPoint,
                        doc.get("maxUsuarios") as Number,
                        doc.get("usuarios") as ArrayList<String>,
                        doc.getString(Constants.USERPHOTO) as String
                    )

                    eventos.add(evento)
                }
                initRecycler()
            }
    }

    private fun initRecycler() {
        recyclerView = findViewById(R.id.eventosParticipados)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = RecyclerEventos(eventos as ArrayList<Evento>)
        recyclerView.adapter = adapter

    }
}