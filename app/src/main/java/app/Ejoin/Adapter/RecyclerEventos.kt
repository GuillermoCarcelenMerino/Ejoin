package app.Ejoin.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Activities.DatosEventos
import app.Ejoin.DataClasses.Evento
import app.Ejoin.R
import com.bumptech.glide.Glide
import utilities.Constants

class RecyclerEventos (private val context : Context,private val dataSet: ArrayList<Evento>) :
RecyclerView.Adapter<RecyclerEventos.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.eventrow, viewGroup, false)

        return ViewHolder(view)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.nombre.text = dataSet[position].getNombreEvento()
        viewHolder.fecha.text = dataSet[position].getFecha()
        viewHolder.empresa.text = dataSet[position].getEmpresa()
        viewHolder.lugar.text = dataSet[position].getLugar()
        viewHolder.precio.text = dataSet[position].getPrecio().toString()+"€"
        viewHolder.categoria.text = dataSet[position].getCategoria()
        viewHolder.usuarios.text = dataSet[position].getusuarios().size.toString() + "/"+ dataSet[position].getMaxUsuarios().toString()
        viewHolder.evento = dataSet[position]
        Glide.with(context )
            .load(dataSet[position].getPhoto())
            .into(viewHolder.photo)


    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size


    /*
    * TODO Incluir informacion en intent para evitar realizar busquedas nuevas en firebase sobre el evento a analizar
    *  Añadir boton para busqueda en mapa
    *
    * */
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
         var evento  = Evento()
        val nombre: TextView
        val empresa : TextView
        val lugar : TextView
        val fecha : TextView
        val precio : TextView
        val categoria : TextView
        val usuarios : TextView
        val photo : ImageView

        init {
            // Define click listener for the ViewHolder's View.
            view.setOnClickListener{
               view.context.startActivity(Intent(view.context, DatosEventos::class.java).apply {
                   putExtra(Constants.EVENTOID,evento.getId())
                   putExtra(Constants.NOMBREVENTO,evento.getNombreEvento())
                   putExtra(Constants.EMAIL,evento.getEmpresa())
                   putExtra(Constants.FECHA,evento.getFecha())
                   putExtra(Constants.CATEGORIA,evento.getCategoria())
                   putExtra(Constants.DETALLES,evento.getDetalles())
                   putExtra(Constants.PRECIO,evento.getPrecio())
                   putExtra(Constants.LUGAR,evento.getLugar())
                   putExtra(Constants.ALTITUD,evento.getCordenada().latitude)
                   putExtra(Constants.LONGITUD,evento.getCordenada().longitude)
                   putExtra(Constants.MAXUSUARIOS,evento.getMaxUsuarios())
                   putExtra(Constants.USUARIOS,evento.getusuarios())
                   putExtra(Constants.USERPHOTO,evento.getPhoto())
               })
            }
            nombre = view.findViewById(R.id.NombreEvento)
            empresa = view.findViewById(R.id.NombreEmpresa)
            lugar = view.findViewById(R.id.LugarEvento)
            fecha = view.findViewById(R.id.FechaEvento)
            precio = view.findViewById(R.id.PrecioEvento)
            categoria = view.findViewById(R.id.CategoriaEvento)
            usuarios = view.findViewById(R.id.Usuarios)
            photo = view.findViewById(R.id.ImagenEvento)

        }
    }

}
