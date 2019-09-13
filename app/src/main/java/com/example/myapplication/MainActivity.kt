package com.example.myapplication

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var user :User

        button_signup.setOnClickListener {
            var intent: Intent = Intent(this,signupActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            applicationContext.startActivity(intent)
        }


        button_login.setOnClickListener {



            if (edittext_password.text.isNotEmpty() && edittext_username.text.isNotEmpty()) {
                loading.visibility = View.VISIBLE

                try {

                    val call = RetrofitService.endpoint.getuser(
                        edittext_username.text.toString(),
                        edittext_password.text.toString()
                    )


                    call.enqueue(object : Callback<List<User>> {
                        override fun onFailure(call: retrofit2.Call<List<User>>, t: Throwable) {


                            loading.visibility = View.GONE
                            Toast.makeText(applicationContext, "Failed to Connect", Toast.LENGTH_LONG).show()


                        }

                        override fun onResponse(
                            call: retrofit2.Call<List<User>>?, response:
                            Response<List<User>>?
                        ) {

                            if (response?.isSuccessful!!) {

                                if (response.body()!!.isEmpty()) {
                                    loading.visibility = View.GONE
                                    Toast.makeText(applicationContext, "Failed to Connect", Toast.LENGTH_LONG).show()
                                } else {
                                    user = response.body()!!.get(0)
                                    val pref = getSharedPreferences("connexion_info", Context.MODE_PRIVATE)
                                    pref.edit { putBoolean("connected", true) }
                                    finish()
                                }
                            }
                        }

                    })


                } catch (e: Exception) {

                    loading.visibility = View.GONE
                    Toast.makeText(applicationContext, "Failed to Connect", Toast.LENGTH_LONG).show()
                }

            }else{
                if (edittext_username.text.isEmpty()) edittext_username.setError("erreur")
                if (edittext_password.text.isEmpty()) edittext_password.setError("erreur")
            }
        }


    }
}
