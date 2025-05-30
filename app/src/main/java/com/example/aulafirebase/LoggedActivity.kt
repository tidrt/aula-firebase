package com.example.aulafirebase

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.aulafirebase.databinding.ActivityLoggedBinding
import com.example.aulafirebase.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth

class LoggedActivity : AppCompatActivity() {
    private val binding by lazy {
        ActivityLoggedBinding.inflate(layoutInflater)
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

        binding.btnLoggedSignOut.setOnClickListener {
            auth.signOut()
            finish()
        }
    }
}