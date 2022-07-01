package app.Ejoin.Activities.Model

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import app.Ejoin.Adapter.RecyclerChat
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.EventoData
import app.Ejoin.DataClasses.Message
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import utilities.Constants
import utilities.PreferencesManager

class Eventos {
    fun registra(userPreferences : PreferencesManager, evento : EventoData) : MutableLiveData<EventoData> {

        val mutableEvent = MutableLiveData<EventoData>()
        val usuarioEmail = userPreferences.getString(Constants.EMAIL)
        val inscritos = evento.usuarios
        val nuevosusuarios : MutableList<String> = inscritos.toMutableList()
        nuevosusuarios.add(usuarioEmail!!)
        evento.usuarios=nuevosusuarios as ArrayList<String>

        FirebaseFirestore.getInstance().collection(Constants.EVENTOSDB)
            .document(evento.id!!)
            .update(Constants.USUARIOS,nuevosusuarios)

        FirebaseFirestore.getInstance()
            .collection(Constants.CHATS)
            .document(evento.id!!)
            .get()
            .addOnSuccessListener {
                var usuario = userPreferences.getString(Constants.EMAIL)
                var chat = it.toObject(Chat::class.java)
                var userChat = ArrayList(chat!!.users)
                userChat.add(usuario)
                chat.users=userChat
                for (user in chat.users)
                {
                    if(user!=usuario)
                    //update chats from userOrigin
                        FirebaseFirestore.getInstance()
                            .collection(Constants.USERBD)
                            .whereEqualTo(Constants.EMAIL,user)
                            .get().addOnSuccessListener {
                                it.documents.get(0)
                                    .reference
                                    .collection("chats")
                                    .document(chat.id)
                                    .update("users",chat.users)
                            }
                    else{
                        FirebaseFirestore.getInstance()
                            .collection(Constants.USERBD)
                            .whereEqualTo(Constants.EMAIL,user)
                            .get()
                            .addOnSuccessListener {
                                it.documents.get(0)
                                    .reference
                                    .collection("chats")
                                    .document(chat.id)
                                    .set(chat)
                            }
                    }
                }

                it.reference.update("users",userChat)
            }
        mutableEvent.value = evento
        return mutableEvent
    }

    fun addImagenEvent(uri : Uri,storageReference : StorageReference)  : MutableLiveData<String> {
            var imagenUrl = MutableLiveData<String>()
            var uploadTask = storageReference.putFile(uri)

            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnSuccessListener { taskSnapshot ->

                taskSnapshot.storage.downloadUrl.addOnCompleteListener {
                     imagenUrl.value = it.result.toString()
                }


            }

        return imagenUrl
    }

    fun addEvento(evento : EventoData,) : MutableLiveData<Boolean>{
        var done = MutableLiveData<Boolean>()
        FirebaseFirestore.getInstance()
            .collection(Constants.EVENTOSDB)
            .add(evento).addOnSuccessListener {
            evento.id=it.id
            it.update("id",it.id)
            var chat = Chat()
            chat.nombre=evento.nombreEvento
            chat.id=it.id
            chat.users=evento.usuarios
            chat.evento=true
            chat.photo=evento.photo
                FirebaseFirestore.getInstance().collection(Constants.CHATS).document(it.id).set(chat)
                FirebaseFirestore.getInstance().collection(Constants.USUARIOS).whereEqualTo(Constants.EMAIL,evento.usuarios[0]).get()
                .addOnSuccessListener {
                    it.documents[0].reference.collection(Constants.CHATS).document(chat.id).set(chat)
                    done.value = true
                }

        }
        return done
    }

    fun getEventos(): MutableLiveData<MutableList<EventoData>>{
        var eventosList = MutableLiveData<MutableList<EventoData>>()

        FirebaseFirestore.getInstance().collection(Constants.EVENTOSDB)
            .addSnapshotListener{eventos,error->
                if (error==null)
                {
                    eventos?.let{
                        eventosList.value = it.toObjects(EventoData::class.java)

                    }
                }
            }

        return eventosList

    }


}