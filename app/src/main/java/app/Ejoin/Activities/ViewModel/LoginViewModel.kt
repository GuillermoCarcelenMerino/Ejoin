package app.Ejoin.Activities.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.Ejoin.Activities.Model.Usuarios
import app.Ejoin.DataClasses.EventoData
import app.Ejoin.DataClasses.Usuario

class LoginViewModel : ViewModel() {
    private val firebase = Usuarios()
    val usuarioLogeado = MutableLiveData<Usuario>()
    fun comprobarDatosLogin(email : String,password : String) {
        firebase.comprobarDatosLogin(email,password ).observeForever{
                usuario-> usuarioLogeado.value=usuario
        }
    }
}