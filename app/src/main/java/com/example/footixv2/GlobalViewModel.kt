package com.example.footixv2

import android.content.ContentValues
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.tasks.OnFailureListener
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random


class GlobalViewModel(
    private val client: OkHttpClient = OkHttpClient(), // Injection de dépendance avec valeur par défaut
    private var dbHelper: DatabaseHelper // Pas de valeur par défaut, doit être fourni
) : ViewModel() {


    // un seul View Model pour tous mes fragments
    //Il devra me permettre de gérer mes cycles de vies
    //Comme les rotations de 'écran par exemple et d'assurer une première couche de persistance
    // de données

    //Dans cette variable, je vais stocker la question posée
    var question = MutableLiveData<String>()
    //Ici On stocke la réponse donnée par les boules MAGIQUES
    val answer = MutableLiveData<String>()


    // LA Clée Pour mon API
    val key = MutableLiveData<String>()
    //on va utiliser la bibliothèque OK HTTP
    //l'url de l'api
    val url ="https://api.openai.com/v1/chat/completions"
    //On va aussi créer une variable aléatoire pour choisir une réponse s'il n'y a pas
    // d'internet
    var randInt: Int? = null



    init {
        // Initialisation des données

        // !!!!! INSERER LA CLE ICI !!!!!

        //Valeur de ma Clée API

        key.value = ""
        //initialement, pas de question posée
        question.value = "Donnée initiale"
        //Valeur de la réponse de base
        answer.value = "Petite réponse Zen"
        //On va implémenter 21 possibilités de réponses sans internet
        randInt = Random.nextInt(from = 1, until = 22)
    }

    //On vautiliser une suspend fun pour pouvoir l'appeler dans des coroutines,
    // et ainsi avoir une meilleure gestion des ressources grâce à l'ascynchronicité
    suspend fun getResponse(question: String): String = suspendCoroutine { continuation ->

        //dans syteme on explique à l'api son role
        val requestBody="""{
            "model": "gpt-3.5-turbo",
            "messages": [
            {
               "role": "system",
               "content": "Tu es une boule magique, répond de manière mytérieuse, et équivoque"
            },
            {
               "role": "user",
               "content": "$question"
            }
            ]    
        }""".trimIndent()



        //on construit notre requete au format json
        val request = Request.Builder()
            .url(url)
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization","Bearer ${key.value}")
            .post(requestBody.toRequestBody("application/json".toMediaTypeOrNull()))
            .build()

        Log.d("APP : REQUETE ENVOYEE", requestBody)

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    Log.e("APP : APPEL API", "API Failed", e)
                    continuation.resumeWithException(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    val responseBody = response.body?.string()

                    if (responseBody != null) {
                        Log.v("APP :Réponse API", responseBody)
                        Log.i("API CHAT GPT", " ${response.code} - ${response.message} - ${response}")
                        try {
                            val JSONObject = JSONObject(responseBody)
                            val jsonArray: JSONArray = JSONObject.getJSONArray("choices")
                            val textResult = jsonArray.getJSONObject(0).getJSONObject("message").getString("content")
                            continuation.resume(textResult)
                        } catch (e: Exception) {
                            continuation.resumeWithException(e)
                        }
                    } else {
                        Log.e("API CHAT GPT", "Echec de la Réponse: ${response.code} - ${response.message} - ${response}")
                        continuation.resumeWithException(IOException("Réponse vide"))
                    }
                }
                })
    }


    //Vérification de l'état de la connection à internet :
    //pour savoir qu'elle version on appelle

    fun isOnline(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } else {
            // Pour les versions antérieures à Android Marshmallow
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnected
        }
    }

    //ici :  tout ce qui a attrait à notre bbd sqlite

    fun setDatabaseHelper(databaseHelper: DatabaseHelper) {
        this.dbHelper = databaseHelper
    }


    //récupération des réponses possibles dans la DB SQLITE
    fun fetchAnswerFromDatabase(context: Context) {
        Log.i("BALL MAGIC APP", "APPEL AU FETCH ANSWER ")
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase


        // Requête pour récupérer la réponse basée sur randInt

        //On regénère un randint à chaque fois
        randInt = Random.nextInt(from = 1, until = 22)
        val cursor = db.rawQuery("SELECT response FROM Answer WHERE id = $randInt", null)
        if (cursor.moveToFirst()) {
            val response = cursor.getString(cursor.getColumnIndexOrThrow("response"))
            answer.postValue(response)
        } else {
            answer.postValue("Pas de réponse trouvée.")
        }
        cursor.close()
        db.close()
    }


    //fonction pour stocker les formulaires dans la tablee :

    fun insertFormResponse(email: String, idea: String, context: Context) {
        val dbHelper = DatabaseHelper(context)
        // Obtenez l'instance de la base de données
        val db = dbHelper.writableDatabase

        // Créez un nouveau map de valeurs, où les noms de colonnes sont les clés
        val values = ContentValues().apply {
            put("email", email)
            put("idea", idea)
        }

        // Insérez la nouvelle ligne, retournant l'ID de la ligne nouvellement insérée
        val newRowId = db.insert("FormAnswer", null, values)

        // Log pour le débogage
        Log.i("APP : SQLite Insertion", "Nouvelle ligne insérée : ID = $newRowId")
    }
    data class FormAnswer(val id: Int, val email: String, val idea: String)

    //FONCTION POUR RÉCUPÉRER CES MÊMES FORMULAIRES :
    fun getLastFormAnswers(): List<FormAnswer> {
        val db = dbHelper.readableDatabase
        val answers = mutableListOf<FormAnswer>()

        // Requête pour obtenir les 4 dernières réponses
        val query = "SELECT * FROM FormAnswer ORDER BY id"
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val email = cursor.getString(cursor.getColumnIndexOrThrow("email"))
                val idea = cursor.getString(cursor.getColumnIndexOrThrow("idea"))
                answers.add(FormAnswer(id, email, idea))
            } while (cursor.moveToNext())
        }
        cursor.close()

        return answers
    }

}



