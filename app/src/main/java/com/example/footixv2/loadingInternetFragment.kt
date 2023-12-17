package com.example.footixv2

import android.database.sqlite.SQLiteDatabase

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient


class loadingInternetFragment : Fragment() {


    private lateinit var viewModel: GlobalViewModel



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Réupération du view model
        val dbHelper = DatabaseHelper(requireContext())
        val view = inflater.inflate(R.layout.fragment_loading_internet, container, false)

        Log.i("APP : Loading Page", "Loading Page créée avec succès")

        val db: SQLiteDatabase = dbHelper.writableDatabase

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Réupération du view model
        viewModel = (activity as MainActivity).viewModel
        //Cette fois _i on va faire appel à l'api de ChatGPT
        //Car on a du WIFI !

        Log.i("APP : Loading Page", "Appel à l'API")

        //animation de chargement
        val satelliteAnimation1 =
            AnimationUtils.loadAnimation(context, R.anim.satellite_internet1)
        val satelliteAnimation2 =
            AnimationUtils.loadAnimation(context, R.anim.satellite_internet2)
        val satteliteViolet = view.findViewById<ImageView>(R.id.imageView8)
        val satteliteVert = view.findViewById<ImageView>(R.id.imageView)

        satteliteViolet.startAnimation(satelliteAnimation2)
        satteliteVert.startAnimation(satelliteAnimation1)


        //appel API en utilisant une coroutine
        lifecycleScope.launch {
            try {
                val response = viewModel.question.value?.let { viewModel.getResponse(it) }
                Log.i("FRAGMENT INTERNET LOADING : API CHAT GPT  : Réussite", "$response")
                viewModel.answer.value = response
                // On arrête les animations de chargement une fois la réponse obtenue
                satteliteViolet.clearAnimation()
                satteliteVert.clearAnimation()
                findNavController().navigate(R.id.action_loadingInternetFragment_to_answerInternetFragment)

            } catch (e: Exception) {
                e.message?.let { Log.e("FRAGMENT INTERNET LOADING : API CHAT GPT : Exception ", it) }
                satteliteViolet.clearAnimation()
                satteliteVert.clearAnimation()
                findNavController().navigate(R.id.action_loadingInternetFragment_to_answerInternetFragment)
            }
        }
    }


    private fun navigateToAnswerFragment() {
        findNavController().navigate(R.id.action_loadingInternetFragment_to_answerInternetFragment)
    }
}






