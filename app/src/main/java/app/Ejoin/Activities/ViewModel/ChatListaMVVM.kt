package app.Ejoin.Activities.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.Ejoin.Activities.Model.Chats
import app.Ejoin.Activities.Model.Usuarios
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.Usuario
import com.google.firebase.firestore.DocumentReference

class ChatListaMVVM : ViewModel() {
    private val firebase = Usuarios()
    private val firebaseChats = Chats()
    val userReference = MutableLiveData<DocumentReference>()
    val chats = MutableLiveData<MutableList<Chat>>()
    val usuariosFiltrados = MutableLiveData<MutableList<Usuario>>()


    fun getUserReference(email :String) {
        firebase.getUsuarioReference(email).observeForever{
                reference-> userReference.value=reference
        }
    }
    fun getChatsUsuario(userReference: DocumentReference){
        firebaseChats.getChats(userReference).observeForever{
            chats.value=it
        }
    }
    fun getUsuariosFiltrados(usuarios : ArrayList<String>){
        firebase.getUsuariosFiltrado(usuarios).observeForever{
            usuariosFiltrados.value=it
        }
    }



}