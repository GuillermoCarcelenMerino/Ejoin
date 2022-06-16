package app.Ejoin.Activities

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Adapter.RecyclerEventos
import app.Ejoin.Adapter.RecyclerFiltro
import app.Ejoin.DataClasses.Evento
import app.Ejoin.DataClasses.Filtros
import app.Ejoin.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import utilities.Constants
import utilities.PreferencesManager

/*
* TODO revisar los navegadores y su proposito, revisar la Interfaz de muestra(modificarla),
*   añadir iconos de navegacion a chat y si es posible añador iconos usuarios. AÑADIR OPCIONES DE BUSQUEDA DE EVENTOS
*  */

class MainActivity : AppCompatActivity() {
    //navegacion
    private lateinit var navView: NavigationView
    private lateinit var auth: FirebaseAuth
    //preferences
    private lateinit var userPreferences : PreferencesManager

    //variables destinadas a obtener datos
    private val db = Firebase.firestore
    var eventos : MutableList<Evento> = mutableListOf()

    //gestion de fragmentos
    val FM: FragmentManager = supportFragmentManager
    lateinit var  googleMapFragment : GoogleMapFragment
    lateinit var fragmentEvento: EventFragment

    //filtros
    private lateinit var adapter : RecyclerFiltro
    private lateinit var recyclerView : RecyclerView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        initNavView()
        initActionBar()
        cargarEventos()



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
                    startActivity(Intent(this,ChatActivity::class.java))
                    true
                }
                R.id.perfil -> {
                    userPreferences = PreferencesManager(this)
                    startActivity(Intent(this,Perfil::class.java).apply {

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


    /**
     * TODO Desarrolla la funcionalidad del navVar
     * */
    private fun initNavView() {
        navView = findViewById(R.id.nav_View)
        navView.setNavigationItemSelectedListener {
            when(it.itemId){

                R.id.nav_home -> Toast.makeText(this,"clicked 1", Toast.LENGTH_LONG).show()
                R.id.nav_logOut -> {
                    auth.signOut()
                    userPreferences = PreferencesManager(this)
                    userPreferences.putString(Constants.EMAIL,"")
                    userPreferences.putBoolean(Constants.LOGEADO,false)
                    userPreferences.putString(Constants.USERID,"")
                    userPreferences.putString(Constants.USERPHOTO,"")
                    startActivity(Intent(this,LoginActivity::class.java))
                    //TODO limpiar historial de activities para que no pueda voler
                }
                R.id.nav_sync -> Toast.makeText(this,"clicked 1", Toast.LENGTH_LONG).show()

            }
            true
        }
    }

    private fun cargarEventos() {

        eventos.clear()

        db.collection(Constants.EVENTOSDB).get()
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

                initControlFragments()



            }
    }

    private fun initControlFragments() {
         googleMapFragment = GoogleMapFragment.newInstance(eventos as ArrayList<Evento>)
        fragmentEvento= EventFragment.newInstance(eventos as ArrayList<Evento>)
        val FT: FragmentTransaction = FM.beginTransaction()
        FT.add(R.id.fragment, googleMapFragment)
        FT.commit()

        findViewById<Button>(R.id.botonMapa).setOnClickListener{

            val FT: FragmentTransaction = FM.beginTransaction()
            FT.replace(R.id.fragment,googleMapFragment)
            FT.commit()
            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            adapter = RecyclerFiltro( ArrayList<String>())
            recyclerView.adapter = adapter
        }
        findViewById<Button>(R.id.botonEvento).setOnClickListener{

            val FT: FragmentTransaction = FM.beginTransaction()
            FT.replace(R.id.fragment,fragmentEvento)
            FT.commit()

            recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
            adapter = RecyclerFiltro( ArrayList<String>())
            recyclerView.adapter = adapter

        }


         recyclerView =findViewById(R.id.recyclerFiltros)

        findViewById<Button>(R.id.filtros).setOnClickListener{
          startActivityForResult(Intent(this,FilterActivity::class.java),1)


        }

        
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var eventosFilt : MutableList<Evento> = mutableListOf()
        var filtro = Filtros()
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

                if(filtro.deportesFilt && x.getCategoria()!="DEPORTES")
                    añadir = false

                if(filtro.ocioFilt && x.getCategoria()!="OCIO")
                 añadir = false
            if( filtro.estudiosFilt && x.getCategoria()!="ESTUDIO")
                    añadir = false
            if( filtro.musicaFilt && x.getCategoria()!="MUSICA")
                añadir = false
            if(  filtro.disponibleFilt && x.getusuarios().size>=x.getMaxUsuarios().toInt())
             añadir = false
            if(  filtro.precioFilt && x.getPrecio().toFloat()>filtro.valorMaxPrecio)
                añadir = false
            if(filtro.diaCambio && x.getFecha()!= (filtro.dia.toString() + "/"+filtro.mes.toString()))
                añadir = false

            if (añadir)
                eventosFilt.add(x)
        }
        if(FM.fragments[0] is GoogleMapFragment)
            (FM.fragments[0] as GoogleMapFragment).filtrarMapa(eventosFilt as ArrayList<Evento>)
        else {
            (FM.fragments[0] as EventFragment).filtrarMapa(eventosFilt as ArrayList<Evento>)
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

    /* private fun añadirEvento() {
         var evento : Evento
         var users : ArrayList<String> = arrayListOf("pepe@gmail.com","paco@gmail.com")
         evento = Evento("Partido de futbol","guillermo@gmail.com","12/2",Categoria.DEPORTES.toString(),"partido 7 v 7",2.2,"Madrid",
             GeoPoint(32.0,20.0),3,users
         )
         db.collection(Constants.EVENTOSDB).add(evento).addOnSuccessListener {
             Toast.makeText(this,"añadido correctanebte",Toast.LENGTH_LONG).show()
         }.addOnFailureListener{
                 Toast.makeText(this,"Error",Toast.LENGTH_LONG).show()

         }

     }*/

}