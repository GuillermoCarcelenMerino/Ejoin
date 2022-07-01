package app.Ejoin.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.DataClasses.Message
import app.Ejoin.DataClasses.Usuario
import app.Ejoin.R

class RecyclerChat (private val dataSet: ArrayList<Message>,private val usuario : String) :
    RecyclerView.Adapter<RecyclerChat.ViewHolder>() {

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.chatrow, viewGroup, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        if(dataSet[position].from==this.usuario) {
            viewHolder.mensajeEmisor.text = dataSet[position].message
            viewHolder.mensajeReceptor.visibility=View.GONE
        }
        else {
            viewHolder.mensajeReceptor.text = dataSet[position].message
            viewHolder.mensajeEmisor.visibility=View.GONE
        }
    }

    override fun getItemCount() = dataSet.size



    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var mensajeEmisor : TextView
        var mensajeReceptor : TextView

        init {
            this.mensajeReceptor = view.findViewById(R.id.mensajeReceptor)
            this.mensajeEmisor = view.findViewById(R.id.mensajeEmisor)

        }
    }

}