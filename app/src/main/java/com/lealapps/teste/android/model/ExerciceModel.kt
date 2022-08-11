package com.lealapps.teste.android.model

import android.os.Parcelable
import com.lealapps.teste.android.ui.helper.FirebaseHelper
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExerciceModel(
    var id: String = "",
    var nome: String = "",
    var imagem: String = "",
    var observacoes: String = ""
) : Parcelable {
    init {
        this.id = FirebaseHelper.getDatabase().push().key ?: ""
    }
}
