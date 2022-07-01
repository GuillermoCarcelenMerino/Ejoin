package utilities

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.Ejoin.DataClasses.EventoData
import app.Ejoin.R
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class ventanaGoogle(context: Activity) : GoogleMap.InfoWindowAdapter {

    private var context : Activity
    init {
        this.context=context
        }
    override fun getInfoContents(p0: Marker): View? {
        TODO("Not yet implemented")
    }

    @SuppressLint("SetTextI18n")
    override fun getInfoWindow(p0: Marker): View? {
        var view : View
        view=context.layoutInflater.inflate(R.layout.markerlayout,null)
        view.findViewById<TextView>(R.id.NombreEventoMarker).text="hola"
         var evento=p0.tag as EventoData
        view.findViewById<TextView>(R.id.NombreEventoMarker).text=evento.nombreEvento
        view.findViewById<TextView>(R.id.bubble_title).text=evento.precio.toString()

        var image= view.findViewById<ImageView>(R.id.bubble_image)
        Glide.with(context )
            .load(evento.photo)
            .into(image)
        view.findViewById<TextView>(R.id.bubble_subdescription)
        view.findViewById<TextView>(R.id.bubble_description).text=evento.usuarios.size.toString()+"/"+evento.maxUsuarios.toString()
        return view





    }



}