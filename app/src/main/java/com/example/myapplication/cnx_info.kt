package com.example.myapplication

class cnx_info {
    var connected:Boolean=false
    lateinit var  addresse :String
    lateinit var phone_number :String
    lateinit var securite_social:String
    lateinit var nom:String
    lateinit var prenom:String
    constructor( connected:Boolean,  addresse :String, phone_number :String, securite_social:String
            , nom:String, prenom:String)
    {
        this.connected=connected
        this.addresse=addresse
        this.nom=nom
        this.phone_number=phone_number
        this.securite_social=securite_social
        this.prenom=prenom

    }
}