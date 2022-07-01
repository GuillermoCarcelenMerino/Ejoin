package app.Ejoin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Activities.View.Chat.fragments.ChatListfragment
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.Usuario
import app.Ejoin.R
import com.bumptech.glide.Glide
import com.mikhaellopez.circularimageview.CircularImageView
import utilities.Constants
import utilities.PreferencesManager

class RecyclerChatLista (private val dataSet: ArrayList<Usuario>, private val chats: ArrayList<Chat>, private val fragment : ChatListfragment) :
    RecyclerView.Adapter<RecyclerChatLista.ViewHolder>() {
    private lateinit var userPreferences : PreferencesManager
    private lateinit var usuario : String


    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.chatlistrow, viewGroup, false)
        userPreferences= PreferencesManager(fragment.requireActivity())
        usuario= userPreferences.getString(Constants.EMAIL)!!
        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

            viewHolder.fragment = fragment


            if(chats.size!=0) {

                if (chats[position].evento) {
                    viewHolder.chatGrupo = chats[position]
                    Glide.with(fragment )
                        .load(chats[position].photo)
                        .into( viewHolder.imagenChat)
                    viewHolder.nombreChat.text = chats[position].nombre


                } else {
                    for (hablado in chats[position].users) {
                        if(hablado!=usuario){
                            var x = dataSet.filter { x-> x.email==hablado }[0]
                            Glide.with(fragment )
                                .load(x.photo)
                                .into( viewHolder.imagenChat)
                            viewHolder.nombreChat.text = x.name
                            viewHolder.usuarioIr = x
                        }
                    }
                }
            }
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() : Int {

            return chats.size

    }


    /*
    * TODO Incluir informacion en intent para evitar realizar busquedas nuevas en firebase sobre el evento a analizar
    *  Añadir boton para busqueda en mapa
    *
    * */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imagenChat : CircularImageView
        val nombreChat : TextView
        lateinit var usuarioIr : Usuario
        var chatGrupo : Chat? = null
        lateinit var fragment : ChatListfragment
        init {
            imagenChat = view.findViewById(R.id.fotoChat)
            nombreChat = view.findViewById(R.id.nombreChat)

            view.setOnClickListener {
                if(chatGrupo==null)
                fragment.replaceFragment(usuarioIr)
                else
                fragment.replaceFragment(chatGrupo!!)

            }


        }
    }

}