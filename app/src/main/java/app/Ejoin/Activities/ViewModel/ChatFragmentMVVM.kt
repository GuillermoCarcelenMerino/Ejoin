package app.Ejoin.Activities.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.Ejoin.Activities.Model.Chats
import app.Ejoin.Activities.Model.Eventos
import app.Ejoin.Activities.Model.Usuarios
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.Message

class ChatFragmentMVVM : ViewModel() {

    private val firebase = Chats()
    private val firebaseUsuarios = Usuarios()
    val mensajesLive = MutableLiveData<MutableList<Message>>()
    val añadido = MutableLiveData<Boolean>()
    val añadidoUserChat = MutableLiveData<Boolean>()
    val chats = MutableLiveData<MutableList<Chat>>()
    val creaado = MutableLiveData<Boolean>()


    fun getMessages(chat : Chat){
        firebase.getMensajes(chat).observeForever{
            mensajesLive.value=it
        }
    }

    fun addMessage(message: Message, chatFin: Chat?, chat: Chat){
        firebase.addMessage(message,chatFin,chat).observeForever {
            añadido.value=it
        }
    }
    fun crearChat(chat: Chat){
        firebase.createChat(chat).observeForever {
            creaado.value=it
        }
    }
    fun addChatUser(chat : Chat,usuarioOrigen : String) {
        firebaseUsuarios.addChatUser(chat,usuarioOrigen).observeForever {
            añadidoUserChat.value=it
        }
    }

    fun getChatUser(userMail : String){
        firebaseUsuarios.getChatUser(userMail).observeForever {
            chats.value=it
        }
    }
    }