package app.Ejoin.Activities

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import app.Ejoin.R
import com.google.android.material.chip.Chip
import com.google.android.material.slider.RangeSlider
import com.google.android.material.switchmaterial.SwitchMaterial


class FilterActivity : AppCompatActivity() {

    private var precioCambio = false
    private var distanciaCambio = false

    private lateinit var precioFilt : RangeSlider
    private lateinit var disponibleFilt : SwitchMaterial
    private lateinit var borrarFiltros : TextView
    private lateinit var calendarFilt : DatePicker
    private lateinit var aplicarFilt : Button



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filter)

        initComponents()
    }

    private fun initComponents() {

        precioFilt = findViewById(R.id.precioFilt)
        disponibleFilt = findViewById(R.id.disponiblesFilt)
        borrarFiltros = findViewById(R.id.borrarFiltros)
        calendarFilt = findViewById(R.id.calendarFilt)
        calendarFilt.updateDate(0,0,0)
        aplicarFilt = findViewById(R.id.aplicarFilt)
        listeners()
    }

    private fun listeners() {

        precioFilt.addOnChangeListener { slider, value, fromUser ->
            precioCambio=true
        }



        borrarFiltros.setOnClickListener {
            borrarCambios()


        }
        aplicarFilt.setOnClickListener {

            guardarCambios()

        }




    }

    private fun borrarCambios() {
        precioCambio=false
        precioFilt.values[0]= 0.0F
        distanciaCambio=false
        disponibleFilt.isChecked=false
        calendarFilt.updateDate(0,0,0)
        findViewById<Chip>(R.id.deportesFilt).isChecked=false
        findViewById<Chip>(R.id.ocioFilt).isChecked=false
        findViewById<Chip>(R.id.estudiosFilt).isChecked=false
        findViewById<Chip>(R.id.musicaFilt).isChecked=false
    }

    private fun guardarCambios() {
        val data = Intent()
        data.putExtra("precioCambio",precioCambio)
        if(precioCambio)
            data.putExtra("precio",precioFilt.values[0])
        data.putExtra("distanciaCambio",distanciaCambio)

        if(calendarFilt.dayOfMonth!=0 && calendarFilt.month!=0 && calendarFilt.year!=0){
            data.putExtra("diaCambio",true)
            data.putExtra("dia",calendarFilt.dayOfMonth)
            data.putExtra("mes",calendarFilt.month+1)
        }
        else data.putExtra("diaCambio",false)

        data.putExtra("disponibles",disponibleFilt.isChecked)
        data.putExtra("deportes", findViewById<Chip>(R.id.deportesFilt).isChecked)
        data.putExtra("ocio",findViewById<Chip>(R.id.ocioFilt).isChecked)
        data.putExtra("estudios",findViewById<Chip>(R.id.estudiosFilt).isChecked)
        data.putExtra("musica", findViewById<Chip>(R.id.musicaFilt).isChecked)

        setResult(RESULT_OK, data)
        finish()
    }

    override fun onBackPressed() {
       borrarCambios()
        guardarCambios()
    }

}