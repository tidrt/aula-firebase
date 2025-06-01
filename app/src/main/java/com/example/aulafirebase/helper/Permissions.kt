package com.example.aulafirebase.helper

import android.app.Activity
import androidx.core.app.ActivityCompat

class Permissions {
    companion object{
        fun requestPermissions(activity : Activity, listPermissions : List<String>){
            ActivityCompat.requestPermissions(activity, listPermissions.toTypedArray(), 0)
        }
    }
}