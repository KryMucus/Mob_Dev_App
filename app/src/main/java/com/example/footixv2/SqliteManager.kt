package com.example.footixv2

//Ce fichier à pour but de gérer la relation avec la base de données SQLITE
//Qui devra permettre de gérer la persistance des données


//en L'occurence, je veux stocker les 21 réponses possibles en l'absence d'internet;
//Et peut-etre un historique de question réponse si j'ai le temps

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import okhttp3.RequestBody


//RENDUE OPEN pour pouvoir la mocker et la tester
open class  DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {


    companion object {
        private const val DATABASE_NAME = "ball.db"
        private const val DATABASE_VERSION = 4
    }

    //J'avais un problème de table non créee, et les logs suivant n'apparaissaient jamais,
    //EN fait, je dois juste Upgrade ma DB, chose que je ne faisait pas auparavant,



    override fun onCreate(db: SQLiteDatabase?) {
        // Ici on crée les tables, tels des ébénistes

        Log.i("APP : SQLITE DataBase", "Database Créée" )

        //Création de la table des réponses dans laquelle piocher si pas de wifi
        db?.execSQL("""
            CREATE TABLE IF NOT EXISTS Answer (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                response TEXT NOT NULL
            )
        """.trimIndent())
        Log.i("APP : SQLITE DataBase", "Table Answer Créée" )




    }



    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        // Gérer les mises à jour de la base de données
        if (oldVersion < newVersion) {

            //Liste des réponses en questions...
            //Je me suis fait un peu plaisir
            val responses = listOf(
                "Les étoiles brillent favorablement pour vous.",
                "Un sombre présage enveloppe votre destinée.",
                "La brume mystique suggère un avenir incertain.",
                "Les esprits murmurent des signes de chance.",
                "Le destin chuchote des malheurs inévitables.",
                "Des ombres de doute planent sur cette perspective.",
                "Le silence de la nuit promet de bonnes nouvelles.",
                "Un ancien sortilège parle de succès proche.",
                "Une réponse se cache dans les ténèbres, attendez la lumière.",
                "Un présage favorable éclaire votre chemin.",
                "Le vent porte des nouvelles de succès inattendus.",
                "Les augures révèlent des défis à venir.",
                "Les voix du passé avertissent d éviter ce choix.",
                "Le voile mystique s éclaircit, promettant succès.",
                "Les secrets anciens révèlent une issue heureuse.",
                "La lune sourit à vos intentions.",
                "La vérité se révèle favorable pour vos projets.",
                "Le temps apportera prospérité et bonheur.",
                "Un frisson de fortune parcourt l air autour de vous.",
                "Les astres annoncent une période de joie.",
                "Un destin lumineux vous attend, malgré les ombres."
            )


            //Insertion de ces réponses dans la table ANSWER nouvellement créée
            responses.forEach { response ->
                db?.execSQL("INSERT INTO Answer (response) VALUES ('$response')")
            }
            Log.i("APP : SQLITE DataBase", "Table Answer Remplie")


            //On va faire des tables pour stocker les forms  :
            db?.execSQL("""
            CREATE TABLE IF NOT EXISTS FormAnswer (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT NOT NULL,
                idea TEXT NOT NULL
            )
        """.trimIndent())
            Log.i("APP : SQLITE DataBase", "Table FormAnswer Créée" )

        }


    }}



