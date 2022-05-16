package app.Ejoin.Activities

import android.app.Activity
import android.graphics.drawable.Drawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.Ejoin.DataClasses.Evento
import app.Ejoin.R
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import java.util.function.Consumer
import utilities.CustomInfoWindow
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy
import androidx.core.graphics.drawable.toDrawable
import org.osmdroid.events.MapEventsReceiver
import org.osmdroid.views.overlay.infowindow.InfoWindow
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay








// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "eventos"

class MapFragment : Fragment(), MapEventsReceiver {
    private lateinit var eventos: ArrayList<Evento>
    private lateinit var  map : MapView


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var V=inflater.inflate(R.layout.fragment_map, container, false)
        map = V.findViewById(R.id.mapas)
        createEventMap()
        return V
    }

    private fun createEventMap() {
        val policy = ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        Configuration.getInstance().setUserAgentValue("EjoinUser");
        map.setMultiTouchControls(true);

        //add rotations to the map
        var mRotationGestureOverlay = RotationGestureOverlay(context, map)
        mRotationGestureOverlay.setEnabled(true)
        map.getOverlays().add(mRotationGestureOverlay)


        map.minZoomLevel=4.0

        val startPoint = GeoPoint(20.0, 20.0)
        val mapController = map.controller

        mapController.setZoom(15)
        mapController.setCenter(startPoint)
        val mapEventsOverlay = MapEventsOverlay(activity, this)
        map.getOverlays().add(0, mapEventsOverlay);

        eventos.forEach(Consumer { x->
            generarMarker( x)
        })

        map.invalidate();
    }

    private fun generarMarker(x: Evento) {
        val eventMarker = Marker(map)
        eventMarker.setInfoWindow(CustomInfoWindow(map,x,activity as Activity))
        eventMarker.setPosition(GeoPoint(x.getCordenada().latitude,x.getCordenada().longitude) )
        eventMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        eventMarker.title=x.getPrecio().toString()
        eventMarker.snippet=x.getFecha()
        eventMarker.subDescription=x.getusuarios().size.toString()+"/"+x.getMaxUsuarios().toString()
        eventMarker.image= R.drawable.chat.toDrawable() //TODO meter imagen
        eventMarker.setAnchor(0f,0f)
        setIcon(x.getCategoria(),eventMarker)


        map.overlays.add(eventMarker)    }

    private fun setIcon(categoria: String, eventMarker: Marker) {
        when (categoria){

            "DEPORTES"->eventMarker.icon=resources.getDrawable(R.drawable.deporte)
            "OCIO"->eventMarker.icon=resources.getDrawable(R.drawable.ocio)
            "MUSICA"->eventMarker.icon=resources.getDrawable(R.drawable.musica)
            "ESTUDIO"->eventMarker.icon=resources.getDrawable(R.drawable.estudio)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MapFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic fun newInstance(param1: ArrayList<Evento>) =
            MapFragment().apply {
                eventos=param1
            }
    }

    override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {

        InfoWindow.closeAllInfoWindowsOn(map);
        return true

    }


    override fun longPressHelper(p: GeoPoint?): Boolean {
        return false
    }



}