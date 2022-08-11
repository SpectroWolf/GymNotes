package com.lealapps.teste.android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lealapps.teste.android.R
import com.lealapps.teste.android.databinding.FragmentRecoverAccountBinding
import com.lealapps.teste.android.ui.helper.BaseFragment
import com.lealapps.teste.android.ui.helper.FirebaseHelper
import com.lealapps.teste.android.ui.helper.initToolbar
import com.lealapps.teste.android.ui.helper.showBottomSheet


class RecoverAccountFragment : BaseFragment() {

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)

        auth = Firebase.auth

        initListeners()
    }

    private fun initListeners() {

        binding.btnSend.setOnClickListener { validateData() }

    }

    private fun validateData() {
        val email = binding.edtEmail.text.toString().trim()

        if (email.isNotEmpty()) {

            hideKeyboard()

            binding.progressBar.isVisible = true
            recoverAccount(email)
        } else {
            showBottomSheet(message = R.string.text_email_empty_login_fragment)
        }
    }

    private fun recoverAccount(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    showBottomSheet(message = R.string.text_email_success_account_fragment)
                } else {
                    showBottomSheet(
                        message = FirebaseHelper.validError(
                            task.exception?.message ?: ""
                        )
                    )
                    binding.progressBar.isVisible = false
                }
            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }


}