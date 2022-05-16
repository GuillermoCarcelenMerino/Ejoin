package app.Ejoin.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Activities.LoginActivity
import app.Ejoin.Activities.Perfil
import app.Ejoin.DataClasses.Usuarios
import app.Ejoin.R
import com.mikhaellopez.circularimageview.CircularImageView
import utilities.Constants

class RecyclerUsuarios (private val dataSet : ArrayList<Usuarios>) :
    RecyclerView.Adapter<RecyclerUsuarios.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
    // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.userrow, parent, false)

        return RecyclerUsuarios.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.imagen.setImageBitmap(dataSet[position].photoBitmap())
        holder.nombreUsuario.text = dataSet[position].getName()
        holder.usuario = dataSet[position]
    }

    override fun getItemCount() = dataSet.size


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var usuario = Usuarios()
        val imagen: CircularImageView
        val nombreUsuario : TextView


        init {
            imagen = view.findViewById(R.id.fotoUsuario)
            nombreUsuario  =view.findViewById(R.id.nombreUsuario)
            view.setOnClickListener{
                view.context.startActivity(Intent(view.context, Perfil::class.java).apply {
                    putExtra(Constants.NOMBREUSUARIO,usuario.getName())
                    putExtra(Constants.USERPHOTO,usuario.getPhoto())
                    putExtra(Constants.EMAIL,usuario.getEmail())
                })
            }
        }


    }
}