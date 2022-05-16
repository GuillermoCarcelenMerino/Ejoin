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
    private lateinit var chats : ArrayList<String>

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
    fun photoBitmap(): Bitmap {
        val imageBytes = Base64.decode(photo, 0)
        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
        return bitmap
    }

    fun encodeImage(bitmap : Bitmap){
        var previewBitmap = Bitmap.createScaledBitmap(bitmap,60,60,false)
        var byteArray = ByteArrayOutputStream()
        previewBitmap.compress(Bitmap.CompressFormat.JPEG,50,byteArray)
        var bytes = byteArray.toByteArray()
        photo=Base64.encodeToString(bytes,Base64.DEFAULT)
    }

    fun getEsEmpresa() : Boolean{
        return  esEmpresa
    }
    fun setEsEmpresa(boolean :  Boolean) {
        esEmpresa  = boolean
    }
    fun setChats(chats :  ArrayList<String>) {
        this.chats  = chats
    }fun getChats(): ArrayList<String>{
        return  chats
    }

    }
