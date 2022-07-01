package app.Ejoin.DataClasses

import com.google.firebase.firestore.GeoPoint

class EventoData(
    var id: String="",
    val  nombreEvento : String="",
    val empresa : String="",
    val fecha : String="", //TODO Definir bien,
    val categoria : String="",
    val detalles : String="",
    val precio : Double=0.0,
    val lugar : String="",
    val cordenada : GeoPoint=GeoPoint(0.0,0.0),
    val maxUsuarios : Int=0,
    var usuarios : ArrayList<String> = arrayListOf(),
    var photo : String=""
) {



}