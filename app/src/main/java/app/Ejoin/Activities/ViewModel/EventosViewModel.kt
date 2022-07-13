package app.Ejoin.Activities.ViewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import app.Ejoin.Activities.Model.Eventos
import app.Ejoin.DataClasses.EventoData
import com.google.firebase.storage.StorageReference
import utilities.PreferencesManager

class EventosViewModel : ViewModel() {
    private val firebase = Eventos()
    val eventos = MutableLiveData<MutableList<EventoData>>()
    fun getEventos() {
        firebase.getEventos().observeForever{
                eventos-> this.eventos.value=eventos
        }
    }
}