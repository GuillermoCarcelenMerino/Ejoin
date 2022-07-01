package app.Ejoin.Activities.Model

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import app.Ejoin.Adapter.RecyclerChat
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.Message
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import utilities.Constants

class Chats {

    fun getChats(userReference: DocumentReference) : MutableLiveData<MutableList<Chat>>{
        var chatsUser = MutableLiveData<MutableList<Chat>>()
        userReference.collection("chats")
            .addSnapshotListener { chats, error ->
                if(error==null)
                {
                    chats?.let {
                        chatsUser.value=it.toObjects(Chat::class.java)
                    }
                }
            }
        return chatsUser
    }

    fun getMensajes(chat : Chat) : MutableLiveData<MutableList<Message>>{

        var mensajes = MutableLiveData<MutableList<Message>>();
        FirebaseFirestore.getInstance().collection(Constants.CHATS)
                    .document(chat.id)
                    .collection("messages").orderBy("time", Query.Direction.ASCENDING)
                    .addSnapshotListener{messages,error->
                        if (error==null)
                        {
                            messages?.let{
                                mensajes.value = it.toObjects(Message::class.java)
                            }
                        }
                    }
        return  mensajes
    }
    fun addMessage(message: Message, chatFin: Chat?, chat: Chat): MutableLiveData<Boolean>{
        var añadido = MutableLiveData<Boolean>()

            if(chatFin==null) {
                FirebaseFirestore.getInstance().collection(Constants.CHATS).document(chat.id).collection("messages")
                    .add(message)
                    .addOnSuccessListener {
                        x->añadido.value=true
                    }
                    .addOnFailureListener {
                            x->añadido.value=false
                    }
            }
            else{
                FirebaseFirestore.getInstance().collection(Constants.CHATS).document(chatFin!!.id).collection("messages")
                    .add(message)
                    .addOnSuccessListener {
                            x->añadido.value=true
                    }
                    .addOnFailureListener {
                            x->añadido.value=false
                    }

            }

        return añadido
        }

    fun createChat(chat : Chat): MutableLiveData<Boolean>{
        var creado = MutableLiveData<Boolean>()
        FirebaseFirestore.getInstance().collection(Constants.CHATS).add(chat).addOnSuccessListener {
            chat.id = it.id
            it.update("id", it.id).addOnSuccessListener {
                creado.value=true
            }

                .addOnFailureListener {
                    creado.value = false
                }
        }.addOnFailureListener {
            creado.value=false
        }
        return creado
    }



    }


