package com.example.footixv2

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.internal.wait


class BallFragment : Fragment() {
    private lateinit var viewModel: GlobalViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_ball, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        val askButton: Button = view.findViewById(R.id.ask_btn)

        //les animations sont légères et s"éxécutent dans le
        //THREAD principal, car appelées dans onViewCreated
        //Donc ça ne plantera pas
        val trans1Animation = AnimationUtils.loadAnimation(context, R.anim.translation1)
        val trans2Animation = AnimationUtils.loadAnimation(context, R.anim.translation2)
        val trans3Animation = AnimationUtils.loadAnimation(context, R.anim.translation3)
        val fadeInAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_in)
        val fadeOutAnimation = AnimationUtils.loadAnimation(context, R.anim.fade_out)

        //récupération des planètes
        val sphereViolette = view.findViewById<ImageView>(R.id.imageView3)
        val sphereRose = view.findViewById<ImageView>(R.id.imageView4)
        val sphereVerte = view.findViewById<ImageView>(R.id.imageView7)

        //Récupération des anneaux
        val anneauViolet = view.findViewById<ImageView>(R.id.imageView6)
        val anneaueRos= view.findViewById<ImageView>(R.id.imageView5)
        val anneauVert = view.findViewById<ImageView>(R.id.imageView9)

        // Animation de fondue pour les planètes
        sphereViolette.startAnimation(fadeInAnimation)
        sphereRose.startAnimation(fadeInAnimation)
        sphereVerte.startAnimation(fadeInAnimation)

        //Animation de translation pour les anneaux
        anneauViolet.startAnimation(trans1Animation)
        anneaueRos.startAnimation(trans2Animation)
        anneauVert.startAnimation(trans3Animation)



        askButton.setOnClickListener {
            val questionField: EditText = view.findViewById(R.id.question_field)

            if (questionField.text.toString().isBlank()) {
                Toast.makeText(requireContext(), "Please don't waste the ball's magic, only come forward with a heartfelt question", Toast.LENGTH_SHORT).show()
            } else {
                viewModel.question.value = questionField.text.toString()

                // Animation de fade out pour les planètes et les anneaux
                sphereViolette.startAnimation(fadeOutAnimation)
                sphereRose.startAnimation(fadeOutAnimation)
                sphereVerte.startAnimation(fadeOutAnimation)
                anneauViolet.startAnimation(fadeOutAnimation)
                anneaueRos.startAnimation(fadeOutAnimation)
                anneauVert.startAnimation(fadeOutAnimation)

                // Lancer une coroutine pour le délai
                lifecycleScope.launch {
                    delay(2000) // Délai de 2 secondes

                    // Vérification de la connexion à Internet et navigation
                    if (viewModel.isOnline(requireContext())) {
                        findNavController().navigate(R.id.action_ballFragment_to_loadingInternetFragment)
                    } else {
                        findNavController().navigate(R.id.action_ballFragment_to_loadingFragment)
                    }
                }
            }
        }




    }
    }


