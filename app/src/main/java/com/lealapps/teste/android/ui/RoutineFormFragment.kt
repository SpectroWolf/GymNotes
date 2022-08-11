package com.lealapps.teste.android.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lealapps.teste.android.R
import com.lealapps.teste.android.databinding.FragmentRoutineFormBinding
import com.lealapps.teste.android.model.ExerciceModel
import com.lealapps.teste.android.model.RoutineModel
import com.lealapps.teste.android.ui.helper.BaseFragment
import com.lealapps.teste.android.ui.helper.FirebaseHelper
import com.lealapps.teste.android.ui.helper.initToolbar
import com.lealapps.teste.android.ui.helper.showBottomSheet
import java.time.Instant
import java.time.format.DateTimeFormatter


class RoutineFormFragment : BaseFragment() {

    private val args: RoutineFormFragmentArgs by navArgs()

    private var _binding: FragmentRoutineFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var routine: RoutineModel
    private var newRoutine: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRoutineFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)

        initListeners()
        getArgs()
    }

    private fun getArgs(){
        args.routine.let {
            if (it != null){
                routine = it
                configRoutine()
            }
        }
    }

    private fun configRoutine() {
        newRoutine = false
        binding.tvTitleText.text = "Editando treino"

        binding.edtTrainingName.setText(routine.nome)
        binding.edtDescription.setText(routine.descricao)
    }


    private fun initListeners() {
        binding.btnSave.setOnClickListener { validateRoutine() }
    }

    private fun validateRoutine() {
        val title = binding.edtTrainingName.text.toString().trim()

        if (title.isNotEmpty()) {

            hideKeyboard()

            binding.progressBar.isVisible = true

            if (newRoutine) routine = RoutineModel()
            routine.nome = title
            routine.descricao = binding.edtDescription.text.toString().trim()
            routine.data = DateTimeFormatter.ISO_INSTANT.format(Instant.now())

            saveRoutine(routine)

        } else {
            showBottomSheet(message = R.string.text_tv_empty_field_fragment_training_form)
        }
    }

    private fun saveRoutine(routine: RoutineModel) {
        FirebaseHelper
            .getDatabase()
            .child("routine")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(routine.id)
            .setValue(routine)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    if (newRoutine) { // Novo exercício
                        findNavController().popBackStack()
                        Toast.makeText(
                            requireContext(),
                            "Treino salvo com sucesso.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else { // Editando exercício
                        binding.progressBar.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "Treino atualizada com sucesso.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    binding.progressBar.isVisible = false

                    Toast.makeText(
                        requireContext(),
                        "Erro ao salvar treino.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                binding.progressBar.isVisible = false

                Toast.makeText(
                    requireContext(),
                    "Erro ao salvar treino.",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}