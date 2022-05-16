package app.Ejoin.Activities

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import app.Ejoin.DataClasses.Usuarios
import app.Ejoin.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.mikhaellopez.circularimageview.CircularImageView
import utilities.Constants
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
    private lateinit var usuario: Usuarios
    private var canBeSaved: Boolean = true
    private lateinit var checkBox : CheckBox


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        auth = Firebase.auth
        asignarObjetos()
        findViewById<Button>(R.id.confirmRegister).setOnClickListener {
            checkuserName()
        }


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
        usuario = Usuarios()
    }


    private fun checkuserName() {
        if (!nombre.text.toString().equals("")) {
            findViewById<TextView>(R.id.nombreRegisterTextView).setTextColor(Color.BLACK)
            db.collection(Constants.USERBD).
            whereEqualTo(Constants.NOMBREUSUARIO,nombre.text.toString())
                .get()
                .addOnSuccessListener {
                    if (it.documents.isEmpty()){
                        checkDataAndCreateUser()
                    }
                    else {
                        findViewById<TextView>(R.id.nombreRegisterTextView).setTextColor(Color.RED)
                        canBeSaved = false
                    }

                }
                .addOnFailureListener {
                    findViewById<TextView>(R.id.nombreRegisterTextView).setTextColor(Color.RED)
                    canBeSaved = false
                    checkDataAndCreateUser()
                }

        } else {
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
        auth.createUserWithEmailAndPassword(correo.text.toString(), password.text.toString())
            .addOnSuccessListener { task ->
                /*
                    creamos nuevo usuario con clase user
                    +/
                     */

                val user = auth.currentUser
                usuario.setEmail(user?.email.toString())
                usuario.setId(user?.uid.toString())
                usuario.setName(nombre.text.toString())
                usuario.setEsEmpresa(checkBox.isChecked)

                db.collection(Constants.USERBD)
                    .add(usuario)
                    .addOnSuccessListener { documentReference ->
                        //guardar en preferences
                        userPreferences = PreferencesManager(this)
                        userPreferences.putString(Constants.EMAIL,usuario.getEmail())
                        userPreferences.putBoolean(Constants.LOGEADO, true)
                        userPreferences.putString(Constants.USERID, auth.currentUser!!.uid)
                        userPreferences.putBoolean(Constants.ESEMPRESA, usuario.getEsEmpresa())
                        userPreferences.putString(Constants.USERPHOTO, usuario.getPhoto())
                        userPreferences.putString(Constants.NOMBREUSUARIO, usuario.getName())


                        if(usuario.getEsEmpresa())
                        {
                            startActivity(Intent(this, EmpresaActivity::class.java))
                        }
                        else startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Fallo al introducir usuario", Toast.LENGTH_LONG)
                            .show()
                    }
                //TODO remplazar vista con un progressbar


            }.addOnFailureListener {
                val errorCode = it.message
                Toast.makeText(this, errorCode, Toast.LENGTH_LONG).show()

            }
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
            val uri = data.data
            try{
                var x = contentResolver.openInputStream(uri!!)
                var bitmap = BitmapFactory.decodeStream(x)
                imagenButton.setImageBitmap(bitmap)
                usuario.encodeImage(bitmap)
                fotoSel=true

            }catch (e : FileNotFoundException){
                Toast.makeText(this, "Error al seleccionar la foto", Toast.LENGTH_LONG).show()            }

        }


    }
}