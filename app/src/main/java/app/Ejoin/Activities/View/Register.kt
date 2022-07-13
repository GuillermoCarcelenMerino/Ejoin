package app.Ejoin.Activities.View

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Observer
import app.Ejoin.Activities.View.EventosMain.MainActivity
import app.Ejoin.Activities.ViewModel.RegisterVieweModel
import app.Ejoin.DataClasses.Usuario
import app.Ejoin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikhaellopez.circularimageview.CircularImageView
import utilities.PreferencesManager
import java.io.FileNotFoundException

class Register : AppCompatActivity() {
    private lateinit var nombre: EditText
    private lateinit var correo: EditText
    private lateinit var password: EditText
    private lateinit var password2: EditText
    private lateinit var auth: FirebaseAuth
    private lateinit var userPreferences: PreferencesManager
    private lateinit var imagenButton: CircularImageView
    private var fotoSel: Boolean = false
    private val db = Firebase.firestore
    private lateinit var usuario: Usuario
    private var canBeSaved: Boolean = true
    private lateinit var checkBox : CheckBox
    private lateinit var uri : Uri

    /**
     *
     *
     *
     *
     *
     * */
    private val viewModel : RegisterVieweModel by viewModels()
    /**
     *
     *
     *
     * */

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        asignarObjetos()
        findViewById<Button>(R.id.confirmRegister).setOnClickListener {
            checkuserName()
        }


        viewModel.nombreUsado.observe(this, Observer {

            if(it){
                findViewById<TextView>(R.id.nombreRegisterTextView).setTextColor(Color.RED)
                canBeSaved = false
            }
            else checkDataAndCreateUser()

        })
        viewModel.registrado.observe(this, Observer {

            if(it) {
                findViewById<TextView>(R.id.nombreRegisterTextView).setTextColor(Color.RED)
                canBeSaved = false
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            else { findViewById<ConstraintLayout>(R.id.formulario).visibility= View.VISIBLE
            findViewById<TextView>(R.id.NombreApp).visibility= View.GONE
            findViewById<ProgressBar>(R.id.progresBar).visibility= View.GONE
            findViewById<TextView>(R.id.bienvenido).visibility= View.GONE
            findViewById<TextView>(R.id.espere).visibility= View.GONE
            Toast.makeText(this, "Fallo al introducir usuario", Toast.LENGTH_LONG)
                .show()}

        })

    }



    private fun asignarObjetos() {
        nombre = findViewById(R.id.nombreRegister)
        correo = findViewById(R.id.correoRegister)
        password = findViewById(R.id.passwordRegister)
        password2 = findViewById(R.id.password2Register)
        imagenButton = findViewById(R.id.fotoPerfil)
        checkBox = findViewById(R.id.esEmpresa)
        imagenButton.setOnClickListener {
            selecionarImagen()
        }
        usuario = Usuario()
    }


    private fun checkuserName() {
        if (!nombre.text.toString().equals("")) {
            findViewById<TextView>(R.id.nombreRegisterTextView).setTextColor(Color.BLACK)

            viewModel.comprobarDatosLogin(nombre.text.toString())
        }else {
                findViewById<TextView>(R.id.nombreRegisterTextView).setTextColor(Color.RED)
                canBeSaved = false
                checkDataAndCreateUser()
            }

    }
    private fun checkDataAndCreateUser() {
        if (!Patterns.EMAIL_ADDRESS.matcher(correo.text).matches())//TODO verificacion del correo
        {
            findViewById<TextView>(R.id.correoRegisterTextView).setTextColor(Color.RED)
            canBeSaved = false
        } else findViewById<TextView>(R.id.correoRegisterTextView).setTextColor(Color.BLACK)
        if (!password.text.toString().equals(password2.text.toString())) {
            canBeSaved = false
            findViewById<TextView>(R.id.incorrectPasswordRegis).text =
                "las contraseñas no coinciden"
        } else findViewById<TextView>(R.id.incorrectPasswordRegis).text = ""
        if (password.text.toString().equals("") || password.text.length < 6) {
            canBeSaved = false
            findViewById<TextView>(R.id.passwordRegisterTextView).setTextColor(Color.RED)
        } else findViewById<TextView>(R.id.passwordRegisterTextView).setTextColor(Color.BLACK)
        if (password2.text.toString().equals("")) {
            canBeSaved = false
            findViewById<TextView>(R.id.password2RegisterTextView).setTextColor(Color.RED)
        } else findViewById<TextView>(R.id.password2RegisterTextView).setTextColor(Color.BLACK)
        if (!fotoSel) {
            canBeSaved = false
            Toast.makeText(this, "SELECIONA UNA FOTO DE PERFIL", Toast.LENGTH_LONG).show()
        }

        if (canBeSaved) {
            createUser()
        } else canBeSaved = true

    }

    private fun createUser() {
        findViewById<ConstraintLayout>(R.id.formulario).visibility= View.GONE
        findViewById<TextView>(R.id.NombreApp).visibility= View.VISIBLE
        findViewById<TextView>(R.id.bienvenido).visibility= View.VISIBLE
        findViewById<TextView>(R.id.espere).visibility= View.VISIBLE
        findViewById<ProgressBar>(R.id.progresBar).visibility= View.VISIBLE

        usuario.email=correo.text.toString()
        usuario.name=nombre.text.toString()
        usuario.esEmpresa=checkBox.isChecked
        viewModel.registrar(nombre.text.toString(),usuario,uri)

    }

    private fun selecionarImagen() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(Intent.createChooser(intent, "Seleciona imagen de perfil"), 1)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //Procesar el resultado
        if (data != null) {
             uri = data.data!!
            try{
                var x = contentResolver.openInputStream(uri!!)
                var bitmap = BitmapFactory.decodeStream(x)
                imagenButton.setImageBitmap(bitmap)
                fotoSel=true

            }catch (e : FileNotFoundException){
                Toast.makeText(this, "Error al seleccionar la foto", Toast.LENGTH_LONG).show()            }

        }


    }
}