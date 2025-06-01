package com.example.aulafirebase.uploads

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aulafirebase.R
import com.example.aulafirebase.databinding.ActivityUploadImageBinding
import com.example.aulafirebase.helper.Permissions
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.io.OutputStream
import java.util.UUID

class UploadImageActivity : AppCompatActivity() {

    private val binding by lazy{
        ActivityUploadImageBinding.inflate(layoutInflater)
    }

    private val storage by lazy {
        FirebaseStorage.getInstance()
    }

    private var actualUri : Uri? = null
    private var actualBitmap : Bitmap? = null

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

    private val openCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){ activityResult ->
        if(activityResult.resultCode == RESULT_OK) {
            actualBitmap = if (Build.VERSION.SDK_INT >= VERSION_CODES.TIRAMISU) {
                activityResult.data?.extras?.getParcelable("data", Bitmap::class.java)
            } else {
                activityResult.data?.extras?.getParcelable("data")
            }
            binding.imgResult.setImageBitmap(actualBitmap)
        }
    }

    private val permissions = listOf(
        android.Manifest.permission.READ_EXTERNAL_STORAGE,
        android.Manifest.permission.CAMERA,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        Permissions.requestPermissions(this, permissions)

        binding.btnGallery.setOnClickListener {
            openGallery.launch("image/*") // Aqui definimos o mime type
        }

        binding.btnUpload.setOnClickListener {
            uploadFromCamera()
        }

        binding.btnCam.setOnClickListener {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            openCamera.launch(intent)
        }
    }

    private fun uploadFromCamera() {

        // fazer a conversão da imagem para um byte array
        val outputStream = ByteArrayOutputStream()
        actualBitmap?.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            outputStream
        )

        if(actualBitmap != null){
            storage
                .getReference("photos")
                .child("travels")
                .child("photo.jpeg")
                .putBytes(outputStream.toByteArray()) // para usarmos uma imagem precisamos empacotar ela com o bytes
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