package app.Ejoin.DataClasses

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.firebase.firestore.GeoPoint
import java.io.ByteArrayOutputStream
import java.io.Serializable

class Evento : Serializable {

    private var id: String? = null
    private lateinit var nombreEvento : String
    private lateinit var empresa : String
    private lateinit var fecha : String //TODO Definir bien
    private lateinit var categoria : String
    private lateinit var detalles : String
    private lateinit var precio : Number
    private lateinit var lugar : String
    private lateinit var cordenada : GeoPoint
    private var maxUsuarios : Number = 0
    private lateinit var usuarios : ArrayList<String>
    private lateinit var photo : String



    constructor(){}
    constructor(
        id: String?,
        nombreEvento: String,
        empresa: String,
        fecha: String,
        categoria: String,
        detalles: String,
        precio: Number,
        lugar: String,
        cordenada: GeoPoint,
        maxUsuarios: Number,
        usuarios: ArrayList<String>,
        photo : String
    ) {
        this.id = id
        this.nombreEvento = nombreEvento
        this.empresa = empresa
        this.fecha = fecha
        this.categoria = categoria
        this.detalles = detalles
        this.precio = precio
        this.lugar = lugar
        this.cordenada = cordenada
        this.maxUsuarios = maxUsuarios
        this.usuarios = usuarios
        this.photo = photo
    }

    constructor(
        nombreEvento: String,
        empresa: String,
        fecha: String,
        categoria: String,
        detalles: String,
        precio: Number,
        lugar: String,
        cordenada: GeoPoint,
        maxUsuarios: Number,
        usuarios: ArrayList<String>,
        photo : String
    ) {
        this.nombreEvento = nombreEvento
        this.empresa = empresa
        this.fecha = fecha
        this.categoria = categoria
        this.detalles = detalles
        this.precio = precio
        this.lugar = lugar
        this.cordenada = cordenada
        this.maxUsuarios = maxUsuarios
        this.usuarios = usuarios
        this.photo = photo
    }


    fun getId(): String? {
        return id
    }
    fun setId(value:String){
        id=value;
    }
    fun getNombreEvento() : String {
        return nombreEvento
    }
    fun setNombre(value : String){
        nombreEvento = value
    }
    fun getEmpresa() : String {
        return empresa
    }
    fun setEmpresa(value : String){
        empresa = value
    }
    fun getDetalles() : String{
        return detalles
    }
    fun setDetalles(value : String){
        detalles = value
    }
    fun getPrecio() : Number {
        return precio
    }
    fun setPrecio(value : Number){
        precio = value
    }
    fun getLugar() : String{
        return  lugar
    }
    fun setLugar(value : String){
        lugar = value
    }
    fun getFecha() : String{
        return  fecha
    }
    fun setFecha(value : String){
        fecha = value
    }
    fun getCategoria() : String{
        return categoria
    }
    fun setCategoria(value : String){
        categoria = value
    }
    fun getCordenada() : GeoPoint{
        return cordenada
    }
    fun setCordenada(value : GeoPoint){
        cordenada = value
    }
    fun getMaxUsuarios() : Number{
        return maxUsuarios
    }
    fun setMaxUsuarios(value : Number){
        maxUsuarios = value
    }
    fun getusuarios() : ArrayList<String>{
        return usuarios
    }
    fun setUsuarios(value : ArrayList<String>){
        usuarios = value
    }
    fun setPhoto(string : String)
    {
        photo = string
    }
    fun getPhoto() : String {
        return photo
    }


}