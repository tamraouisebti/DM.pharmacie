package com.example.myapplication

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pharmacies")
data class pharmacie
    (
    @PrimaryKey
    var id_pharmacie: Int,
    var Address : String,
    var horaire_overture : Int,
    var horaire_fermeture : Int,
    var phone_number : String,
    var cnas :Boolean,
    var casnos :Boolean,
    var lien_facebook : String,
    var position_longitude : Float,
    var position_latitude:Float)
  /*  constructor(Address : String,horaire_overture : Int, horaire_fermeture : Int, phone_number : String, cnas :Boolean=false, casnos :Boolean=false
                        , lien_facebook : String, position_longitude : Float,position_latitude:Float
    )
    {
        this.Address=Address
        this.horaire_overture=horaire_overture
        this.horaire_fermeture=horaire_fermeture
        this.phone_number=phone_number
        this.cnas=cnas
        this.casnos=casnos
        this.lien_facebook=lien_facebook
        this.position_longitude=position_longitude
    }

*/

