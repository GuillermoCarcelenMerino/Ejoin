package app.Ejoin.Activities.ViewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.Ejoin.Activities.Model.Usuarios
import app.Ejoin.DataClasses.Usuario

class RegisterVieweModel  : ViewModel() {
    private val firebase = Usuarios()
    val nombreUsado = MutableLiveData<Boolean>()
    fun comprobarDatosLogin(nombre : String) {
        firebase.checkUserName(nombre).observeForever{
                nombre-> nombreUsado.value=nombre
        }
    }
    val registrado = MutableLiveData<Boolean>()
    fun registrar(password : String,usuario : Usuario,uri : Uri)  {
        firebase.registrarUsuario(password, usuario, uri).observeForever {
            registrado.value = it
        }
    }

}
