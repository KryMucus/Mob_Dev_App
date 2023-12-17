package com.example.footixv2

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText

import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient


class FormFragment : Fragment() {
    private lateinit var viewModel: GlobalViewModel

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Accès à viewModel à partir de MainActivity
        viewModel = (activity as MainActivity).viewModel
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_form, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = view.findNavController()


        val submit_btn = view.findViewById<Button>(R.id.submit_btn)
        val back_btn = view.findViewById<Button>(R.id.back_btn)

        submit_btn.setOnClickListener {
            Log.i("APP : Form", "Form SUBMITTED")
            val emailEditText = view.findViewById<EditText?>(R.id.editTextEmail)
            val ideaEditText = view.findViewById<EditText?>(R.id.editTextIdeas)

            val email = emailEditText?.text.toString().trim()
            val idea = ideaEditText?.text.toString().trim()

            // Vérifiez que les champs ne sont pas vides
            if (email.isEmpty() || idea.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Form Submitted.", Toast.LENGTH_SHORT).show()
                // Utilisation d'une coroutine pour l'insertion des données
                lifecycleScope.launch {
                    //stockage de la réponse dans la BDD
                    viewModel.insertFormResponse(email, idea, requireContext())
                }
            }
        }


        back_btn.setOnClickListener {
            findNavController().navigate(R.id.action_formFragment_to_ballFragment)
        }

        }


    }

