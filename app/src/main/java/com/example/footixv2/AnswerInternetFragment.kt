package com.example.footixv2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import okhttp3.OkHttpClient


class AnswerInternetFragment : Fragment() {

    private lateinit var viewModel: GlobalViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        Log.i("APP : Navigation", "Fragment Reponse Internet Crée")

        val answerTextView = view.findViewById<TextView>(R.id.answer_field)

        // On va observer les changements de la réponse
        viewModel.answer.observe(viewLifecycleOwner, Observer { answer ->
            // Mettre à jour le TextView quand la réponse change
            answerTextView.text = answer
            Log.i("APP : Answer fragment", "réponse récupérée : $answer")
        })

        // Gestion de la navigation
        val thanks_btn: Button = view.findViewById(R.id.thanks_btn)
        thanks_btn.setOnClickListener {
            findNavController().navigate(R.id.action_answerInternetFragment_to_ballFragment)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_answer_internet, container, false)
    }


}