package com.example.aulafirebase.uploads

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aulafirebase.R
import com.example.aulafirebase.databinding.ActivityUploadImageBinding
import com.google.firebase.storage.FirebaseStorage

class UploadImageActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityUploadImageBinding.inflate(layoutInflater)
    }

    private val storage by lazy {
        FirebaseStorage.getInstance()
    }

    private var actualUri : Uri? = null

    private val openGallery = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ){ uri ->
        if(uri != null){
            binding.imgSource.setImageURI(uri)
            actualUri = uri
            Toast.makeText(this, "Imagem selecionada!", Toast.LENGTH_SHORT)
        } else {
            Toast.makeText(this, "Imagem não selecionada!", Toast.LENGTH_SHORT)
        }
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

        binding.btnGallery.setOnClickListener {
            openGallery.launch("image/*") // Aqui definimos o mime type
        }

        binding.btnUpload.setOnClickListener {
            uploadFromGallery()
        }
    }

    private fun uploadFromGallery() {


        // O storage usa uma estrutura de pastas que podemos criar dessa forma

        if(actualUri != null){
            storage
                .getReference("photos") // pasta raíz
                .child("travels") // subpasta
                .child("photo.jpeg") // nome do arquivo
                .putFile(actualUri!!)
                .addOnSuccessListener { taskSnapshot ->
                    Toast.makeText(this, "Sucesso ao fazer o Upload para o Firestore", Toast.LENGTH_SHORT).show()

                    taskSnapshot.metadata?.reference?.downloadUrl?.addOnSuccessListener { url ->
                        Toast.makeText(this, "Url: ${url.toString()}", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Falha ao fazer o Upload para o Firestore", Toast.LENGTH_SHORT).show()
                }
        }
    }
}