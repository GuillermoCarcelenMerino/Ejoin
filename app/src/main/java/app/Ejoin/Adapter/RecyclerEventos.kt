package app.Ejoin.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Activities.Model.Usuarios
import app.Ejoin.Activities.View.DatosEventos
import app.Ejoin.DataClasses.EventoData
import app.Ejoin.DataClasses.Usuario
import app.Ejoin.R
import com.bumptech.glide.Glide
import utilities.Constants

class RecyclerEventos(
    private val context: Context,
    private val dataSet: ArrayList<EventoData>,
    private val perfil : Boolean
) :
RecyclerView.Adapter<RecyclerEventos.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view : View
        if(!perfil)
        view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.eventrow, viewGroup, false)
        else view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.perfileventrow, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.nombre.text = dataSet[position].nombreEvento
        viewHolder.fecha.text = dataSet[position].fecha
        viewHolder.empresa.text = dataSet[position].empresa
        viewHolder.precio.text = dataSet[position].precio.toString()+"€"
        viewHolder.usuarios.text = dataSet[position].usuarios.size.toString() + "/"+ dataSet[position].maxUsuarios.toString()
        viewHolder.evento = dataSet[position]
        Glide.with(context )
            .load(dataSet[position].photo)
            .into(viewHolder.photo)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         var evento  = EventoData()
        val nombre: TextView
        val empresa : TextView
        val fecha : TextView
        val precio : TextView
        val usuarios : TextView
        val photo : ImageView

        init {
            // Define click listener for the ViewHolder's View.
            view.setOnClickListener{
               view.context.startActivity(Intent(view.context, DatosEventos::class.java).apply {
                   putExtra(Constants.EVENTOID,evento.id)
                   putExtra(Constants.NOMBREVENTO,evento.nombreEvento)
                   putExtra(Constants.EMAIL,evento.empresa)
                   putExtra(Constants.FECHA,evento.fecha)
                   putExtra(Constants.CATEGORIA,evento.categoria)
                   putExtra(Constants.DETALLES,evento.detalles)
                   putExtra(Constants.PRECIO,evento.precio)
                   putExtra(Constants.LUGAR,evento.lugar)
                   putExtra(Constants.ALTITUD,evento.cordenada.latitude)
                   putExtra(Constants.LONGITUD,evento.cordenada.longitude)
                   putExtra(Constants.MAXUSUARIOS,evento.maxUsuarios)
                   putExtra(Constants.USUARIOS,evento.usuarios)
                   putExtra(Constants.USERPHOTO,evento.photo)
               })
            }
            nombre = view.findViewById(R.id.NombreEvento)
            empresa = view.findViewById(R.id.NombreEmpresa)
            fecha = view.findViewById(R.id.FechaEvento)
            precio = view.findViewById(R.id.PrecioEvento)
            usuarios = view.findViewById(R.id.Usuarios)
            photo = view.findViewById(R.id.ImagenEvento)

        }
    }

}
