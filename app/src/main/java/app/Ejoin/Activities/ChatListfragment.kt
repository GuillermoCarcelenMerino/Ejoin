package app.Ejoin.Activities

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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import utilities.Constants

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
        if(usuariosHablados.size==0){
            adapter = RecyclerChatLista(usuarios  ,this)

        }
        else adapter = RecyclerChatLista(usuariosHablados  ,this)
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
                            adapter = RecyclerChatLista(usuarios  ,this)

                        }
                        else adapter = RecyclerChatLista(usuariosHablados  ,this)
                        recyclerView.adapter = adapter
                    }
                }
            }




            }




    fun replaceFragment(usuarioIr: Usuarios) {
        fragmentChat = ChatFragment.newInstance(usuario,usuarioIr)
        val FT: FragmentTransaction = FM.beginTransaction()
        FT.replace(R.id.fragment, fragmentChat)
        FT.commit()
    }
}