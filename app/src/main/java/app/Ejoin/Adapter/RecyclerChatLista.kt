package app.Ejoin.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Activities.ChatListfragment
import app.Ejoin.Activities.DatosEventos
import app.Ejoin.DataClasses.Evento
import app.Ejoin.DataClasses.Usuarios
import app.Ejoin.R
import com.mikhaellopez.circularimageview.CircularImageView
import utilities.Constants

class RecyclerChatLista (private val dataSet: ArrayList<Usuarios>,private val fragment : ChatListfragment) :
    RecyclerView.Adapter<RecyclerChatLista.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.chatlistrow, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {


            viewHolder.imagenChat.setImageBitmap(dataSet[position].photoBitmap())
            viewHolder.nombreChat.text=dataSet[position].getName()
            viewHolder.usuarioIr = dataSet[position]
             viewHolder.fragment = fragment
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


    /*
    * TODO Incluir informacion en intent para evitar realizar busquedas nuevas en firebase sobre el evento a analizar
    *  Añadir boton para busqueda en mapa
    *
    * */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagenChat : CircularImageView
        val nombreChat : TextView
        lateinit var usuarioIr : Usuarios
        lateinit var fragment : ChatListfragment
        init {
            imagenChat = view.findViewById(R.id.fotoChat)
            nombreChat = view.findViewById(R.id.nombreChat)
            view.setOnClickListener {
                fragment.replaceFragment()


            }
        }
    }

}