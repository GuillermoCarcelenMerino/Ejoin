package app.Ejoin.Activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.Usuarios
import app.Ejoin.R
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import utilities.Constants
import utilities.PreferencesManager

class ChatActivity : AppCompatActivity() {

    //gestion de fragmentos
    val FM: FragmentManager = supportFragmentManager
    lateinit var fragmentLista: ChatListfragment
    private lateinit var userPreferences : PreferencesManager
    private lateinit var user : Usuarios

    //Datos para el recycler de usuarios
    private val db = Firebase.firestore
    var usuarios : MutableList<Usuarios> = mutableListOf()
    private  lateinit var  usuario : Usuarios
    private  lateinit var userReference : DocumentReference

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
                        userReference=it.reference
                    }


                }

                userReference.collection("chats").get().addOnSuccessListener {
                    var chats = it.toObjects(Chat::class.java)
                    usuario.setChats(chats as ArrayList<Chat>)
                    initControlFragments()
                }



            }
            .addOnFailureListener {
            }
    }

    private fun initControlFragments() {
        fragmentLista= ChatListfragment.newInstance(usuarios as ArrayList<Usuarios>,usuario, userReference)
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

    override fun onBackPressed() {


        if(FM.fragments[0] is ChatFragment)
        {
            val FT: FragmentTransaction = FM.beginTransaction()
            FT.replace(R.id.fragment, fragmentLista)
            FT.commit()
        }
        else super.onBackPressed()

    }

}