package app.Ejoin.Activities.View.Chat.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Activities.Model.Chats
import app.Ejoin.Activities.View.EventosMain.MainActivity
import app.Ejoin.Activities.View.Perfil
import app.Ejoin.Activities.ViewModel.ChatListaMVVM
import app.Ejoin.Activities.ViewModel.PerfilViewModel
import app.Ejoin.Adapter.RecyclerChatLista
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.Usuario
import app.Ejoin.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import utilities.Constants
import utilities.PreferencesManager

class ChatListfragment : Fragment() {
    private lateinit var usuario: String
    private lateinit var chats: ArrayList<Chat>
    private lateinit var usuariosFilt: ArrayList<Usuario>
    private lateinit var userReference: DocumentReference
    private lateinit var adapter : RecyclerChatLista
    private lateinit var recyclerView : RecyclerView
    lateinit var fragmentChat: ChatFragment
    lateinit var FM: FragmentManager
    private lateinit var userPreferences : PreferencesManager
    private val viewModel : ChatListaMVVM by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         FM= requireActivity().supportFragmentManager
        userPreferences= PreferencesManager(requireActivity())
        usuario=userPreferences.getString(Constants.EMAIL)!!
        setObservers()
    }
    private fun setObservers() {
        viewModel.userReference.observe(this, Observer {
            userReference=it
            initRecycler()
        })

        viewModel.chats.observe(this, Observer {
            chats= it as ArrayList<Chat>
            var users = arrayListOf<String>()
           for (chat in it )
           {
               if(!chat.evento){
                   for (user in chat.users)
                   {
                       if(user!=usuario)
                           users.add(user)
                   }
               }
           }
            viewModel.getUsuariosFiltrados(users)
        })

        viewModel.usuariosFiltrados.observe(this, Observer {
            usuariosFilt= it as ArrayList<Usuario>
            adapter = RecyclerChatLista(it as ArrayList<Usuario>,chats  ,this)
            recyclerView.adapter = adapter

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var V=inflater.inflate(R.layout.fragment_chat_listfragment, container, false)
        V.findViewById<EditText>(R.id.buscarUser).doOnTextChanged { text, start, before, count ->
            filtUsers(text)
        }
        recyclerView = V.findViewById(R.id.recyclerChatList)
        initActionBar(V)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        viewModel.getUserReference(usuario)
        return V
    }

    private fun filtUsers(text: CharSequence?) {

        if(text!=""){
            var chatsFilt = chats.filter { x-> x.nombre.contains(text.toString()) }
            adapter = RecyclerChatLista(usuariosFilt,chatsFilt as ArrayList<Chat>  ,this)
            recyclerView.adapter = adapter
        }
        else{
            adapter = RecyclerChatLista(usuariosFilt  ,chats  ,this)
            recyclerView.adapter = adapter
        }


    }

    private fun initRecycler() {

        viewModel.getChatsUsuario(userReference)

            }

    fun replaceFragment(chatGrupo: Chat) {
        fragmentChat = ChatFragment.newInstance(usuario,chatGrupo,chats)
        val FT: FragmentTransaction = FM.beginTransaction()
        FT.replace(R.id.fragment, fragmentChat)
        FT.commit()
    }

    fun replaceFragment(usuarioIr: Usuario) {
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
                    startActivity(Intent(requireActivity(), MainActivity::class.java))
                    true
                }
                R.id.perfil -> {
                    userPreferences = PreferencesManager(requireActivity())
                    startActivity(Intent(requireActivity(), Perfil::class.java).apply {

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