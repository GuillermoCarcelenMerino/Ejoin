package app.Ejoin.Activities.ViewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.Ejoin.Activities.Model.Eventos
import app.Ejoin.DataClasses.EventoData
import app.Ejoin.DataClasses.Usuario
import com.google.firebase.storage.StorageReference
import utilities.PreferencesManager


class EventosDatoViewModel : ViewModel() {
   private val firebase = Eventos()
    val eventoUpdate = MutableLiveData<EventoData>()
    val usuarios = MutableLiveData<MutableList<Usuario>>()
    fun registra(userPreferences : PreferencesManager, evento : EventoData) {
        firebase.registra(userPreferences,evento).observeForever{
                event-> eventoUpdate.value=event
        }
    }
    val imageEvento = MutableLiveData<String>()
    val eventoAdded = MutableLiveData<Boolean> ()
    fun addEVent(evento : EventoData) {
        firebase.addEvento(evento).observeForever{
            done -> eventoAdded.value=true
        }
    }
    fun addImage(uri : Uri, storageReference : StorageReference){
        firebase.addImagenEvent(uri,storageReference).observeForever{
                url -> imageEvento.value=url
        }
    }
    fun getUsuariosEvento( eventoUsers : ArrayList<String>) {
        firebase.getUsuariosEvento(eventoUsers).observeForever {
            usuarios.value=it
        }

    }
}