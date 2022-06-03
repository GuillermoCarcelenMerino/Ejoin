package app.Ejoin.Adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Activities.Perfil
import app.Ejoin.DataClasses.Filtros
import app.Ejoin.DataClasses.Usuarios
import app.Ejoin.R
import com.mikhaellopez.circularimageview.CircularImageView
import utilities.Constants

class RecyclerFiltro (private val filtros : ArrayList<String>) :
RecyclerView.Adapter<RecyclerFiltro.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.filter_row, parent, false)

        return RecyclerFiltro.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.nombreFiltro.text=filtros[position]
    }

    override fun getItemCount() = filtros.size


    class ViewHolder(view : View) : RecyclerView.ViewHolder(view){
        var nombreFiltro : TextView


        init {
            nombreFiltro = view.findViewById(R.id.filtro)

        }


    }
}