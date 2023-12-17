package com.example.footixv2


import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import okhttp3.OkHttpClient


class LoadingFragment : Fragment() {
    private lateinit var viewModel: GlobalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_loading, container, false)
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel

        //on va faire tourner les satelittes pour indiquer le chargement

        val satelliteAnimation2 = AnimationUtils.loadAnimation(context, R.anim.rotation_satelite_part2)
        val satelliteAnimation3 = AnimationUtils.loadAnimation(context, R.anim.rotation_satelite_part3)
        val sattelite = view.findViewById<ImageView>(R.id.imageView8)
        val sattelite2 = view.findViewById<ImageView>(R.id.imageView)

        sattelite.startAnimation(satelliteAnimation3)
        sattelite2.startAnimation(satelliteAnimation2)



        Handler(Looper.getMainLooper()).postDelayed({
            navigateToAnswerFragment()
        }, 3500) // Délai en millisecondes

    }

        private fun navigateToAnswerFragment() {
        findNavController().navigate(R.id.action_loadingFragment_to_answerFragment)
    }



}