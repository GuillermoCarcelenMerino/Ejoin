package app.Ejoin.Activities.View

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
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
    private var rememberMe=false

    private val viewModel : LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_loginactivity)
        email=findViewById(R.id.UsuarioMail)
        passWord=findViewById(R.id.UsuarioPassword)
        // Initialize Firebase Auth
        setObservers()
        userPreferences = PreferencesManager(this)
        rememberMe = userPreferences.getBoolean(Constants.LOGEADO)
        if(rememberMe){
            viewModel.comprobarDatosLogin(userPreferences.getString(Constants.EMAIL)!! ,userPreferences.getString(Constants.Contraseña)!!)
        }
        addButtonFuncionality()
    }

    private fun setObservers() {
        viewModel.usuarioLogeado.observe(this, Observer {
            if (it){
                if(rememberMe){
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                else{
                    viewModel.getUsuario(email.text.toString())
                }
            }

        })
        viewModel.usuario.observe(this, Observer {
            var esEmpresa= it.esEmpresa
            userPreferences.putBoolean(Constants.ESEMPRESA,esEmpresa)
            userPreferences.putString(Constants.USERPHOTO,it.photo)
            userPreferences.putString(Constants.NOMBREUSUARIO, it.name)
            userPreferences.putString(Constants.EMAIL,email.text.toString())
            userPreferences.putString(Constants.Contraseña,passWord.text.toString())
            if(findViewById<CheckBox>(R.id.rememberMe).isChecked){
                userPreferences.putBoolean(Constants.LOGEADO,true)
            }
            else  userPreferences.putBoolean(Constants.LOGEADO,false)

            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }

    private fun addButtonFuncionality() {
        findViewById<TextView>(R.id.RecuperarContraseña).setOnClickListener {
            startActivity(Intent(this, RecuperarPassword::class.java))
        }

        findViewById<Button>(R.id.login).setOnClickListener {
            if(email.text.toString() !="" && passWord.text.toString()!=""){
                viewModel.comprobarDatosLogin(email.text.toString() ,passWord.text.toString())
            }
            else Toast.makeText(this,"Datos vacios",Toast.LENGTH_LONG).show()
              }
        findViewById<TextView>(R.id.registrarse).setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }

}