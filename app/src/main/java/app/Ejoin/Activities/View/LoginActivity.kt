package app.Ejoin.Activities.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import app.Ejoin.Activities.View.EventosMain.MainActivity
import app.Ejoin.Activities.ViewModel.LoginViewModel
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


    /**
     *
     *
     *
     *
     *
     * */
    private val viewModel : LoginViewModel by viewModels()
    /**
     *
     *
     *
     * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginactivity)
        // Initialize Firebase Auth
        auth = Firebase.auth

        viewModel.usuarioLogeado.observe(this, Observer {
            var esEmpresa= it.esEmpresa
            userPreferences.putBoolean(Constants.ESEMPRESA,esEmpresa)
            userPreferences.putString(Constants.USERPHOTO,it.photo)
            userPreferences.putString(Constants.NOMBREUSUARIO, it.name)
            userPreferences.putString(Constants.EMAIL,email.text.toString())
            if(findViewById<CheckBox>(R.id.rememberMe).isChecked){

                userPreferences.putBoolean(Constants.LOGEADO,true)

            }
            else  userPreferences.putBoolean(Constants.LOGEADO,false)

            startActivity(Intent(this, MainActivity::class.java))
            finish()

        })


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

        findViewById<Button>(R.id.login).setOnClickListener {
            if(email.text.toString() !="" && passWord.text.toString()!=""){
                viewModel.comprobarDatosLogin(email.text.toString() ,passWord.text.toString())
            }
            else Toast.makeText(this,"Datos vacios",Toast.LENGTH_LONG).show()
              }
        findViewById<Button>(R.id.registrarse).setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

}