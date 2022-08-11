package com.lealapps.teste.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.lealapps.teste.android.R
import com.lealapps.teste.android.databinding.FragmentTrainningFormBinding
import com.lealapps.teste.android.model.ExerciceModel
import com.lealapps.teste.android.ui.helper.BaseFragment
import com.lealapps.teste.android.ui.helper.FirebaseHelper
import com.lealapps.teste.android.ui.helper.initToolbar
import com.lealapps.teste.android.ui.helper.showBottomSheet


class TrainingFormFragment : BaseFragment() {

    private val args: TrainingFormFragmentArgs by navArgs()

    private var _binding: FragmentTrainningFormBinding? = null
    private val binding get() = _binding!!

    private lateinit var exercice: ExerciceModel
    private var newExercice: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTrainningFormBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)

        initListeners()
        getArgs()
    }

    private fun initListeners() {
        binding.btnSave.setOnClickListener { validateExercice() }
    }

    private fun getArgs(){
        args.exercice.let {
            if (it != null){
                exercice = it
                configExercice()
            }
        }
    }

    private fun configExercice() {
        newExercice = false
        binding.tvToolbarTitle.text = "Editando exercício"

        binding.edtTrainingName.setText(exercice.nome)
        binding.edtUrlImage.setText(exercice.imagem)
        binding.edtObs.setText(exercice.observacoes)
    }

    private fun validateExercice() {
        val title = binding.edtTrainingName.text.toString().trim()

        if (title.isNotEmpty()) {

            hideKeyboard()

            binding.progressBar.isVisible = true

            if (newExercice) exercice = ExerciceModel()
            exercice.nome = title
            exercice.imagem = binding.edtUrlImage.text.toString().trim()
            exercice.observacoes = binding.edtObs.text.toString().trim()

            saveExercice(exercice)

        } else {
            showBottomSheet(message = R.string.text_tv_empty_field_exercice_form_fragment)
        }
    }

    private fun saveExercice(exercice: ExerciceModel) {
        FirebaseHelper
            .getDatabase()
            .child("exercice")
            .child(FirebaseHelper.getIdUser() ?: "")
            .child(exercice.id)
            .setValue(exercice)
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    if (newExercice) { // Novo exercício
                        findNavController().popBackStack()
                        Toast.makeText(
                            requireContext(),
                            "Exercício salvo com sucesso.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else { // Editando exercício
                        binding.progressBar.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "Exercício atualizada com sucesso.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    binding.progressBar.isVisible = false

                    Toast.makeText(
                        requireContext(),
                        "Erro ao salvar exercício.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                binding.progressBar.isVisible = false

                Toast.makeText(
                    requireContext(),
                    "Erro ao salvar exercício.",
                    Toast.LENGTH_SHORT
                ).show()

            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}