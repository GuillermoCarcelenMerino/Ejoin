package app.Ejoin.Activities

import android.app.Activity
import android.os.Bundle
import android.os.StrictMode
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import app.Ejoin.Adapter.RecyclerEventos
import app.Ejoin.DataClasses.Evento
import app.Ejoin.R
import org.osmdroid.config.Configuration
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.MapEventsOverlay
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.gestures.RotationGestureOverlay
import utilities.CustomInfoWindow

class EventFragment : Fragment() {
    private lateinit var eventos: ArrayList<Evento>
    private lateinit var adapter : RecyclerEventos
    private lateinit var recyclerView : RecyclerView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var V=inflater.inflate(R.layout.fragment_event, container, false)
        recyclerView = V.findViewById(R.id.ReciclerEventoFragment)
        initRecycler()
        return V
    }

    companion object {

        @JvmStatic fun newInstance(param1: ArrayList<Evento>) =
            EventFragment().apply {
                this.eventos=param1
            }
    }

    private fun initRecycler() {

        recyclerView.layoutManager = LinearLayoutManager(activity)
        adapter = RecyclerEventos(eventos as ArrayList<Evento>)
        recyclerView.adapter = adapter
    }
}