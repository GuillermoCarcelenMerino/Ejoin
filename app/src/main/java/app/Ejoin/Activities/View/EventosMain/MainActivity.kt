package app.Ejoin.Activities.View.EventosMain

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Activities.View.Chat.ChatActivity
import app.Ejoin.Activities.FilterActivity
import app.Ejoin.Activities.View.AddEvent
import app.Ejoin.Activities.View.EventosMain.fragments.EventFragment
import app.Ejoin.Activities.View.EventosMain.fragments.GoogleMapFragment
import app.Ejoin.Activities.View.Perfil
import app.Ejoin.Activities.ViewModel.EventosViewModel
import app.Ejoin.Adapter.RecyclerFiltro
import app.Ejoin.DataClasses.EventoData
import app.Ejoin.DataClasses.Filtros
import app.Ejoin.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import utilities.Constants
import utilities.PreferencesManager

/*
* TODO revisar los navegadores y su proposito, revisar la Interfaz de muestra(modificarla),
*   añadir iconos de navegacion a chat y si es posible añador iconos usuarios. AÑADIR OPCIONES DE BUSQUEDA DE EVENTOS
*  */

class MainActivity : AppCompatActivity() {

    private var fragmentoUsadoMapa = false
    //preferences
    private lateinit var userPreferences : PreferencesManager

    //variables destinadas a obtener datos
    private val db = Firebase.firestore
    var eventos : MutableList<EventoData> = mutableListOf()

    //gestion de fragmentos
    val FM: FragmentManager = supportFragmentManager
    lateinit var  googleMapFragment : GoogleMapFragment
    lateinit var fragmentEvento: EventFragment

    //filtros
    private lateinit var adapter : RecyclerFiltro
    private lateinit var recyclerView : RecyclerView

    private var firstCharge = true
    var filtro = Filtros()
    //new Event
    private lateinit var addEvent : ExtendedFloatingActionButton
    /**
     *
     *
     *
     *
     *
     * */
    private val viewModel : EventosViewModel by viewModels()
    /**
     *
     *
     *
     * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        initActionBar()
        userPreferences = PreferencesManager(this)
           if(userPreferences.getBoolean(Constants.ESEMPRESA)) {
            addEvent = findViewById(R.id.addEvent)
            addEvent.visibility= View.VISIBLE
            addEvent.setOnClickListener {
                añadirEvento()
            }
        }

        cargarEventos()
        viewModel.eventos.observe(this, Observer {
            eventos=it
            if(firstCharge){
                initControlFragments()
                firstCharge=false
            }
            else {
                if(fragmentoUsadoMapa)
                    (FM.fragments[0] as GoogleMapFragment).filtrarMapa(eventos  as ArrayList<EventoData>)
                else {
                    (FM.fragments[0] as EventFragment).filtrarMapa(eventos  as ArrayList<EventoData>)
                }

            }

        })


    }
    
    /*
    * TODO Terminar navegador y añadir el swip down
    *  */
    private fun initActionBar() {
        findViewById<BottomNavigationView>(R.id.bottom_navigation).setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    true
                }
                R.id.chat -> {
                    startActivity(Intent(this, ChatActivity::class.java))
                    true
                }
                R.id.perfil -> {
                    userPreferences = PreferencesManager(this)
                    startActivity(Intent(this, Perfil::class.java).apply {

                        putExtra(Constants.EMAIL,userPreferences.getString(Constants.EMAIL))
                        putExtra(Constants.USERPHOTO,userPreferences.getString(Constants.USERPHOTO))
                        putExtra(Constants.NOMBREUSUARIO,userPreferences.getString(Constants.NOMBREUSUARIO))
                    })
                    true
                }
                else -> false
            }
        }

    }


    private fun cargarEventos() {
        viewModel.getEventos()
    }

    private fun initControlFragments() {
         googleMapFragment = GoogleMapFragment.newInstance(eventos as ArrayList<EventoData>)
        fragmentEvento= EventFragment.newInstance(eventos as ArrayList<EventoData>)
        val FT: FragmentTransaction = FM.beginTransaction()
        FT.add(R.id.fragment, fragmentEvento)
        FT.commit()

        findViewById<Button>(R.id.botonMapa).setOnClickListener{

            val FT: FragmentTransaction = FM.beginTransaction()
            FT.replace(R.id.fragment,googleMapFragment)
            FT.commit()
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            adapter = RecyclerFiltro( ArrayList<String>())
            recyclerView.adapter = adapter
            fragmentoUsadoMapa = true
        }
        findViewById<Button>(R.id.botonEvento).setOnClickListener{

            val FT: FragmentTransaction = FM.beginTransaction()
            FT.replace(R.id.fragment,fragmentEvento)
            FT.commit()

            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            adapter = RecyclerFiltro( ArrayList())
            recyclerView.adapter = adapter
            fragmentoUsadoMapa = false

        }


         recyclerView =findViewById(R.id.recyclerFiltros)

        findViewById<Button>(R.id.filtros).setOnClickListener{
          startActivityForResult(Intent(this, FilterActivity::class.java),1)


        }

        
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        adapter = RecyclerFiltro( ArrayList())
        recyclerView.adapter = adapter
        filtrar(data)

    }

    fun filtrar(data: Intent?) {
        var eventosFilt : MutableList<EventoData> = mutableListOf()
        filtro.deportesFilt= data?.extras?.get("deportes") as Boolean
        filtro.ocioFilt= data?.extras?.get("ocio") as Boolean
        filtro.musicaFilt= data?.extras?.get("musica") as Boolean
        filtro.estudiosFilt= data?.extras?.get("estudios") as Boolean
        filtro.disponibleFilt= data?.extras?.get("disponibles") as Boolean
        filtro.diaCambio= data?.extras?.get("diaCambio") as Boolean

        if(data?.extras?.get("precioCambio") as Boolean){
            filtro.precioFilt= true
            filtro.valorMaxPrecio=data?.extras?.get("precio") as Float
        }
        if(filtro.diaCambio){
            filtro.dia= data?.extras?.get("dia") as Int
            filtro.mes= data?.extras?.get("mes") as Int
        }

        crearRecyclerFiltros(filtro)

        eventos.forEach {x->
            var añadir = true

            if(filtro.deportesFilt && x.categoria!="DEPORTES")
                añadir = false

            if(filtro.ocioFilt && x.categoria!="OCIO")
                añadir = false
            if( filtro.estudiosFilt && x.categoria!="ESTUDIO")
                añadir = false
            if( filtro.musicaFilt && x.categoria!="MUSICA")
                añadir = false
            if(  filtro.disponibleFilt && x.usuarios.size>=x.maxUsuarios.toInt())
                añadir = false
            if(  filtro.precioFilt && x.precio.toFloat()>filtro.valorMaxPrecio)
                añadir = false
            if(filtro.diaCambio && x.fecha!= (filtro.dia.toString() + "/"+filtro.mes.toString()))
                añadir = false

            if (añadir)
                eventosFilt.add(x)
        }
        if(fragmentoUsadoMapa)
        {
            for(fragment in FM.fragments){
                if (fragment is GoogleMapFragment)
                    fragment.filtrarMapa(eventosFilt as ArrayList<EventoData>)
            }
        }
        else {
            for(fragment in FM.fragments){
                if (fragment is EventFragment)
                    fragment.filtrarMapa(eventosFilt as ArrayList<EventoData>)
            }
        }
    }

    private fun crearRecyclerFiltros(filtro: Filtros) {
        var filtros : MutableList<String> = mutableListOf()

        if(filtro.deportesFilt )
            filtros.add("deportes")
        if(filtro.ocioFilt )
            filtros.add("ocio")

        if(filtro.estudiosFilt)
            filtros.add("estudio")


        if( filtro.musicaFilt)
            filtros.add("musica")

        if(  filtro.disponibleFilt )
            filtros.add("disponibles")

        if(  filtro.precioFilt )
            filtros.add("precio")

        if(filtro.diaCambio )
            filtros.add("dia")

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        adapter = RecyclerFiltro(filtros as ArrayList<String> )
        recyclerView.adapter = adapter


    }

     private fun añadirEvento() {
         startActivity(Intent(this, AddEvent::class.java)
             .apply {
                 putExtra(Constants.EMAIL,userPreferences.getString(Constants.EMAIL))
             }
         )
     }

}