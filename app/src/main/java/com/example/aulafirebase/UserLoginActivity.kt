package com.example.aulafirebase

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aulafirebase.databinding.ActivityUserLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserLoginActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityUserLoginBinding.inflate(layoutInflater)
    }

    private val auth by lazy {
        FirebaseAuth.getInstance()
    }

    private val db by lazy {
        FirebaseFirestore.getInstance()
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

        binding.btnList.setOnClickListener {
            listData()
        }

        binding.btnUpdateDB.setOnClickListener {
            saveOnDb()
        }

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            finish()
        }
    }

    private fun listData() {
        val userId = auth.currentUser?.uid

        if(userId != null){
            val userReference = db.collection("usuarios").document(userId)
            userReference.addSnapshotListener { documentSnapshot, error ->
                val data = documentSnapshot?.data
                if (data != null){
                    val name = data["nome"]
                    val age = data["idade"]

                    val text =  "Nome: $name\n" +
                                "Idade: $age"

                    binding.txtResult.text = text
                }
            }
                /*.get()
                .addOnSuccessListener { documentSnapshot ->
                    val data = documentSnapshot.data
                    if(data != null) {
                        val name = data["nome"]
                        val age = data["idade"]

                        val text =  "Nome: $name\n" +
                                    "Idade: $age"

                        binding.txtResult.text = text
                    }
                }
                .addOnFailureListener {
                    val text = "Não foi possível recuperar dados do banco de dados!"
                    binding.txtResult.text = text
                }*/
        }
    }

    private fun saveOnDb() {
        val userId = auth.currentUser?.uid

        val userName = binding.editTextName.text.toString()
        val userAge = binding.editTextAge.text.toString()

        val data = mapOf(
            "nome" to userName,
            "idade" to userAge
        )

        if(userId != null){
            db
                .collection("usuarios")
                .document(userId)
                .set(data)
        }
    }
}