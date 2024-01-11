package com.example.samaan.fragments.loginRegister

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.samaan.R
import com.example.samaan.data.User
import com.example.samaan.databinding.FragmentRegisterBinding
import com.example.samaan.util.RegisterValidation
import com.example.samaan.util.Resource
import com.example.samaan.util.validateEmail
import com.example.samaan.viewmodel.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

private val TAG = "RegisterFragment"

@Suppress("DEPRECATION")
@AndroidEntryPoint
class RegisterFragment: Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.LoginBtn.setOnClickListener {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
        }

        binding.apply {
            RegisterBtn.setOnClickListener {
                val user = User(
                    FirstNameInputRegister.text.toString().trim(),
                    LastNameInputRegister.text.toString().trim(),
                    RegisterEmailInput.text.toString().trim(),
                )
                val password = RegisterPasswordInput.text.toString()
                viewModel.createUserWithEmailAndPassword(user, password)
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.register.collect{
                when(it){
                    is Resource.Loading -> {
                        binding.RegisterBtn.startAnimation()
                    }
                    is Resource.Success -> {
                        Log.d("test",it.message.toString())
                        binding.RegisterBtn.revertAnimation()
                    }
                    is Resource.Error -> {
                        Log.d(TAG,it.message.toString())
                        binding.RegisterBtn.revertAnimation()
                    }
                    else -> Unit
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            viewModel.validation.collect {
                validation->
                if(validation.email is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.RegisterEmailInput.apply {
                            requestFocus()
                            error= validation.email.message
                        }
                    }
                }
                if(validation.password is RegisterValidation.Failed){
                    withContext(Dispatchers.Main){
                        binding.RegisterPasswordInput.apply {
                            requestFocus()
                            error= validation.password.message
                        }
                    }
                }
            }
        }
    }
}