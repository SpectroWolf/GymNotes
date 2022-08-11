package com.lealapps.teste.android.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lealapps.teste.android.databinding.ItemExerciceBinding
import com.lealapps.teste.android.model.ExerciceModel

class ExerciceAdapter(
    private val context: Context,
    private val exercicesList: List<ExerciceModel>,
    val exerciceSelected: (ExerciceModel, Int) -> Unit
) : RecyclerView.Adapter<ExerciceAdapter.MyViewHolder>() {

    companion object {
        val SELECT_EDIT: Int = 1
        val SELECT_DELETE: Int = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExerciceAdapter.MyViewHolder {
        return MyViewHolder(
            ItemExerciceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ExerciceAdapter.MyViewHolder, position: Int) {
        val exercice = exercicesList[position]

        holder.binding.tvTitleExercice.text = exercice.nome
        holder.binding.tvTextDescription.text = exercice.observacoes

        holder.binding.ivDelete.setOnClickListener { exerciceSelected(exercice, SELECT_DELETE)}
        holder.binding.ivEdit.setOnClickListener { exerciceSelected(exercice, SELECT_EDIT)}
    }

    override fun getItemCount() = exercicesList.size

    inner class MyViewHolder(val binding: ItemExerciceBinding) :
        RecyclerView.ViewHolder(binding.root)


}