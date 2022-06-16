package app.Ejoin.Activities

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
import app.Ejoin.DataClasses.Evento
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
    private lateinit var eventos: ArrayList<Evento>
private lateinit var mapa : GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        mapa = googleMap

        googleMap.isMyLocationEnabled = false
        val sydney = LatLng(-34.0, 151.0)

        when {
            ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                googleMap.isMyLocationEnabled = true
                googleMap.uiSettings.isMyLocationButtonEnabled=true
            }


            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
                requireActivity().requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),1)
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
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
        @JvmStatic fun newInstance(param1: ArrayList<Evento>) =
            GoogleMapFragment().apply {
                eventos=param1
            }
    }

    private fun generarMarker(evento: Evento,map : GoogleMap) {
        val position = LatLng(evento.getCordenada().latitude, evento.getCordenada().longitude)
        map.addMarker(
            MarkerOptions()
                .position(position)
                .icon(BitmapDescriptorFactory.fromResource(setIcon(evento.getCategoria())))

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
        var evento = p0.tag as Evento
       requireActivity().startActivity(Intent(requireActivity(), DatosEventos::class.java).apply {
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

     fun filtrarMapa( eventos : ArrayList<Evento>){
        mapa?.clear()
        eventos.forEach(Consumer { x->
            generarMarker( x,mapa)
        })
    }


}


