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
}