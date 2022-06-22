package app.Ejoin.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Adapter.RecyclerEventos
import app.Ejoin.DataClasses.Evento
import app.Ejoin.DataClasses.Usuarios
import app.Ejoin.R
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikhaellopez.circularimageview.CircularImageView
import utilities.Constants
import utilities.PreferencesManager

class Perfil : AppCompatActivity() {
    private lateinit var nombre : TextView
    private lateinit var correo : TextView
    private lateinit var foto : CircularImageView
    private lateinit var chatButton : Button

    private  var usuario = Usuarios()
    //Datos para el recycler de usuarios
    private val db = Firebase.firestore
    var eventos : MutableList<Evento> = mutableListOf()
    private lateinit var adapter : RecyclerEventos
    private lateinit var recyclerView : RecyclerView
    private lateinit var userPreferences : PreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        userPreferences= PreferencesManager(this)

        initActionBar()
        obtenerDatosUsuario()
        cargarEventos()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

                when(item.itemId){
                    R.id.logOut -> {
                        userPreferences = PreferencesManager(this)
                        userPreferences.putString(Constants.EMAIL,"")
                        userPreferences.putBoolean(Constants.LOGEADO,false)
                        userPreferences.putString(Constants.USERID,"")
                        userPreferences.putString(Constants.USERPHOTO,"" )
                        userPreferences.putString(Constants.NOMBREUSUARIO ,"")
                        userPreferences.putBoolean(Constants.ESEMPRESA,false)


                        startActivity(Intent(this,LoginActivity::class.java))
                        //TODO limpiar historial de activities para que no pueda voler
                    }


                }
                true



        return super.onOptionsItemSelected(item)
    }

    private fun obtenerDatosUsuario() {
        chatButton = findViewById(R.id.chatButton)
        var datosUsuario=intent.extras
        if(datosUsuario!=null) {
            usuario.setName(datosUsuario.getString(Constants.NOMBREUSUARIO)!!)
            usuario.setEmail(datosUsuario.getString(Constants.EMAIL)!!)
            usuario.setPhoto(datosUsuario.getString(Constants.USERPHOTO)!!)
            if (usuario.getEmail() != userPreferences.getString(Constants.EMAIL)) {

            chatButton.visibility = View.VISIBLE
            chatButton.setOnClickListener { x ->
                startActivity(Intent(this, ChatActivity::class.java).apply {
                    putExtra(Constants.EMAIL, usuario.getEmail())
                })
            }
        }
            else{
                this.setSupportActionBar(findViewById(R.id.toolbarPerfil))
                getSupportActionBar()?.setDisplayShowTitleEnabled(false);

            }

        }
        nombre = findViewById(R.id.nombrePerfil)
        correo = findViewById(R.id.correoPerfil)
        foto = findViewById(R.id.fotoPerfilAjustes)
        nombre.text=usuario.getName()
        correo.text=usuario.getEmail()
        Glide.with(this )
            .load(usuario.getPhoto())
            .into(foto)
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
        adapter = RecyclerEventos(this,eventos as ArrayList<Evento>)
        recyclerView.adapter = adapter

    }

    private fun initActionBar() {
        findViewById<BottomNavigationView>(R.id.bottom_navigationPerfil).selectedItemId=R.id.perfil
        findViewById<BottomNavigationView>(R.id.bottom_navigationPerfil).setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this,MainActivity::class.java))

                    true
                }
                R.id.chat -> {
                    startActivity(Intent(this,ChatActivity::class.java))
                    true
                }


                else -> false
            }
        }

    }
}