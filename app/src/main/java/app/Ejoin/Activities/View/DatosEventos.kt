package app.Ejoin.Activities.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Activities.PasarelaPagoJava
import app.Ejoin.Activities.ViewModel.EventosDatoViewModel
import app.Ejoin.Adapter.RecyclerUsuarios
import app.Ejoin.DataClasses.EventoData
import app.Ejoin.DataClasses.Usuario
import app.Ejoin.R
import com.bumptech.glide.Glide
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import utilities.Constants
import utilities.PreferencesManager

class DatosEventos : AppCompatActivity() {
    private lateinit var evento : EventoData
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

    var usuarios : MutableList<Usuario> = mutableListOf()
    private lateinit var adapter : RecyclerUsuarios
    private lateinit var recyclerView : RecyclerView

    private val viewModel : EventosDatoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_datos_eventos)
        userPreferences = PreferencesManager(this)
        obetenerDatosIntent()
        cargarDatos()
        cargarRecycler()
        viewModel.eventoUpdate.observe(this, Observer {
            evento=it
            numParticipantes.text=evento.usuarios.size.toString() + "/"+evento.maxUsuarios
            var usuarioNuevo = Usuario(userPreferences.getString(Constants.EMAIL)!!,
                userPreferences.getString(Constants.USERPHOTO)!!,
                userPreferences.getString(Constants.NOMBREUSUARIO)!!,
                userPreferences.getBoolean(Constants.ESEMPRESA))

            usuarios.add(usuarioNuevo)
            adapter= RecyclerUsuarios(this,usuarios as ArrayList<Usuario>)
            recyclerView.adapter = adapter
        })
        viewModel.usuarios.observe(this, Observer {
            x->this.usuarios=x
            initRecycler()
        })
    }

    private fun cargarRecycler() {
        if(evento.usuarios.isNotEmpty())
        viewModel.getUsuariosEvento(evento.usuarios)
    }

    private fun initRecycler() {
        recyclerView = findViewById(R.id.participantes)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = RecyclerUsuarios(this,usuarios as ArrayList<Usuario>)
        recyclerView.adapter = adapter
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
        nombreEvento.text = evento.nombreEvento.toUpperCase()
        categoriaEvento.text = evento.categoria
        empresa.text = evento.empresa
        fecha.text = evento.fecha
        lugar.text = evento.lugar
        numParticipantes.text = evento.usuarios.size.toString() +"/" + evento.maxUsuarios
        detalles.text= evento.detalles
        precio.text = evento.precio.toString()
        Glide.with(this )
            .load(evento.photo)
            .into(photo)
        if(evento.usuarios.contains(userPreferences.getString(Constants.EMAIL))|| evento.usuarios.size>=evento.maxUsuarios)
            this.inscribirse.visibility= View.GONE

    }

    private fun obetenerDatosIntent() {
        var bundle = intent.extras
        if (bundle != null) {
            evento = EventoData(
                bundle.get(Constants.EVENTOID) as String,
                bundle.get(Constants.NOMBREVENTO) as String,
                bundle.get(Constants.EMAIL) as String,
                bundle.get(Constants.FECHA) as String,
                bundle.get(Constants.CATEGORIA) as String,
                bundle.get(Constants.DETALLES) as String,
                bundle.get(Constants.PRECIO) as Double,
                bundle.get(Constants.LUGAR) as String,
                GeoPoint(
                    bundle.get(Constants.ALTITUD) as Double,
                    bundle.get(Constants.LONGITUD) as Double
                ),
                bundle.get(Constants.MAXUSUARIOS) as Int,
                bundle.get(Constants.USUARIOS) as ArrayList<String>,
                bundle.getString(Constants.USERPHOTO) as String
                )
        }
    }

    private fun inscribirse() {
        inscribirse.setOnClickListener {
            startActivityForResult(Intent(this, PasarelaPagoJava::class.java).apply {
                putExtra(Constants.PRECIO, evento.precio)
            }, 1)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(resultCode== RESULT_OK){

            viewModel.registra(userPreferences,evento)

            }
    }


}