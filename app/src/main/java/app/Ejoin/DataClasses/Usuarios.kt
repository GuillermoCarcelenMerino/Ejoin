package app.Ejoin.DataClasses

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

class Usuarios {
    private lateinit var email : String
    private lateinit var id : String
    private lateinit var photo : String
    private lateinit var name : String
    private var esEmpresa : Boolean = false
    private  var chats : ArrayList<Chat> = arrayListOf()

    constructor()
    constructor(
        email: String,
        userID: String,
        userPhoto: String,
        nombreUsuario: String,
        esEmpresa: Boolean
    ) {
        this.email = email
        this.id = userID
        this.photo = userPhoto
        this.name = nombreUsuario
        this.esEmpresa = esEmpresa
    }


    fun getName() : String{
        return name
    }

    fun setId(id : String){
        this.id = id
    }
    fun setEmail(mail : String){
        email=mail
    }
    fun setName(name : String){
        this.name =name
    }
    fun getId(): String{
        return id
    }
    fun getEmail(): String{
        return email
    }
    fun setPhoto(string : String)
    {
        photo = string
    }
    fun getPhoto() : String {
        return photo
    }

    fun getEsEmpresa() : Boolean{
        return  esEmpresa
    }
    fun setEsEmpresa(boolean :  Boolean) {
        esEmpresa  = boolean
    }
    fun setChats(chats :  ArrayList<Chat>) {
        this.chats  = chats
    }
    fun getChats(): ArrayList<Chat>{
        if(chats==null)
            return arrayListOf()
        else return  chats
    }

    }
