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
import app.Ejoin.Adapter.RecyclerEventos
import app.Ejoin.DataClasses.Evento
import app.Ejoin.DataClasses.Usuarios
import app.Ejoin.R
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import utilities.Constants

class ChatListfragment : Fragment() {
    private lateinit var usuarios: ArrayList<Usuarios>
    private lateinit var usuario: Usuarios
    private  var usuariosHablados = ArrayList<Usuarios>()

    private lateinit var adapter : RecyclerChatLista
    private lateinit var recyclerView : RecyclerView
    lateinit var fragmentChat: ChatFragment
    lateinit var FM: FragmentManager

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




        /*for (hablado in usuarios){
            if (usuario.getChats().contains(hablado.getEmail())){
                usuariosHablados.add(hablado)
            }
        }*/
        initRecycler()
        return V
    }

    companion object {

        @JvmStatic fun newInstance(param1: ArrayList<Usuarios>,param2: Usuarios) =
            ChatListfragment().apply {
                this.usuarios=param1
                this.usuario = param2
            }
    }

    private fun initRecycler() {

        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = RecyclerChatLista(usuariosHablados  ,this)
        recyclerView.adapter = adapter
    }

    fun replaceFragment(){
        fragmentChat = ChatFragment()
        val FT: FragmentTransaction = FM.beginTransaction()
        FT.replace(R.id.fragment, fragmentChat)
        FT.commit()
    }
}