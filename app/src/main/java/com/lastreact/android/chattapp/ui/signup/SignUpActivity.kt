package com.lastreact.android.chattapp.ui.signup

import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.lastreact.android.chattapp.R
import com.lastreact.android.chattapp.base.BaseActivity
import com.lastreact.android.chattapp.databinding.ActivitySignUpBinding
import com.lastreact.android.chattapp.extensions.afterTextChanged
import com.lastreact.android.chattapp.ui.channels.ChannelsActivity
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.intentFor

class SignUpActivity : BaseActivity<ActivitySignUpBinding>() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        setupSignUp()
        setupTextFields()
    }

    override fun createViewBinding(): ActivitySignUpBinding =
        ActivitySignUpBinding.inflate(layoutInflater)

    private fun setupSignUp() {
        binding.signUpButton.setOnClickListener {
            when {
                !isEmailValid(binding.emailEditText.text.toString()) -> {
                    binding.emailTextInputLayout.error = getString(R.string.email_not_valid)
                }
                !isUsernameValid(binding.userNameEditText.text.toString()) -> {
                    binding.userNameTextInputLayout.error = getString(R.string.invalid_username)
                }
                !isPasswordValidAndMatch(
                    binding.passwordEditText.text.toString(),
                    binding.confirmPasswordEditText.text.toString()
                ) -> {
                    binding.passwordTextInputLayout.error = getString(R.string.password_not_match)
                    binding.confirmPasswordTextInputLayout.error =
                        getString(R.string.password_not_match)
                }
                else -> {
                    showLoading(true)
                    auth.createUserWithEmailAndPassword(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString()
                    ).addOnCompleteListener { task ->
                        showLoading(false)
                        if (task.isComplete && task.isSuccessful) {
                            val updates = UserProfileChangeRequest.Builder()
                                .setDisplayName(binding.userNameEditText.text.toString())
                                .build()
                            auth.currentUser?.updateProfile(updates)
                            startActivity(intentFor<ChannelsActivity>())
                        } else {
                            binding.root.longSnackbar(task.exception?.localizedMessage.toString())
                        }
                    }

                }
            }
        }
    }

    private fun setupTextFields() {
        binding.emailEditText.afterTextChanged {
            if (it.isNotEmpty()) {
                binding.emailTextInputLayout.error = null
            }
        }
        binding.userNameEditText.afterTextChanged {
            if (it.isNotEmpty()) {
                binding.userNameTextInputLayout.error = null
            }
        }
        binding.passwordEditText.afterTextChanged {
            if (it.isNotEmpty()) {
                binding.passwordTextInputLayout.error = null
            }
        }
        binding.confirmPasswordEditText.afterTextChanged {
            if (it.isNotEmpty()) {
                binding.confirmPasswordTextInputLayout.error = null
            }
        }

    }

    private fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() &&
                email.contains("@") &&
                Patterns.EMAIL_ADDRESS.matcher(email).matches()

    }

    private fun isUsernameValid(username: String): Boolean {
        return username.isNotBlank()
    }

    private fun isPasswordValidAndMatch(firstPassword: String, secondPassword: String): Boolean {
        return (firstPassword.length > 7 && secondPassword.length > 7) &&
                (firstPassword == secondPassword)
    }

    private fun showLoading(show: Boolean) {
        binding.loading.visibility = if (show) View.VISIBLE else View.GONE
    }

}