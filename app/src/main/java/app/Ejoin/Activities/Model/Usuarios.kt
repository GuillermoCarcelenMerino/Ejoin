package app.Ejoin.Activities.Model

import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.EventoData
import app.Ejoin.DataClasses.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirestoreRegistrar
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import utilities.Constants

class Usuarios {
    private  lateinit var userReference : DocumentReference
    fun getEventosUsuario( usuario : String) :MutableLiveData<MutableList<EventoData>> {
        var mutableData = MutableLiveData<MutableList<EventoData>>()
        FirebaseFirestore.getInstance().collection(Constants.EVENTOSDB).whereArrayContains(Constants.USUARIOS,usuario).get()
            .addOnSuccessListener{
                    result->
                mutableData.value=result.toObjects(EventoData::class.java)
            }
        return mutableData
    }

    fun comprobarDatosLogin(email : String,password : String) : MutableLiveData<Usuario>{
        var mutableUser = MutableLiveData<Usuario>()
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnSuccessListener {
            FirebaseFirestore.getInstance().collection(Constants.USERBD).
            whereEqualTo(Constants.EMAIL,email)
                .get()
                .addOnSuccessListener {
                    var usuario : Usuario= it.documents[0].toObject(Usuario::class.java)!!
                    mutableUser.value = usuario
                }
        }
        return mutableUser
    }


    fun checkUserName(nombre : String) : MutableLiveData<Boolean>{
        var usado = MutableLiveData<Boolean>()
        FirebaseFirestore.getInstance().collection(Constants.USERBD).
            whereEqualTo(Constants.NOMBREUSUARIO,nombre)
                .get()
                .addOnSuccessListener {
                    if (it.documents.isEmpty()){
                        usado.value=false
                    }
                    else {
                        usado.value= true
                    }

                }
        return  usado

    }

    fun registrarUsuario(password : String,usuario : Usuario,uri : Uri) : MutableLiveData<Boolean>{
        var registrado = MutableLiveData<Boolean>()
        var auth=  FirebaseAuth.getInstance()
        auth.createUserWithEmailAndPassword(usuario.email, password)
            .addOnSuccessListener { task ->



                var storage = Firebase.storage

                val storageRef = storage.reference
                var file = uri
                val riversRef = storageRef.child("usuarios/"+usuario.email)
                var uploadTask = riversRef.putFile(file!!)

                // Register observers to listen for when the download is done or if it fails
                uploadTask.addOnSuccessListener { taskSnapshot ->

                    taskSnapshot.storage.downloadUrl.addOnCompleteListener {
                        var imagenUrl = it.result.toString()
                        usuario.photo=imagenUrl
                        FirebaseFirestore.getInstance().collection(Constants.USERBD)
                            .add(usuario)
                            .addOnSuccessListener { documentReference ->
                                registrado.value=true
                            }
                            .addOnFailureListener { e ->
                                registrado.value=false
                            }

                    }}


            }.addOnFailureListener {
                registrado.value=false

            }
        return registrado

    }

    fun getUsuarioReference(emailUsuario : String) :MutableLiveData<DocumentReference>{
       var usuarioChatLive = MutableLiveData<DocumentReference>()


        FirebaseFirestore.getInstance().collection(Constants.USERBD).whereEqualTo(Constants.EMAIL,emailUsuario)
            .get()
            .addOnSuccessListener {
               usuarioChatLive.value=it.documents[0].reference
            }

        return usuarioChatLive

    }

    fun getUsuariosFiltrado(usuarios : ArrayList<String>) : MutableLiveData<MutableList<Usuario>>{
        var usuariosFiltrados = MutableLiveData<MutableList<Usuario>>()
        FirebaseFirestore.getInstance().collection(Constants.USERBD).whereIn(Constants.EMAIL,usuarios).get().addOnSuccessListener {
            usuariosFiltrados.value = it.toObjects(Usuario::class.java)
        }
        return  usuariosFiltrados
    }

    fun addChatUser(chat : Chat,usuarioOrigen : String): MutableLiveData<Boolean>{
        var añadido = MutableLiveData<Boolean>()

        //update chats from userOrigin
        FirebaseFirestore.getInstance().collection(Constants.USERBD).whereEqualTo(Constants.EMAIL,usuarioOrigen).get().addOnSuccessListener {
            it.documents.get(0)
                .reference
                .collection("chats")
                .add(chat)
                .addOnSuccessListener {
                    añadido.value=true
                }
                .addOnFailureListener {
                    añadido.value=false
                }
        }
        return añadido
    }

    fun getChatUser(userEmail : String) : MutableLiveData<MutableList<Chat>>{
        var chats =  MutableLiveData<MutableList<Chat>>()
        FirebaseFirestore.getInstance().collection(Constants.USERBD).whereEqualTo(Constants.EMAIL,userEmail)
            .get().addOnSuccessListener {
                it.documents[0].reference.collection(Constants.CHATS).get()
                    .addOnSuccessListener {
                        chats.value=it.toObjects(Chat::class.java)
                    }
            }
        return chats
    }

    }

