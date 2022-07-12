package app.Ejoin.Activities.View.Chat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import app.Ejoin.Activities.View.Chat.fragments.ChatFragment
import app.Ejoin.Activities.View.Chat.fragments.ChatListfragment
import app.Ejoin.DataClasses.Usuario
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
    private lateinit var user : Usuario

    //Datos para el recycler de usuarios
    private val db = Firebase.firestore
    var usuarios : MutableList<Usuario> = mutableListOf()
    private  lateinit var  usuario : Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        userPreferences = PreferencesManager(this)
        usuario= Usuario()
        usuario.email=userPreferences.getString(Constants.EMAIL)!!
        usuario.photo= userPreferences.getString(Constants.USERPHOTO)!!
        usuario.name =userPreferences.getString(Constants.NOMBREUSUARIO)!!
        usuario.esEmpresa=userPreferences.getBoolean(Constants.ESEMPRESA)
        initControlFragments()
    }

    private fun initControlFragments() {
        var datosUsuario=intent.extras
        if(datosUsuario!=null)
        {

            var usuarioIr  = Usuario()
            usuarioIr.name= datosUsuario.getString(Constants.NOMBREUSUARIO)!!
            usuarioIr.email=datosUsuario.getString(Constants.EMAIL)!!
            usuarioIr.photo=datosUsuario.getString(Constants.USERPHOTO)!!
            usuarioIr.esEmpresa=datosUsuario.getBoolean(Constants.ESEMPRESA)!!

            var fragmentChat = ChatFragment.newInstance(usuario.email, usuarioIr)
            val FT: FragmentTransaction = FM.beginTransaction()
            FT.replace(R.id.fragment, fragmentChat)
            FT.commit()

        }
        else {

            fragmentLista = ChatListfragment()
            val FT: FragmentTransaction = FM.beginTransaction()
            FT.add(R.id.fragment, fragmentLista)
            FT.commit()

        }
    }



























/*
    private fun cargarRecycler() {

        db.collection(Constants.USERBD).get()
            .addOnSuccessListener {

                it.documents.forEach {

                    var usuario  = Usuario(
                        it.getString(Constants.EMAIL)!!,
                        it.getString(Constants.USERPHOTO)!!,
                        it.getString(Constants.NOMBREUSUARIO)!! ,
                        it.getBoolean(Constants.ESEMPRESA)!!)
                    if(it.getString(Constants.EMAIL)!=this.usuario.email) {
                        usuarios.add(usuario)
                    }
                    else {
                        userReference=it.reference
                    }


                }

                userReference.collection("chats").get().addOnSuccessListener {
                    var chats = it.toObjects(Chat::class.java)
                    usuario.chats=chats as ArrayList<Chat>
                    initControlFragments()
                }



            }
            .addOnFailureListener {
            }
    }

    private fun initControlFragments() {
        var datosUsuario=intent.extras
        if(datosUsuario!=null)
        {
            var usuarioIr : Usuario = Usuario()
            var emailUsuario = datosUsuario.get(Constants.EMAIL)
            for(usuario in usuarios){
                if(usuario.email==emailUsuario)
                    usuarioIr = usuario
            }

            var fragmentChat = ChatFragment.newInstance(usuario,usuarioIr)
            val FT: FragmentTransaction = FM.beginTransaction()
            FT.replace(R.id.fragment, fragmentChat)
            FT.commit()

        }

        else {
            fragmentLista = ChatListfragment.newInstance(
                usuarios as ArrayList<Usuario>,
                usuario,
                userReference
            )
            val FT: FragmentTransaction = FM.beginTransaction()
            FT.add(R.id.fragment, fragmentLista)
            FT.commit()

        }
    }

    override fun onBackPressed() {

        var datosUsuario = intent.extras
        if (datosUsuario != null) {
            finish()
        } else {
            if (FM.fragments[1] is ChatFragment) {
                val FT: FragmentTransaction = FM.beginTransaction()
                FT.replace(R.id.fragment, fragmentLista)
                FT.commit()
            } else super.onBackPressed()

        }
    }

*/

}