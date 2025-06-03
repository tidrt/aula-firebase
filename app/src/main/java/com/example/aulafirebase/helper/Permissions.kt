package com.example.aulafirebase.helper

import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class Permissions {
    companion object{

        fun requestPermissions(activity : Activity, listPermissions : List<String>, requestCode : Int){

            // verificar permissoes
            var deniedPermission = mutableListOf<String>()
            listPermissions.forEach { perm ->
                val havePermission = ContextCompat.checkSelfPermission(
                    activity, perm
                ) == PackageManager.PERMISSION_GRANTED

                if(!havePermission){
                    deniedPermission.add(perm)
                }
            }

            // requisitar permissoes somente quando tiver permissoes negadas
            if (deniedPermission.isNotEmpty()) {
                ActivityCompat.requestPermissions(activity, deniedPermission.toTypedArray(), requestCode)
            }
        }
    }
}