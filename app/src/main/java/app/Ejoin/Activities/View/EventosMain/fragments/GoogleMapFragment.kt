package app.Ejoin.Activities.View.EventosMain.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context.LOCATION_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import app.Ejoin.Activities.View.DatosEventos
import app.Ejoin.DataClasses.EventoData
import app.Ejoin.R
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import utilities.ventanaGoogle
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.Marker
import utilities.Constants
import java.util.function.Consumer

class GoogleMapFragment : Fragment(), GoogleMap.OnInfoWindowClickListener {
    private lateinit var eventos: ArrayList<EventoData>
    private lateinit var mapa : GoogleMap

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapa = googleMap
        googleMap.isMyLocationEnabled = false

        when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled=true
            }

            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {

            requireActivity().requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
            }
            else -> {
                requireActivity().requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
            }
        }
        eventos.forEach(Consumer { x->
            generarMarker( x,googleMap)
        })
        googleMap.setInfoWindowAdapter(ventanaGoogle(requireActivity()))
        googleMap.setOnInfoWindowClickListener(this);
        googleMap.setMinZoomPreference(3.0f)
        googleMap.setMaxZoomPreference(14.0f)
        googleMap.uiSettings.isZoomControlsEnabled=true
        googleMap.uiSettings.isCompassEnabled=true
        googleMap.uiSettings.isZoomGesturesEnabled=true

    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_google_map, container, false)
    }

    @SuppressLint("WrongConstant")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        var locationManager = requireActivity().getSystemService(LOCATION_SERVICE) as LocationManager
        if(!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            val x = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            startActivity(x)
        }
    }

    companion object {
        @JvmStatic fun newInstance(param1: ArrayList<EventoData>) =
            GoogleMapFragment().apply {
                eventos=param1
            }
    }

    private fun generarMarker(evento: EventoData,map : GoogleMap) {
        val position = LatLng(evento.cordenada.latitude, evento.cordenada.longitude)
        map.addMarker(
            MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromResource(setIcon(evento.categoria)))

        )!!.tag = evento

            }

    private fun setIcon(categoria: String): Int {
        return when (categoria) {

            "DEPORTES" -> return R.drawable.deporte
            "OCIO" -> return R.drawable.ocio
            "MUSICA" -> return R.drawable.musica
            else -> return R.drawable.estudio
        }
    }

    override fun onInfoWindowClick(p0: Marker) {
        var evento = p0.tag as EventoData
       requireActivity().startActivity(Intent(requireActivity(), DatosEventos::class.java).apply {
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

     fun filtrarMapa( eventos : ArrayList<EventoData>){
        mapa?.clear()
        eventos.forEach(Consumer { x->
            generarMarker( x,mapa)
        })
    }


}


