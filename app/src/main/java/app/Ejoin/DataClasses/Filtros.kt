package app.Ejoin.DataClasses

import android.widget.Button
import android.widget.TextView
import com.google.android.material.chip.Chip
import com.google.android.material.slider.RangeSlider
import com.google.android.material.switchmaterial.SwitchMaterial
import java.time.Instant
import java.util.*

data class Filtros(

    var deportesFilt: Boolean =false,
    var ocioFilt: Boolean = false,
    var estudiosFilt: Boolean = false,
    var musicaFilt: Boolean = false,
    var precioFilt: Boolean = false,
    var valorMaxPrecio: Float = 0.0f,
    var disponibleFilt: Boolean =false,
    var diaCambio: Boolean =false,
    var dia : Int = 0,
    var mes : Int = 0
)


