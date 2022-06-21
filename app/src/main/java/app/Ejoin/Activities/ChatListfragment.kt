package app.Ejoin.Activities

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Adapter.RecyclerChatLista
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.Usuarios
import app.Ejoin.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import utilities.Constants
import utilities.PreferencesManager

class ChatListfragment : Fragment() {
    private lateinit var usuarios: ArrayList<Usuarios>
    private lateinit var usuario: Usuarios
    private lateinit var userReference: DocumentReference
    private  var usuariosHablados = ArrayList<Usuarios>()

    private lateinit var adapter : RecyclerChatLista
    private lateinit var recyclerView : RecyclerView
    lateinit var fragmentChat: ChatFragment
    lateinit var FM: FragmentManager
    private val db = Firebase.firestore

    private lateinit var userPreferences : PreferencesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         FM= requireActivity().supportFragmentManager


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var V=inflater.inflate(R.layout.fragment_chat_listfragment, container, false)
        recyclerView = V.findViewById(R.id.recyclerChatList)
        initActionBar(V)
        userPreferences= PreferencesManager(requireActivity())

        for (hablado in usuarios){
            for(chat in usuario.getChats())
                if(chat.users.contains(hablado.getEmail()) && hablado.getEmail() != usuario.getEmail() && chat.users.size==2)
                    usuariosHablados.add(hablado)
        }

        initRecycler()
        return V
    }

    companion object {

        @JvmStatic fun newInstance(
            param1: ArrayList<Usuarios>,
            param2: Usuarios,
            userReference: DocumentReference
        ) =
            ChatListfragment().apply {
                this.usuarios=param1
                this.usuario = param2
                this.userReference = userReference
            }
    }

    private fun initRecycler() {

        recyclerView.layoutManager = LinearLayoutManager(activity)

        adapter = RecyclerChatLista(usuarios,usuario.getChats()  ,this)
//        if(usuariosHablados.size==0){
//            adapter = RecyclerChatLista(usuarios  ,this)
//
//        }
//        else adapter = RecyclerChatLista(usuariosHablados  ,this)
        recyclerView.adapter = adapter


            userReference.collection("chats")
            .addSnapshotListener { chats, error ->
                if(error==null)
                {
                    chats?.let {
                        this.usuario.setChats( it.toObjects(Chat::class.java) as ArrayList<Chat>)
                        usuariosHablados= arrayListOf()
                        for (hablado in usuarios){
                            for(chat in this.usuario.getChats())
                                if(chat.users.contains(hablado.getEmail()) && hablado.getEmail() != this.usuario.getEmail())
                                    usuariosHablados.add(hablado)
                        }
                        if(usuariosHablados.size==0){
                            adapter = RecyclerChatLista(usuarios,usuario.getChats()  ,this)

                        }
                        else adapter = RecyclerChatLista(usuarios,usuario.getChats()  ,this)
                        recyclerView.adapter = adapter
                    }
                }
            }




            }

    fun replaceFragment(chatGrupo: Chat) {
        fragmentChat = ChatFragment.newInstance(usuario,chatGrupo)
        val FT: FragmentTransaction = FM.beginTransaction()
        FT.replace(R.id.fragment, fragmentChat)
        FT.commit()
    }


    fun replaceFragment(usuarioIr: Usuarios) {
        fragmentChat = ChatFragment.newInstance(usuario,usuarioIr)
        val FT: FragmentTransaction = FM.beginTransaction()
        FT.replace(R.id.fragment, fragmentChat)
        FT.commit()
    }

    private fun initActionBar(V: View) {
        V.findViewById<BottomNavigationView>(R.id.bottom_navigationChat).selectedItemId=R.id.chat
        V.findViewById<BottomNavigationView>(R.id.bottom_navigationChat).setOnNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.home -> {
                    startActivity(Intent(requireActivity(),MainActivity::class.java))
                    true
                }
                R.id.perfil -> {
                    userPreferences = PreferencesManager(requireActivity())
                    startActivity(Intent(requireActivity(),Perfil::class.java).apply {

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
}