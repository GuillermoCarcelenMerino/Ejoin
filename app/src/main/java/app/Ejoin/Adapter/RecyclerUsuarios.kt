package app.Ejoin.Adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Activities.View.Perfil
import app.Ejoin.DataClasses.Chat
import app.Ejoin.DataClasses.Usuario
import app.Ejoin.R
import com.bumptech.glide.Glide
import com.mikhaellopez.circularimageview.CircularImageView
import utilities.Constants

class RecyclerUsuarios (private val context : Activity,private val dataSet : ArrayList<Usuario>) :
    RecyclerView.Adapter<RecyclerUsuarios.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.userrow, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context )
            .load(dataSet[position].photo)
            .into( holder.imagen)
        holder.nombreUsuario.text = dataSet[position].name
        holder.usuario = dataSet[position]
    }

    override fun getItemCount() = dataSet.size


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var usuario = Usuario()
        val imagen: CircularImageView
        val nombreUsuario : TextView


        init {
            imagen = view.findViewById(R.id.fotoUsuario)
            nombreUsuario  =view.findViewById(R.id.nombreUsuario)
            view.setOnClickListener{
                view.context.startActivity(Intent(view.context, Perfil::class.java).apply {
                    putExtra(Constants.NOMBREUSUARIO,usuario.name)
                    putExtra(Constants.USERPHOTO,usuario.photo)
                    putExtra(Constants.EMAIL,usuario.email)
                    putExtra(Constants.ESEMPRESA,usuario.esEmpresa)
                    putExtra(Constants.CHATS,usuario.chats)

                })
            }
        }


    }
}