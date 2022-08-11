package com.lealapps.teste.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.lealapps.teste.android.R
import com.lealapps.teste.android.databinding.FragmentTrainingBinding
import com.lealapps.teste.android.model.ExerciceModel
import com.lealapps.teste.android.ui.adapters.ExerciceAdapter
import com.lealapps.teste.android.ui.adapters.RoutineAdapter
import com.lealapps.teste.android.ui.helper.FirebaseHelper

class TrainingFragment : Fragment() {

    private var _binding: FragmentTrainingBinding? = null
    private val binding get() = _binding!!

    private val exerciceList = mutableListOf<ExerciceModel>()
    private lateinit var exerciceAdapter: ExerciceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        getExercices()
        initRecycler()
    }

    private fun initListeners() {
        binding.fabAdd.setOnClickListener {
            val action = HomeFragmentDirections
            .actionHomeFragmentToTrainingFormFragment(null)
            findNavController().navigate(action)
        }
    }

    private fun getExercices() {
        FirebaseHelper
            .getDatabase()
            .child("exercice")
            .child(FirebaseHelper.getIdUser() ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        exerciceList.clear()

                        for (snap in snapshot.children) {
                            val exercice = snap.getValue(ExerciceModel::class.java) as ExerciceModel
                            exerciceList.add(exercice)
                        }

                        binding.tvLoadingInfo.text = ""

                        exerciceList.reverse()

                    } else {
                        exerciceList.clear()
                        binding.tvLoadingInfo.text = "Nenhum exercício cadastrado"
                    }
                    binding.progressBar.isVisible = false
                    exerciceAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um erro ao carregar os exercícios.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            )
    }

    private fun initRecycler(){
        binding.rvTrainning.layoutManager = LinearLayoutManager(requireContext())
        binding.rvTrainning.setHasFixedSize(true)
        exerciceAdapter = ExerciceAdapter(requireContext(),exerciceList) { exercice, selected ->
            optionSelected(exercice, selected)
        }
        binding.rvTrainning.adapter = exerciceAdapter
    }

    private fun optionSelected(exercice: ExerciceModel, selected: Int){
        when (selected) {
            ExerciceAdapter.SELECT_DELETE -> delete(exercice)

            RoutineAdapter.SELECT_EDIT -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToTrainingFormFragment(exercice)
                findNavController().navigate(action)
            }

        }
    }

    private fun delete(exercice: ExerciceModel){
        FirebaseHelper
            .getDatabase()
            .child("exercice")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(exercice.id)
            .removeValue()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}