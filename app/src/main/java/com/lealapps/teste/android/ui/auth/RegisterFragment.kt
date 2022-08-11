package com.lealapps.teste.android.ui.auth

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lealapps.teste.android.R
import com.lealapps.teste.android.databinding.FragmentRegisterBinding
import com.lealapps.teste.android.ui.helper.BaseFragment
import com.lealapps.teste.android.ui.helper.FirebaseHelper
import com.lealapps.teste.android.ui.helper.initToolbar
import com.lealapps.teste.android.ui.helper.showBottomSheet


class RegisterFragment : BaseFragment() {

    private var _binding: FragmentRegisterBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initToolbar(binding.toolbar)

        auth = Firebase.auth

        initListeners()

    }

    private fun validateData() {
        val email = binding.edtEmail.text.toString().trim()
        val password = binding.edtPassword.text.toString().trim()

        if (email.isNotEmpty()) {
            if (password.isNotEmpty()) {

                hideKeyboard()

                binding.progressBar.isVisible = true
                registerUser(email, password)

            } else {
                showBottomSheet(message = R.string.text_password_empty_login_fragment)
            }
        } else {
            showBottomSheet(message = R.string.text_email_empty_login_fragment)
        }

    }

    private fun initListeners() {
        binding.btnRegister.setOnClickListener { validateData() }
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    findNavController().navigate(R.id.action_global_homeFragment)
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