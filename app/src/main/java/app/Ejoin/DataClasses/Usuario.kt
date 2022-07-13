package app.Ejoin.DataClasses

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import java.io.ByteArrayOutputStream

data class Usuario(var email : String="",
                   var photo : String="",
                   var name : String="",
                   var esEmpresa: Boolean=false,
                   var chats : ArrayList<Chat> = arrayListOf()  )




