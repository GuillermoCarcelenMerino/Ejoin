package app.Ejoin.Activities.ViewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.Ejoin.Activities.Model.Usuarios
import app.Ejoin.DataClasses.EventoData

class PerfilViewModel : ViewModel() {
    private val firebase = Usuarios()
    val eventosusuarioList = MutableLiveData<MutableList<EventoData>>()
    fun getEventosUsuario(usuario : String) {
        firebase.getEventosUsuario(usuario ).observeForever{
            eventList-> eventosusuarioList.value=eventList
        }
    }
}