package app.Ejoin.Activities.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.Ejoin.Activities.Model.Usuarios
import app.Ejoin.DataClasses.EventoData

class RecuperarViewModel : ViewModel() {
    private val firebase = Usuarios()
    val usuarioExiste = MutableLiveData<Boolean>()
    fun emailExiste(email : String) {
        firebase.checkMail(email ).observeForever{
            usuarioExiste.value=it
        }
    }
}