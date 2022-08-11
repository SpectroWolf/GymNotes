package com.lealapps.teste.android.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lealapps.teste.android.databinding.ItemRoutineBinding
import com.lealapps.teste.android.model.RoutineModel

class RoutineAdapter(
    private val context: Context,
    private val routineList: List<RoutineModel>,
    val routineSelected: (RoutineModel, Int) -> Unit
) : RecyclerView.Adapter<RoutineAdapter.MyViewHolder>() {

    companion object {
        val SELECT_EDIT: Int = 1
        val SELECT_DELETE: Int = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoutineAdapter.MyViewHolder {
        return MyViewHolder(
            ItemRoutineBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RoutineAdapter.MyViewHolder, position: Int) {
        val routine = routineList[position]

        holder.binding.tvTitleRoutine.text = routine.nome
        holder.binding.tvDescription.text = routine.descricao

        holder.binding.ivDelete.setOnClickListener { routineSelected(routine, SELECT_DELETE)}
        holder.binding.ivEdit.setOnClickListener { routineSelected(routine, SELECT_EDIT)}
    }

    override fun getItemCount() = routineList.size

    inner class MyViewHolder(val binding: ItemRoutineBinding) :
        RecyclerView.ViewHolder(binding.root)


}