package com.example.task.ui.auth

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.task.databinding.FragmentRecoverAccountBinding
import com.example.task.helper.BaseFragment
import com.example.task.helper.FirebaseHelper
import com.example.task.helper.initToolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RecoverAccountFragment : BaseFragment() {

    private var _binding: FragmentRecoverAccountBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecoverAccountBinding.inflate(inflater, container, false)
        return _binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initToolbar(binding.toolbar)
        auth = Firebase.auth
        initClicks()
    }

    private fun initClicks() {
        binding.btnSend.setOnClickListener { validateData() }
    }

    private fun validateData() {
        val email = binding.edtEmail.text.toString().trim()

        if (email.isNotEmpty()) {

            hideKeyboard()

            binding.progressBar.isVisible = true

            recoverAccountUser(email)

        } else {
            Toast.makeText(requireContext(), "Informe seu email.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun recoverAccountUser(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        requireContext(),
                        "Enviado link de recuperção para seu e-mail",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.i("INFO_Error", "recoverAccountUser: ${task.exception?.message}")
                    Toast.makeText(
                        requireContext(),
                        FirebaseHelper.validError(task.exception?.message ?: "Error."),
                        Toast.LENGTH_SHORT
                    ).show()
                    Toast.makeText(
                        requireContext(),
                        "Fail:  ${task.exception?.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                binding.progressBar.isVisible = false

            }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}