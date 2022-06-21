package app.Ejoin.Activities

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NO_HISTORY
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import app.Ejoin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import utilities.Constants
import utilities.PreferencesManager

class LoginActivity : AppCompatActivity() {
    private lateinit var userPreferences : PreferencesManager
    private lateinit var auth: FirebaseAuth
    private lateinit var email : EditText
    private lateinit var passWord : EditText
    private val db = Firebase.firestore


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginactivity)
        // Initialize Firebase Auth
        auth = Firebase.auth

    }

    override fun onDestroy() {
        super.onDestroy()
        auth.signOut()

    }

    public override fun onStart() {
        super.onStart()

        userPreferences = PreferencesManager(this)
        var logeado : Boolean? = userPreferences.getBoolean(Constants.LOGEADO)
        if(logeado == true){
             startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
        email = findViewById(R.id.UsuarioMail)
        passWord = findViewById(R.id.UsuarioPassword)
        addButtonFuncionality()
    }

    private fun addButtonFuncionality() {

        var esEmpresa = false
        findViewById<Button>(R.id.login).setOnClickListener {
            if(email.text.toString() !="" && passWord.text.toString()!=""){

                auth.signInWithEmailAndPassword(email.text.toString(),passWord.text.toString()).addOnSuccessListener {

                    db.collection(Constants.USERBD).
                    whereEqualTo(Constants.EMAIL,email.text.toString())
                        .get()
                        .addOnSuccessListener {
                                esEmpresa= it.documents[0].getBoolean("esEmpresa")!!
                                userPreferences.putBoolean(Constants.ESEMPRESA,esEmpresa)
                            userPreferences.putString(Constants.USERPHOTO,it.documents[0].getString(Constants.USERPHOTO)!!)
                            userPreferences.putString(Constants.NOMBREUSUARIO, it.documents[0].getString(Constants.NOMBREUSUARIO)!!)
                            userPreferences.putString(Constants.EMAIL,email.text.toString())
                            userPreferences.putString(Constants.USERID,auth.currentUser!!.uid)
                            if(findViewById<CheckBox>(R.id.rememberMe).isChecked){

                                userPreferences.putBoolean(Constants.LOGEADO,true)

                            }
                            else  userPreferences.putBoolean(Constants.LOGEADO,false)

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }

                }.addOnFailureListener {
                    Toast.makeText(this,"Los datos no coinciden",Toast.LENGTH_LONG).show()
                }
            }
            else Toast.makeText(this,"Datos vacios",Toast.LENGTH_LONG).show()

        }
        findViewById<Button>(R.id.registrarse).setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

}