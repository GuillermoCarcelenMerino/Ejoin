package utilities

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import utilities.Constants.Companion.PREFERENCES

class PreferencesManager {
     var sharedPreferenceManager : SharedPreferences

    constructor(context: Context){
        sharedPreferenceManager = context.getSharedPreferences(PREFERENCES, MODE_PRIVATE)
    }

    fun putBoolean (key : String,value : Boolean){
        var editor = sharedPreferenceManager.edit()
        editor.putBoolean(key,value)
        editor.apply()
    }
    fun putString (key : String,value : String){
        var editor = sharedPreferenceManager.edit()
        editor.putString(key,value)
        editor.apply()
    }
    fun putInt (key : String,value : Int){
        var editor = sharedPreferenceManager.edit()
        editor.putInt(key,value)
        editor.apply()
    }
    fun putFloat (key : String,value : Float){
        var editor = sharedPreferenceManager.edit()
        editor.putFloat(key,value)
        editor.apply()
    }

    fun getBoolean (key : String) : Boolean{
        return sharedPreferenceManager.getBoolean(key,false)

    }
    fun getString (key : String) : String? {
        return sharedPreferenceManager.getString(key,null)

    }
    fun getInt (key : String) : Int{
        return sharedPreferenceManager.getInt(key,null!!)

    }
    fun getFloat (key : String) : Float{
       return sharedPreferenceManager.getFloat(key,null!!)
    }

}