package com.example.samaan.fragments.loginRegister

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.samaan.R
import com.example.samaan.activities.ShoppingActivity
import com.example.samaan.databinding.FragmentLoginBinding
import com.example.samaan.dialog.setupBottomSheetDialog
import com.example.samaan.util.Resource
import com.example.samaan.viewmodel.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginFragment: Fragment(R.layout.fragment_login) {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.RegisterBtn.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        binding.apply {
            LoginBtn.setOnClickListener {
                val email = LoginEmailInput.text.toString().trim()
                val password = LoginPasswordInput.text.toString()

                viewModel.login(email, password)
            }
        }
        binding.ForgotPw.setOnClickListener {
            setupBottomSheetDialog { email -> viewModel.resetPassword(email)
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.resetPassword.collect {
                when (it) {
                    is Resource.Loading -> {
                    }

                    is Resource.Success -> {
                        Snackbar.make(requireView(), "Reset link was sent to your email", Snackbar.LENGTH_LONG).show()
                    }

                    is Resource.Error -> {
                        Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_LONG).show()
                        binding.LoginBtn.revertAnimation()
                    }

                    else -> Unit
                }
            }
        }


            lifecycleScope.launchWhenStarted {
                viewModel.login.collect {
                    when (it) {
                        is Resource.Loading -> {
                            binding.LoginBtn.startAnimation()
                        }

                        is Resource.Success -> {
                            binding.LoginBtn.revertAnimation()
                            Intent(requireActivity(), ShoppingActivity::class.java).also { intent ->
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                            }
                        }

                        is Resource.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                            binding.LoginBtn.revertAnimation()
                        }

                        else -> Unit
                    }
                }
            }
        }
    }