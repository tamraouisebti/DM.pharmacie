package com.example.myapplication

import androidx.room.Entity

@Entity(tableName = "users")
data class User
    (var id_user :Int,
     var nom :String,
     var prenom:String,
     var numero_social:String,
     var phone_number:String,
     var Adresse:String,
     var password:String,
     var new : Int)
