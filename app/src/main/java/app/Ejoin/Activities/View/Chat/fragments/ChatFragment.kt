package app.Ejoin.Activities.View.Chat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Activities.ViewModel.ChatFragmentMVVM
import app.Ejoin.Adapter.RecyclerChat
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.Message
import app.Ejoin.DataClasses.Usuario
import app.Ejoin.R
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikhaellopez.circularimageview.CircularImageView
import utilities.Constants
import kotlin.collections.ArrayList


class ChatFragment : Fragment() {

    private lateinit var usuarioOrigen: String
    private lateinit var chats : ArrayList<Chat>
    private lateinit var usuarioFin: Usuario
    var chatFin: Chat? = null

    private lateinit var editMessage : EditText
    private lateinit var sendMessage: Button
    private lateinit var nombreUsuarioFin: TextView
    private lateinit var photoUsuarioFin : CircularImageView
    private var newChat : Boolean = true

    private lateinit var adapter : RecyclerChat
    private lateinit var recyclerView : RecyclerView


    private  var mensajes : MutableList<Message> = mutableListOf()

    private lateinit var chat : Chat
    /**
     *
     *
     *
     *
     *
     * */
    private val viewModel : ChatFragmentMVVM by viewModels()
    /**
     *
     *
     *
     * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setObservers()
        chat = Chat()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var V=inflater.inflate(R.layout.fragment_chat, container, false)
        this.editMessage = V.findViewById(R.id.textMessage)
        this.sendMessage=V.findViewById(R.id.sendMessage)
        this.recyclerView = V.findViewById(R.id.recyclerChat)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        this.nombreUsuarioFin = V.findViewById(R.id.nombreUsuarioFin)
        this.photoUsuarioFin=V.findViewById(R.id.imagenUsuarioFin)

        //chat grupo
        if(chatFin!=null)
        {
            newChat=false
            this.nombreUsuarioFin.text= chatFin!!.nombre
            cargarMensages(chatFin!!)


            Glide.with(requireActivity() )
                .load(chatFin!!.photo)
                .into( photoUsuarioFin)
            sendMessage.setOnClickListener { x->sendMessageChat() }
        }
        //chat individual

        else
        {
            chat.users= listOf( usuarioOrigen,usuarioFin.email)
            this.nombreUsuarioFin.text=usuarioFin.name
            Glide.with(requireActivity() )
                .load(usuarioFin.photo)
                .into( photoUsuarioFin)
            sendMessage.setOnClickListener { x->sendMessageChat() }

            comprobarNewChat()




        }

        return V
    }

    private fun setObservers() {
        viewModel.mensajesLive.observe(this, Observer {
            this.mensajes=it
            adapter = RecyclerChat(mensajes as ArrayList<Message> ,usuarioOrigen)
            recyclerView.adapter = adapter
        })

        viewModel.añadido.observe(this, Observer {
            if(!it)
                Toast.makeText(requireActivity(),"No pudo enviarse el mensaje",Toast.LENGTH_SHORT).show()
            else{
                if(newChat){
                    newChat = false
                    cargarMensages()
                }

            }
        })

        viewModel.creaado.observe(this, Observer {
            if (it){
                viewModel.addChatUser(chat,usuarioOrigen)
                viewModel.addChatUser(chat,usuarioFin.email)
                var message = Message()
                message.from=usuarioOrigen
                message.message=this.editMessage.text.toString()
                message.time = Timestamp.now()
                viewModel.addMessage(message,chatFin,chat)
            }
            else Toast.makeText(requireActivity(),"No Crearse el chat",Toast.LENGTH_SHORT).show()

        })
        viewModel.añadidoUserChat.observe(this, Observer {
            if(!it)
                Toast.makeText(requireActivity(),"No Crearse el chat",Toast.LENGTH_SHORT).show()

        })

        viewModel.chats.observe(this, Observer {
            newChat = true
            for(chat in it)
                if( chat.users.size == 2 && chat.users.contains(usuarioOrigen) && chat.users.contains(usuarioFin.email)) {
                    this.chat=chat
                    newChat=false
                    cargarMensages()
                }

        })
    }

    private fun comprobarNewChat() {

        viewModel.getChatUser(usuarioOrigen)

    }

    private fun sendMessageChat() {
        if(newChat){
            createNewChat()
        }
        else addMesage()
    }

    private fun addMesage() {

        var message = Message()
        message.from=usuarioOrigen
        message.message=this.editMessage.text.toString()
        message.time=Timestamp.now()
        viewModel.addMessage(message,chatFin,chat)

        }



    private fun createNewChat() {

    viewModel.crearChat(chat)
    }

    private fun cargarMensages() {
        viewModel.getMessages(chat)
    }

    private fun cargarMensages(chat : Chat) {
        viewModel.getMessages(chat)
    }




    companion object {

        @JvmStatic
        fun newInstance(usuarioOrigen: String, usuarioFin: Usuario) =
            ChatFragment().apply {
                this.usuarioOrigen=usuarioOrigen
                this.usuarioFin=usuarioFin

            }
        @JvmStatic
        fun newInstance(usuarioOrigen: String, chat: Chat,chats : ArrayList<Chat>) =
            ChatFragment().apply {
                this.usuarioOrigen=usuarioOrigen
                this.chatFin=chat
                this.chats = chats
            }
    }
}

























































/*package app.Ejoin.Activities.View.Chat.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Adapter.RecyclerChat
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.Message
import app.Ejoin.DataClasses.Usuario
import app.Ejoin.R
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikhaellopez.circularimageview.CircularImageView
import utilities.Constants
import kotlin.collections.ArrayList


class ChatFragment : Fragment() {
    private  var chatFin: Chat? = null
    private lateinit var usuarioOrigen: Usuario
    private lateinit var usuarioFin: Usuario
    private lateinit var editMessage : EditText
    private lateinit var sendMessage: Button
    private lateinit var nombreUsuarioFin: TextView
    private lateinit var photoUsuarioFin : CircularImageView
    private var newChat : Boolean = true

    private lateinit var adapter : RecyclerChat
    private lateinit var recyclerView : RecyclerView

    private  var mensajes : MutableList<Message> = mutableListOf()
    private val db = Firebase.firestore
    private lateinit var auth: FirebaseAuth

    private lateinit var chat : Chat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        chat = Chat()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var V=inflater.inflate(R.layout.fragment_chat, container, false)
        this.editMessage = V.findViewById(R.id.textMessage)
        this.sendMessage=V.findViewById(R.id.sendMessage)
        this.recyclerView = V.findViewById(R.id.recyclerChat)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        this.nombreUsuarioFin = V.findViewById(R.id.nombreUsuarioFin)
        this.photoUsuarioFin=V.findViewById(R.id.imagenUsuarioFin)

        //chat grupo
        if(chatFin!=null)
        {
            newChat=false
            cargarMensages(chatFin!!)
            this.nombreUsuarioFin.text= chatFin!!.id

            Glide.with(requireActivity() )
                .load(chatFin!!.photo)
                .into( photoUsuarioFin)
            sendMessage.setOnClickListener { x->sendMessageChat() }
        }
        //chat individual

        else
        {
            chat.users= listOf( usuarioOrigen.email,usuarioFin.email)
            this.nombreUsuarioFin.text=usuarioFin.name
            Glide.with(requireActivity() )
                .load(usuarioFin.photo)
                .into( photoUsuarioFin)
            sendMessage.setOnClickListener { x->sendMessageChat() }

            comprobarNewChat()

            if(!newChat)
                cargarMensages()


        }

        return V
    }

    private fun comprobarNewChat() {
        newChat = true
        for(chat in this.usuarioOrigen.chats)
            if(chat.users.size == 2 && chat.users.contains(usuarioOrigen.email) && chat.users.contains(usuarioFin.email)) {
                newChat = false
                this.chat=chat
            }

    }

    private fun sendMessageChat() {
        if(newChat){

            createNewChat()
        }
        else addMesage()
    }

    private fun addMesage() {
        var message = Message()
        message.from=usuarioOrigen.email
        message.message=this.editMessage.text.toString()
        message.time=Timestamp.now()
        if(chatFin==null) {
            db.collection(Constants.CHATS).document(chat.id).collection("messages")
                .add(message).addOnFailureListener {
                    Toast.makeText(activity, "Mensaje no pudo enviarse", Toast.LENGTH_SHORT).show()
                }
        }
        else{
            db.collection(Constants.CHATS).document(chatFin!!.id).collection("messages")
                .add(message).addOnFailureListener {
                    Toast.makeText(activity, "Mensaje no pudo enviarse", Toast.LENGTH_SHORT).show()
                }

        }
    }



    private fun createNewChat() {



        db.collection(Constants.CHATS).add(chat).addOnSuccessListener {
            chat.id=it.id
            it.update("id",it.id)


            //update chats from userOrigin
            db.collection(Constants.USERBD).whereEqualTo(Constants.EMAIL,usuarioOrigen.email).get().addOnSuccessListener {
                it.documents.get(0)
                    .reference
                    .collection("chats")
                    .add(chat)
                    .addOnFailureListener {
                        Toast.makeText(activity,"Mensaje no pudo enviarse",Toast.LENGTH_SHORT).show()
                    }
            }

            //update chats from userFin
            db.collection(Constants.USERBD).whereEqualTo(Constants.EMAIL,usuarioFin.email).get().addOnSuccessListener {
                it.documents.get(0)
                    .reference
                    .collection("chats")
                    .add(chat)
                    .addOnFailureListener {
                        Toast.makeText(activity,"Mensaje no pudo enviarse",Toast.LENGTH_SHORT).show()
                    }
            }

            var message = Message()
            message.from=usuarioOrigen.email
            message.message=this.editMessage.text.toString()
            message.time = Timestamp.now()
            db.collection(Constants.CHATS).document(chat.id).collection("messages").add(message).addOnFailureListener {
                Toast.makeText(activity,"Chat no pudo crearse",Toast.LENGTH_SHORT).show()
            }

            db.collection(Constants.CHATS)
                .document(chat.id)
                .collection("messages").orderBy("time", Query.Direction.ASCENDING)
                .addSnapshotListener{messages,error->
                    if (error==null)
                    {
                        messages?.let{
                            mensajes = it.toObjects(Message::class.java)
                            adapter = RecyclerChat(mensajes as ArrayList<Message> ,usuarioOrigen)
                            recyclerView.adapter = adapter
                        }
                    }
                }



        }


        newChat=false
    }

    private fun cargarMensages() {


        db.collection(Constants.CHATS)
            .document(chat.id)
            .collection("messages").get()
            .addOnSuccessListener {
                for (doc in it.documents) {
                    var messageDb = Message()
                    messageDb.message = doc.get("message") as String
                    messageDb.from = doc.get("from") as String
                    messageDb.time = doc.get("time") as Timestamp
                    mensajes.add(messageDb)
                }
                cargarRecycler()

                db.collection(Constants.CHATS)
                    .document(chat.id)
                    .collection("messages").orderBy("time", Query.Direction.ASCENDING)
                    .addSnapshotListener{messages,error->
                        if (error==null)
                        {
                            messages?.let{
                                mensajes = it.toObjects(Message::class.java)
                                adapter = RecyclerChat(mensajes as ArrayList<Message> ,usuarioOrigen)
                                recyclerView.adapter = adapter
                            }
                        }
                    }


            }
    }

    private fun cargarMensages(chat : Chat) {


        db.collection(Constants.CHATS)
            .document(chat.id)
            .collection("messages").get()
            .addOnSuccessListener {
                for (doc in it.documents) {
                    var messageDb = Message()
                    messageDb.message = doc.get("message") as String
                    messageDb.from = doc.get("from") as String
                    messageDb.time = doc.get("time") as Timestamp
                    mensajes.add(messageDb)
                }
                cargarRecycler()

                db.collection(Constants.CHATS)
                    .document(chat.id)
                    .collection("messages").orderBy("time", Query.Direction.ASCENDING)
                    .addSnapshotListener{messages,error->
                        if (error==null)
                        {
                            messages?.let{
                                mensajes = it.toObjects(Message::class.java)
                                adapter = RecyclerChat(mensajes as ArrayList<Message> ,usuarioOrigen)
                                recyclerView.adapter = adapter
                            }
                        }
                    }


            }
    }

    private fun cargarRecycler() {
        adapter = RecyclerChat(mensajes as ArrayList<Message> ,usuarioOrigen)
        recyclerView.adapter = adapter
    }

    companion object {

        @JvmStatic
        fun newInstance(usuarioOrigen: Usuario, usuarioFin: Usuario) =
            ChatFragment().apply {
                this.usuarioOrigen=usuarioOrigen
                this.usuarioFin=usuarioFin
            }
        @JvmStatic
        fun newInstance(usuarioOrigen: Usuario, chat: Chat) =
            ChatFragment().apply {
                this.usuarioOrigen=usuarioOrigen
                this.chatFin=chat
            }
    }



}*/