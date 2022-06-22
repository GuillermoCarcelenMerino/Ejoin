package app.Ejoin.Activities

import android.app.DownloadManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
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
import app.Ejoin.DataClasses.Usuarios
import app.Ejoin.R
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikhaellopez.circularimageview.CircularImageView
import utilities.Constants
import java.util.*
import kotlin.collections.ArrayList


class ChatFragment : Fragment() {
    private  var chatFin: Chat? = null
    private lateinit var usuarioOrigen: Usuarios
    private lateinit var usuarioFin: Usuarios
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
            chat.users= listOf( usuarioOrigen.getEmail(),usuarioFin.getEmail())
            this.nombreUsuarioFin.text=usuarioFin.getName()
            Glide.with(requireActivity() )
                .load(usuarioFin.getPhoto())
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
        for(chat in this.usuarioOrigen.getChats())
            if(chat.users.size == 2 && chat.users.contains(usuarioOrigen.getEmail()) && chat.users.contains(usuarioFin.getEmail())) {
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
        message.from=usuarioOrigen.getEmail()
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
            db.collection(Constants.USERBD).whereEqualTo(Constants.EMAIL,usuarioOrigen.getEmail()).get().addOnSuccessListener {
                it.documents.get(0)
                    .reference
                    .collection("chats")
                    .add(chat)
                    .addOnFailureListener {
                        Toast.makeText(activity,"Mensaje no pudo enviarse",Toast.LENGTH_SHORT).show()
                    }
            }

            //update chats from userFin
            db.collection(Constants.USERBD).whereEqualTo(Constants.EMAIL,usuarioFin.getEmail()).get().addOnSuccessListener {
                it.documents.get(0)
                    .reference
                    .collection("chats")
                    .add(chat)
                    .addOnFailureListener {
                        Toast.makeText(activity,"Mensaje no pudo enviarse",Toast.LENGTH_SHORT).show()
                    }
            }

            var message = Message()
            message.from=usuarioOrigen.getEmail()
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
        fun newInstance(usuarioOrigen: Usuarios, usuarioFin: Usuarios) =
            ChatFragment().apply {
                this.usuarioOrigen=usuarioOrigen
                this.usuarioFin=usuarioFin
            }
        @JvmStatic
        fun newInstance(usuarioOrigen: Usuarios, chat: Chat) =
            ChatFragment().apply {
                this.usuarioOrigen=usuarioOrigen
                this.chatFin=chat
            }
    }



}