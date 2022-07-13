package app.Ejoin.Activities.View

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import app.Ejoin.Activities.ViewModel.PerfilViewModel
import app.Ejoin.Activities.ViewModel.RecuperarViewModel
import app.Ejoin.R
import app.Ejoin.databinding.ActivityRecuperarPasswordBinding
import com.google.firebase.auth.FirebaseAuth

class RecuperarPassword : AppCompatActivity() {
    private lateinit var binding : ActivityRecuperarPasswordBinding

    private val viewModel : RecuperarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRecuperarPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObserver()
        binding.continuar.setOnClickListener {
                x->
            if(!binding.emailChange.text.isNullOrEmpty()){
                viewModel.emailExiste(binding.emailChange.text.toString())
            }
        }
    }

    private fun setObserver() {
        viewModel.usuarioExiste.observe(this, Observer {
            if(it)
                FirebaseAuth.getInstance().sendPasswordResetEmail(binding.emailChange.text.toString())
                    .addOnSuccessListener {
                        Toast.makeText(this,"Se ha enviado un mensaje a su correo", Toast.LENGTH_LONG).show()
                    }
            else Toast.makeText(this,"Email no registrado",Toast.LENGTH_LONG).show()
        })
    }
}