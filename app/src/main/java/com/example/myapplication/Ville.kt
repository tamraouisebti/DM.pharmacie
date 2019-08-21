package com.example.myapplication

import com.google.android.gms.maps.model.LatLng

class Ville {
    lateinit var  nom_ville:String
    lateinit var position : LatLng
    var id : Int

    constructor(nom_ville:String , position : LatLng,id: Int)
     {
         this.id=id
         this.nom_ville=nom_ville
         this.position=position
     }

}