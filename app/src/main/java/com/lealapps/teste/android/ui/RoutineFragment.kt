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
import com.lealapps.teste.android.databinding.FragmentRoutineBinding
import com.lealapps.teste.android.model.RoutineModel
import com.lealapps.teste.android.ui.adapters.RoutineAdapter
import com.lealapps.teste.android.ui.helper.FirebaseHelper

class RoutineFragment : Fragment() {

    private var _binding: FragmentRoutineBinding? = null
    private val binding get() = _binding!!

    private val routineList = mutableListOf<RoutineModel>()
    private lateinit var routineAdapter: RoutineAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutineBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initListeners()
        getRoutines()
        initRecycler()
    }

    private fun initListeners() {
        binding.fabAdd.setOnClickListener {
            val action = HomeFragmentDirections
                .actionHomeFragmentToRoutineFormFragment(null)
            findNavController().navigate(action)
        }
    }

    private fun getRoutines() {
        FirebaseHelper
            .getDatabase()
            .child("routine")
            .child(FirebaseHelper.getIdUser() ?: "")
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {

                        routineList.clear()

                        for (snap in snapshot.children) {
                            val routine = snap.getValue(RoutineModel::class.java) as RoutineModel
                            routineList.add(routine)
                        }

                        binding.tvLoadingInfo.text = ""

                        routineList.reverse()

                    } else {
                        routineList.clear()
                        binding.tvLoadingInfo.text = "Nenhum treino cadastrado"
                    }
                    routineAdapter.notifyDataSetChanged()
                    binding.progressBar.isVisible = false
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        requireContext(),
                        "Ocorreu um erro ao carregar os treinos.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }
            )
    }

    private fun initRecycler() {
        binding.rvRoutine.layoutManager = LinearLayoutManager(requireContext())
        binding.rvRoutine.setHasFixedSize(true)
        routineAdapter = RoutineAdapter(requireContext(), routineList) { routine, selected ->
            optionSelected(routine, selected)
        }
        binding.rvRoutine.adapter = routineAdapter
    }

    private fun optionSelected(routine: RoutineModel, selected: Int) {
        when (selected) {
            RoutineAdapter.SELECT_DELETE -> delete(routine)

            RoutineAdapter.SELECT_EDIT -> {
                val action = HomeFragmentDirections
                    .actionHomeFragmentToRoutineFormFragment(routine)
                findNavController().navigate(action)
            }

        }
    }

    private fun delete(routine: RoutineModel) {
        FirebaseHelper
            .getDatabase()
            .child("routine")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(routine.id)
            .removeValue()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
