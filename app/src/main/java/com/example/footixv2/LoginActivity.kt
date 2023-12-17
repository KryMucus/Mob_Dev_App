package com.example.footixv2

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

import android.content.SharedPreferences
import android.widget.Button
import android.widget.EditText


class LoginActivity : AppCompatActivity(){

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Ajoutez ici votre code pour la logique de l'activité

        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        //avant toute chose on vérifie si on est déjà connecté :
        val statusConnection = sharedPreferences.getBoolean("statusConnection",false)

        if (statusConnection){
            //  oN PASSE AU MENU PRINCIPAL
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }





        val loginButton = findViewById<Button>(R.id.login_btn)
        //Pour l'instant pas de SIgn IN donc mot de passe et username généré à la main
        val savedUsername = sharedPreferences.getString("username", "Christophe_Mae")
        val savedPassword =  sharedPreferences.getString("password", "Willemite")
        //On va créer une variable pour vérifier si on est déjà connecté, dans ce cas pas de login

        loginButton.setOnClickListener{

            Log.i("APP : Login", "Bouton de login clické" )


            val enteredUsername = findViewById<EditText>(R.id.username_field).text.toString()
            Log.i("APP : Login", "username récupéré ")
            val enteredPassword = findViewById<EditText>(R.id.password_field).text.toString()
            Log.i("APP : Login", "password récupéré ")

            //SI BON COUPLE USERNAME PASSWORD
            if ( (enteredUsername == savedUsername && enteredPassword == savedPassword)) {
                //Connexion réussie :
                Toast.makeText(this@LoginActivity, "Connexion réussie", Toast.LENGTH_SHORT).show()
                //On edit le statut de Connection
                sharedPreferences.edit().putBoolean("statusConnection", true).apply()
                //  oN PASSE AU MENU PRINCIPAL
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()


            }else{
                //Connexion Echouée :
                Toast.makeText(this@LoginActivity, "Connexion échouée", Toast.LENGTH_SHORT).show()

            }

        }


    }
}