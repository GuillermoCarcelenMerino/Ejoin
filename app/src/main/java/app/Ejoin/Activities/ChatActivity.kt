package app.Ejoin.Activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Adapter.RecyclerUsuarios
import app.Ejoin.DataClasses.Evento
import app.Ejoin.DataClasses.Usuarios
import app.Ejoin.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import utilities.Constants
import utilities.PreferencesManager

class ChatActivity : AppCompatActivity() {

    //gestion de fragmentos
    val FM: FragmentManager = supportFragmentManager
    lateinit var fragmentLista: ChatListfragment
    lateinit var fragmentChat: ChatFragment

    private lateinit var userPreferences : PreferencesManager
    private lateinit var user : Usuarios

    //Datos para el recycler de usuarios
    private val db = Firebase.firestore
    var usuarios : MutableList<Usuarios> = mutableListOf()
    private  lateinit var  usuario : Usuarios


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        userPreferences = PreferencesManager(this)

            usuario= Usuarios(
                userPreferences.getString(Constants.EMAIL)!!,
                userPreferences.getString(Constants.USERID)!!,
                userPreferences.getString(Constants.USERPHOTO)!!,
                userPreferences.getString(Constants.NOMBREUSUARIO)!!,
                userPreferences.getBoolean(Constants.ESEMPRESA)
            )
        cargarRecycler()



    }


    private fun cargarRecycler() {
        db.collection(Constants.USERBD).get()
            .addOnSuccessListener {

                it.documents.forEach {

                    var usuario  = Usuarios(
                        it.getString(Constants.EMAIL)!!,
                        it.getString(Constants.USERID)!! ,
                        it.getString(Constants.USERPHOTO)!!,
                        it.getString(Constants.NOMBREUSUARIO)!! ,
                        it.getBoolean(Constants.ESEMPRESA)!!)
                    if(it.getString(Constants.EMAIL)!=this.usuario.getEmail()) {
                        usuarios.add(usuario)
                    }
                    else {
                        this.usuario.setChats(it.get("chats") as ArrayList<String>)
                    }


                }

               initControlFragments()

            }
            .addOnFailureListener {
            }
    }

    private fun initControlFragments() {
        //fragmentChat = ChatFragment().newInstance(eventos as ArrayList<Evento>)
        fragmentLista= ChatListfragment.newInstance(usuarios as ArrayList<Usuarios>,usuario)
        val FT: FragmentTransaction = FM.beginTransaction()
        FT.add(R.id.fragment, fragmentLista)
        FT.commit()

        /*9
            val FT: FragmentTransaction = FM.beginTransaction()
            FT.replace(R.id.fragment,fragmentMapa)
            FT.commit()

        }
        findViewById<Button>(R.id.botonEvento).setOnClickListener{

            val FT: FragmentTransaction = FM.beginTransaction()
            FT.replace(R.id.fragment,fragmentEvento)
            FT.commit()

        }*/
    }

}