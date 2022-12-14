package com.lealapps.teste.android.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.lealapps.teste.android.R
import com.lealapps.teste.android.databinding.FragmentHomeBinding
import com.lealapps.teste.android.ui.adapters.ViewPagerAdapter


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        configTabLayout()
        initListeners()
    }

    private fun configTabLayout() {
        val adapter = ViewPagerAdapter(requireActivity())
        binding.viewPager.adapter = adapter

        adapter.addFragment(RoutineFragment(), "Treino")
        adapter.addFragment(TrainingFragment(), "Exercícios")

        binding.viewPager.offscreenPageLimit = adapter.itemCount

        TabLayoutMediator(
            binding.tabs, binding.viewPager
        ) { tab, position ->
            tab.text = adapter.getTitle(
                position
            )
        }.attach()
    }

    private fun initListeners() {
        binding.ibLogout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        auth.signOut()
        findNavController().navigate(R.id.action_homeFragment_to_authentication)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}