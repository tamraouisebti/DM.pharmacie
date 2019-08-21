package com.example.myapplication


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.transition.Slide
import android.transition.TransitionManager
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.places.ui.PlacePicker
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.activity_maps1.*
import org.jetbrains.anko.makeCall
import java.io.IOException


class MapsActivity : AppCompatActivity(), OnMapReadyCallback,  GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(marker: Marker): Boolean {




        // Initialize a new layout inflater instance

        val inflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        // Inflate a custom view using layout inflater
        val view = inflater.inflate(R.layout.another_view,null)

        // Initialize a new instance of popup window
        val popupWindow = PopupWindow(
            view, // Custom view to show in popup window
            LinearLayout.LayoutParams.WRAP_CONTENT, // Width of popup window
            LinearLayout.LayoutParams.WRAP_CONTENT // Window height
        )

        // Set an elevation for the popup window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.elevation = 10.0F
        }


        // If API level 23 or higher then execute the code
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            // Create a new slide animation for popup window enter transition
            val slideIn = Slide()
            slideIn.slideEdge = Gravity.TOP
            popupWindow.enterTransition = slideIn

            // Slide animation for popup window exit transition
            val slideOut = Slide()
            slideOut.slideEdge = Gravity.RIGHT
            popupWindow.exitTransition = slideOut

        }

        // Get the widgets reference from custom view
        val tv = view.findViewById<TextView>(R.id.text_view)
        val buttonPopup = view.findViewById<Button>(R.id.button_popup)
        val imagebutton_map = view.findViewById<ImageButton>(R.id.imageButton_map)
        val imagebutton_facebook = view.findViewById<ImageButton>(R.id.imageButton_facebook)
        val imagebutton_call = view.findViewById<ImageButton>(R.id.imageButton_call)
        // Set click listener for popup window's text view
        tv.setOnClickListener{
            // Change the text color of popup window's text view
            tv.setTextColor(Color.RED)
        }

        // Set a click listener for popup's button widget
        buttonPopup.setOnClickListener{
            // Dismiss the popup window

            popupWindow.dismiss()

        }

        imagebutton_map.setOnClickListener {
            var uri: String  ="http://maps.google.com/maps?&daddr="+marker.position.latitude.toString()+","+marker.position.longitude.toString()
            var intent:Intent  = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
        }

        imagebutton_facebook.setOnClickListener {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("fb://page/678329039013848"))
                startActivity(intent)
            } catch (e: Exception) {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("http://www.facebook.com/678329039013848")))
            }

        }


        imagebutton_call.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this,
                    android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.CALL_PHONE), 4)}

            makeCall("0672634290")

        }

        // Set a dismiss listener for popup window
        popupWindow.setOnDismissListener {
            Toast.makeText(applicationContext,"Popup closed",Toast.LENGTH_SHORT).show()
        }

        popupWindow.setFocusable(true)


        // Finally, show the popup window on app
        TransitionManager.beginDelayedTransition(container)
        popupWindow.showAtLocation(
            container, // Location to display popup window
            Gravity.CENTER, // Exact position of layout to display popup
            0, // X offset
            0 // Y offset
        )

        var  destinationlocation:Location= Location("marker")
        destinationlocation.latitude=marker.position.latitude
        destinationlocation.longitude=marker.position.longitude

        tv.text=marker.title+"distance " +lastLocation.distanceTo(destinationlocation)



        return false
    }

    private lateinit var mMap: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    // 1
    private lateinit var locationCallback: LocationCallback
    // 2
    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false
    private  var liste_ville= arrayListOf<Ville>()
    private  var liste_pharmacie= arrayListOf<pharmacie>()
    private  lateinit var phone_number:String
    lateinit  var position_pharmacie:LatLng




    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)

        setUpMap()
    }


    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_search -> {
                viewflipper.displayedChild=0
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                viewflipper.displayedChild=1
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_profil -> {
                viewflipper.displayedChild=2
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        if (savedInstanceState == null) {
            viewflipper.displayedChild=1
        }
        navView.selectedItemId=R.id.navigation_map
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        add_ville()
        add_pharmacie()
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)

                lastLocation = p0.lastLocation
                placeMarkerOnMap(LatLng(lastLocation.latitude, lastLocation.longitude))

            }
        }




        createLocationRequest()


        val ville = arrayOf(
            "ALGER","ANNABA","ORAN","EL TARF","CONSTATINE",
            "SETIF","TELEMCEN"
        )

        val adapter = ArrayAdapter<String>(
            this, // Context
            android.R.layout.simple_dropdown_item_1line, // Layout
            ville // Array
        )


        // Set the AutoCompleteTextView adapter
        autoComplete.setAdapter(adapter)

        // The minimum number of characters to type to show the drop down
        autoComplete.threshold = 1

        // Set an item click listener for auto complete text view
        autoComplete.onItemClickListener = AdapterView.OnItemClickListener{
                parent,view,position,id->
            val selectedItem = parent.getItemAtPosition(position).toString()
            // Display the clicked item using toast
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(liste_ville.get(id.toInt()).position, 12f))
            Toast.makeText(applicationContext,"Selected : $selectedItem",Toast.LENGTH_SHORT).show()
        }



        // Set a dismiss listener for auto complete text view
    /*    autoComplete.setOnDismissListener {
            Toast.makeText(applicationContext,"Suggestion closed.",Toast.LENGTH_SHORT).show()
        }
*/

        autoComplete.onFocusChangeListener = View.OnFocusChangeListener{
                view, b ->
            if(b){
                // Display the suggestion dropdown on focus
                autoComplete.showDropDown()
            }
        }


        ////////////////////////////////////////////////////////////////






       /* ////////////////////////////////////////////////




        Places.initialize(getApplicationContext(), "AIzaSyB1vpo6ZmLAgoc0-dVaxOIR0j18e-rCcYc")
        val placesClient : PlacesClient = Places.createClient(this)

        val placeId :String  = "ChIJrTLr-GyuEmsRBfy61i59si0"
        val placeFields = Arrays.asList(com.google.android.libraries.places.api.model.Place.Field.ID
            , com.google.android.libraries.places.api.model.Place.Field.NAME)
        val request0 = FetchPlaceRequest.builder(placeId, placeFields).build()

        placesClient.fetchPlace(request0).addOnSuccessListener { response ->
            val place = response.place
        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
                val statusCode = exception.statusCode
                // Handle error with given status code.
            }
        }


        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
// and once again when the user makes a selection (for example when calling fetchPlace()).
        val token = AutocompleteSessionToken.newInstance()
        // Create a RectangularBounds object.
        val bounds = RectangularBounds.newInstance(
            LatLng(-33.880490, 151.184363),
            LatLng(-33.858754, 151.229596)
        )
        // Use the builder to create a FindAutocompletePredictionsRequest.
        val request = FindAutocompletePredictionsRequest.builder()
            // Call either setLocationBias() OR setLocationRestriction().
            //.setLocationRestriction(bounds)

            .setTypeFilter(TypeFilter.ADDRESS)
            .setSessionToken(token)
            .setQuery("annaba")
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            for (prediction in response.autocompletePredictions) {


                Toast.makeText(applicationContext,prediction.getPrimaryText(null).toString(), Toast.LENGTH_LONG).show();
            }
        }.addOnFailureListener { exception ->
            if (exception is ApiException) {
                Toast.makeText(applicationContext,"place not found", Toast.LENGTH_LONG).show()
            }
        }


        autoComplete.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val request = FindAutocompletePredictionsRequest.builder()
                    // Call either setLocationBias() OR setLocationRestriction().
                    //.setLocationRestriction(bounds)

                    .setCountry("dz")
                    .setSessionToken(token)
                    .setQuery(p0.toString())
                    .build()






                placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
                    for (prediction in response.autocompletePredictions) {




                        Toast.makeText(applicationContext,prediction.getFullText(null).toString(), Toast.LENGTH_LONG).show();
                    }
                }.addOnFailureListener { exception ->
                    if (exception is ApiException) {
                        Toast.makeText(applicationContext,"place not found", Toast.LENGTH_LONG).show()
                    }
                }


            }
        })


        /////////////////////////////////////////////////
*/
    }


    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }



        // 1
        mMap.isMyLocationEnabled = true

// 2
        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            // 3
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(location.latitude, location.longitude)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }

    }


    private fun placeMarkerOnMap(location: LatLng) {

        for (i in liste_pharmacie)
        {mMap.addMarker(MarkerOptions().position(i.position).title(i.Address)) }


    }







    companion object {
         const val LOCATION_PERMISSION_REQUEST_CODE = 1
         const val REQUEST_CHECK_SETTINGS = 2
         const val PLACE_PICKER_REQUEST = 3
        const val CALL_PHONE_PERMISSION_REQUEST_CODE = 1

    }


    private fun getAddress(latLng: LatLng): String {
        // 1
        val geocoder = Geocoder(this)
        val addresses: List<Address>?
        val address: Address?
        var addressText = ""

        try {
            // 2
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
            // 3
            if (null != addresses && !addresses.isEmpty()) {
                address = addresses[0]
                for (i in 0 until address.maxAddressLineIndex) {
                    addressText += if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i)
                }
            }
        } catch (e: IOException) {
            Log.e("MainActivity", e.localizedMessage)
        }

        return addressText
    }



    private fun startLocationUpdates() {
        //1
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
            return
        }
        //2
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null /* Looper */)
    }


    private fun createLocationRequest() {
        // 1
        locationRequest = LocationRequest()
        // 2
        locationRequest.interval = 10000
        // 3
        locationRequest.fastestInterval = 5000
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        // 4
        val client = LocationServices.getSettingsClient(this)
        val task = client.checkLocationSettings(builder.build())

        // 5
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(this@MapsActivity,
                        REQUEST_CHECK_SETTINGS)
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
                }
            }
        }
    }




    // 1
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                val place = PlacePicker.getPlace(this, data)
                var addressText = place.name.toString()
                addressText += "\n" + place.address.toString()

                placeMarkerOnMap(place.latLng)
            }
        }

    }

    // 2
    override fun onPause() {
        super.onPause()
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    // 3
    public override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }

    public  fun add_ville() {
        liste_ville.add(Ville("ALGER",LatLng(36.753769, 3.058756),0))
        liste_ville.add(Ville("ANNABA",LatLng(36.900162, 7.765300),1))
        liste_ville.add(Ville("ORAN",LatLng(35.697071, -0.630799),2))
        liste_ville.add(Ville("EL TARF",LatLng(36.767010, 8.313710),3))
        liste_ville.add(Ville("CONSTATINE",LatLng(36.351830,6.611630),4))
        liste_ville.add(Ville( "SETIF",LatLng(36.185360, 5.408680),5))
        liste_ville.add(Ville("TLEMCEN",LatLng(34.883850, -1.314020),6))


    }
    public fun add_pharmacie()
    {
        liste_pharmacie.add(pharmacie("RUE 11 Novembre",9,23,"0672634290",
            true,false,"426253597411506",LatLng(36.784393, 8.381000)))
        liste_pharmacie.add(pharmacie("RUE Abbatoire",9,23,"0672634290",
            true,false,"426253597411506",LatLng(36.780657, 8.379028)))
        liste_pharmacie.add(pharmacie("RUE Batiment",9,23,"0672634290",
            true,false,"426253597411506",LatLng(36.787454, 8.382703)))
    }



}

