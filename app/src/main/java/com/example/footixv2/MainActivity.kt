package com.example.footixv2

import android.content.Context
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import okhttp3.OkHttpClient


class MainActivity : AppCompatActivity() {
    private lateinit var navController : NavController
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var viewModel: GlobalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        //initialistation de la BDD

        val dbHelper = DatabaseHelper(this)
        val db: SQLiteDatabase = dbHelper.writableDatabase



        viewModel = GlobalViewModel(OkHttpClient(), dbHelper)

        //On va proposer une option de logout :

        sharedPreferences = getSharedPreferences("MySharedPref", Context.MODE_PRIVATE)
        //Récupération du bouton
        val log_out_btn = findViewById<ImageButton>(R.id.log_out_btn)
        //Création du message d'alerte
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Confirm Logout")
        builder.setMessage("Are you sure you want to logout?")
        //Gestion des cas :

        //Deconnexion poursuivie
        builder.setPositiveButton("Yes, too much magic for me") { dialog, which ->
            Log.i("APP : Main", "Logout Attempted ")
            //On se déconnecte
            sharedPreferences.edit().putBoolean("statusConnection", false).apply()
            //on clos l'app
            finishAffinity()
        }

        // Bouton pour annuler la déconnexion
        builder.setNegativeButton("No") { dialog, which ->
            Log.i("APP : Main", "Logout Aborted ")
            dialog.dismiss()
        }

        log_out_btn.setOnClickListener {
            Log.i("APP : Main", "Logout Button Clicked ")
            val dialog = builder.create()
            dialog.show()
        }

        val form_btn = findViewById<ImageButton>(R.id.form_btn)
        form_btn.setOnClickListener {
            navController.navigate(R.id.formFragment)
        }

    }
}
