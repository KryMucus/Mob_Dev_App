package com.example.footixv2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import okhttp3.OkHttpClient


class AnswerFragment : Fragment() {


    private lateinit var viewModel: GlobalViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        Log.i("APP : Navigation", "Fragment Reponse (PAS Internet) Crée" )

        //de manière assez drôle, on n'a même pas besoin de lire la question de l'utilisateur
        //Pour lui répondre, (en tout cas sans Internet)
        val answerTextView = view.findViewById<TextView>(R.id.answer_field)
        viewModel.fetchAnswerFromDatabase(requireContext())
        // Observer les changements de la réponse
        viewModel.answer.observe(viewLifecycleOwner) { response ->
            // Mettre à jour le TextView avec la réponse
            answerTextView.text = response
        }

        //gestion de la navigation :
        val thanks_btn: Button = view.findViewById(R.id.thanks_btn)
        thanks_btn.setOnClickListener {
            findNavController().navigate(R.id.action_answerFragment_to_ballFragment)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_answer, container, false)
    }


}