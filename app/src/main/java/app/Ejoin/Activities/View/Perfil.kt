package app.Ejoin.Activities.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Activities.View.Chat.ChatActivity
import app.Ejoin.Activities.View.EventosMain.MainActivity
import app.Ejoin.Activities.ViewModel.PerfilViewModel
import app.Ejoin.Adapter.RecyclerEventos
import app.Ejoin.DataClasses.EventoData
import app.Ejoin.DataClasses.Usuario
import app.Ejoin.R
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikhaellopez.circularimageview.CircularImageView
import utilities.Constants
import utilities.PreferencesManager

class Perfil : AppCompatActivity() {
    private lateinit var nombre : TextView
    private lateinit var correo : TextView
    private lateinit var foto : CircularImageView

    private  var usuario = Usuario()
    //Datos para el recycler de usuarios
    var eventos : MutableList<EventoData> = mutableListOf()
    private lateinit var adapter : RecyclerEventos
    private lateinit var recyclerView : RecyclerView
    private lateinit var userPreferences : PreferencesManager


    private val viewModel : PerfilViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil)
        userPreferences= PreferencesManager(this)
        initActionBar()
        obtenerDatosUsuario()
        viewModel.eventosusuarioList.observe(this, Observer {
            eventos=it
            initRecycler()
        })
        viewModel.getEventosUsuario(this.usuario.email)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu,menu)
        if (usuario.email == userPreferences.getString(Constants.EMAIL))
            menu!!.findItem(R.id.chatUser).isVisible=false
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

                when(item.itemId){

                    R.id.chatUser ->{
                            startActivity(Intent(this, ChatActivity::class.java).apply {
                                putExtra(Constants.NOMBREUSUARIO, usuario.name)
                                putExtra(Constants.USERPHOTO, usuario.photo)
                                putExtra(Constants.EMAIL, usuario.email)
                                putExtra(Constants.ESEMPRESA, usuario.esEmpresa)
                            })

                    }
                    R.id.logOut -> {
                        userPreferences = PreferencesManager(this)
                        userPreferences.putString(Constants.EMAIL,"")
                        userPreferences.putBoolean(Constants.LOGEADO,false)
                        userPreferences.putString(Constants.USERID,"")
                        userPreferences.putString(Constants.USERPHOTO,"" )
                        userPreferences.putString(Constants.NOMBREUSUARIO ,"")
                        userPreferences.putBoolean(Constants.ESEMPRESA,false)


                        startActivity(Intent(this, LoginActivity::class.java))
                        //TODO limpiar historial de activities para que no pueda voler
                    }

                    R.id.Contraseña ->{
                        FirebaseAuth.getInstance().sendPasswordResetEmail(usuario.email)
                            .addOnSuccessListener {
                                Toast.makeText(this,"Se ha enviado un mensaje a su correo",Toast.LENGTH_LONG).show()
                            }
                    }

                }
                true

        return super.onOptionsItemSelected(item)
    }

    private fun obtenerDatosUsuario() {
        var datosUsuario=intent.extras
        if(datosUsuario!=null) {
            usuario.name=datosUsuario.getString(Constants.NOMBREUSUARIO)!!
            usuario.email=datosUsuario.getString(Constants.EMAIL)!!
            usuario.photo=datosUsuario.getString(Constants.USERPHOTO)!!
            usuario.esEmpresa=datosUsuario.getBoolean(Constants.ESEMPRESA)!!

                this.setSupportActionBar(findViewById(R.id.toolbarPerfil))
                getSupportActionBar()?.setDisplayShowTitleEnabled(false)



        }
        nombre = findViewById(R.id.nombrePerfil)
        correo = findViewById(R.id.correoPerfil)
        foto = findViewById(R.id.fotoPerfilAjustes)
        nombre.text=usuario.name
        correo.text=usuario.email
        Glide.with(this )
            .load(usuario.photo)
            .into(foto)
    }




    private fun initRecycler() {
        recyclerView = findViewById(R.id.eventosParticipados)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = RecyclerEventos(this, eventos as ArrayList<EventoData>,true)
        recyclerView.adapter = adapter

    }

    private fun initActionBar() {
        findViewById<BottomNavigationView>(R.id.bottom_navigationPerfil).selectedItemId=R.id.perfil
        findViewById<BottomNavigationView>(R.id.bottom_navigationPerfil).setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    startActivity(Intent(this, MainActivity::class.java))

                    true
                }
                R.id.chat -> {
                    if(usuario.email!=userPreferences.getString(Constants.EMAIL))
                    startActivity(Intent(this, ChatActivity::class.java))
                    true
                }


                else -> false
            }
        }

    }
}