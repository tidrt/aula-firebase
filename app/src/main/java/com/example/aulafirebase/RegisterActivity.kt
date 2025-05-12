package com.example.aulafirebase

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aulafirebase.databinding.ActivityRegisterBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegisterActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    private val auth by lazy{
        FirebaseAuth.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnFirstRegister.setOnClickListener {
            createUser()
        }
    }

    private fun createUser() {
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextTextPassword.text.toString()

        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val successId = it.user?.uid
            val successEmail = it.user?.email
            showMessage("Sucesso ao Cadastrar Usuário com Id: $successId e Email: $successEmail")
        }.addOnFailureListener {
            val error = it.printStackTrace()
            showMessage("Erro ao Cadastrar Usuário, stacktrace: $error")
        }
    }

    private fun showMessage(s : String){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}