package com.example.myapplication

import com.google.android.gms.maps.model.LatLng

class pharmacie {
     lateinit var Address : String
     var horaire_overture : Int = 0
     var horaire_fermeture : Int =0
     lateinit var phone_number : String
     var cnas :Boolean=false
     var casnos :Boolean=false
     lateinit var lien_facebook : String
     lateinit var position : LatLng

    constructor(Address : String,horaire_overture : Int, horaire_fermeture : Int, phone_number : String, cnas :Boolean=false, casnos :Boolean=false
                        , lien_facebook : String, position : LatLng
    )
    {
        this.Address=Address
        this.horaire_overture=horaire_overture
        this.horaire_fermeture=horaire_fermeture
        this.phone_number=phone_number
        this.cnas=cnas
        this.casnos=casnos
        this.lien_facebook=lien_facebook
        this.position=position
    }



}