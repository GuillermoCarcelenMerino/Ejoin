package utilities

import android.app.Activity
import android.view.View
import android.widget.Button
import android.widget.TextView
import app.Ejoin.R
import org.osmdroid.views.MapView

import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow
import android.widget.Toast
import org.osmdroid.views.overlay.Marker
import android.content.Intent
import androidx.fragment.app.FragmentActivity
import app.Ejoin.Activities.DatosEventos
import app.Ejoin.Activities.Perfil
import app.Ejoin.DataClasses.Evento
import org.osmdroid.views.overlay.infowindow.InfoWindow


class CustomInfoWindow(mapView: MapView?, evento: Evento, context: Activity) :
    MarkerInfoWindow(R.layout.markerlayout, mapView)
{


     private var evento : Evento
    private var context : Activity
    init {
        this.evento=evento
        this.context=context
        mView.findViewById<TextView>(R.id.NombreEventoMarker).text=evento.getNombreEvento()
       mView.findViewById<View>(R.id.bubble_title).setOnClickListener {
            AbrirEvento()
        }
        mView.findViewById<View>(R.id.bubble_image).setOnClickListener {
            AbrirEvento()
        }
        mView.findViewById<View>(R.id.bubble_subdescription).setOnClickListener {
            AbrirEvento()
        }
        mView.findViewById<View>(R.id.bubble_description).setOnClickListener {
            AbrirEvento()
        }

    }


    override fun onOpen(item: Any?) {
        super.onOpen(item)
        mView.findViewById<View>(R.id.bubble_title).visibility =
            View.VISIBLE
    }
   fun AbrirEvento(){
       context.startActivity(Intent(view.context, DatosEventos::class.java).apply {
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
       close()
   }


    }