package utilities

import android.app.Activity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import app.Ejoin.DataClasses.EventoData
import app.Ejoin.R
import com.bumptech.glide.Glide
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking

class ventanaGoogle(context: Activity) : GoogleMap.InfoWindowAdapter {

    private var context : Activity
    init {
        this.context=context
        }
    override fun getInfoContents(p0: Marker): View? {
        TODO("Not yet implemented")
    }


    override fun getInfoWindow(p0: Marker): View? {
        var view : View
        view=context.layoutInflater.inflate(R.layout.markerlayout,null)
        view.findViewById<TextView>(R.id.NombreEventoMarker).text="hola"
         var evento=p0.tag as EventoData
        view.findViewById<TextView>(R.id.NombreEventoMarker).text=evento.nombreEvento
        view.findViewById<TextView>(R.id.bubble_title).text=evento.precio.toString()

        var image= view.findViewById<ImageView>(R.id.bubble_image)


        runBlocking {
         var cargar= async {
              Glide.with(context)
                  .load(evento.photo)
                  .into(image)
          }
        cargar.await()
        }
        if(image.drawable==null)
            if(evento.categoria=="OCIO")
                image.setImageResource(R.drawable.ocios)
            else if(evento.categoria=="ESTUDIO")
                image.setImageResource(R.drawable.estudios)
            else if(evento.categoria=="MUSICA")
                image.setImageResource(R.drawable.concierto)
            else image.setImageResource(R.drawable.deportes)
        view.findViewById<TextView>(R.id.bubble_subdescription)
        view.findViewById<TextView>(R.id.bubble_description).text=evento.usuarios.size.toString()+"/"+evento.maxUsuarios.toString()
        return view





    }




}