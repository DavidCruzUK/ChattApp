/*
 *  Copyright (c) 2020 Razeware LLC
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 *  distribute, sublicense, create a derivative work, and/or sell copies of the
 *  Software in any work that is designed, intended, or marketed for pedagogical or
 *  instructional purposes related to programming, coding, application development,
 *  or information technology.  Permission for such use, copying, modification,
 *  merger, publication, distribution, sublicensing, creation of derivative works,
 *  or sale is expressly withheld.
 *
 *  This project and source code may use libraries or frameworks that are
 *  released under various Open-Source licenses. Use of those libraries and
 *  frameworks are governed by their own individual licenses.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 *
 */

package com.lastreact.android.chattapp.ui.login

import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.lastreact.android.chattapp.R
import com.lastreact.android.chattapp.base.BaseActivity
import com.lastreact.android.chattapp.databinding.ActivityLoginBinding
import com.lastreact.android.chattapp.extensions.afterTextChanged
import com.lastreact.android.chattapp.extensions.hideKeyboard
import com.lastreact.android.chattapp.ui.channels.ChannelsActivity
import com.lastreact.android.chattapp.ui.signup.SignUpActivity
import org.jetbrains.anko.design.longSnackbar
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.longToast

class LoginActivity : BaseActivity<ActivityLoginBinding>() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            startActivity(intentFor<ChannelsActivity>())
        } else {
            setUpLogin()
            setupTextFields()
            binding.signUpButton.setOnClickListener { startSignUpActivity() }
            binding.forgotPasswordTextView.setOnClickListener { forgotPassword() }
        }
    }

    override fun createViewBinding(): ActivityLoginBinding =
        ActivityLoginBinding.inflate(layoutInflater)

    private fun setUpLogin() {
        binding.loginButton.setOnClickListener {
            hideKeyboard()
            when {
                !isEmailValid(binding.emailEditText.text.toString()) -> {
                    binding.emailTextInputLayout.error = getString(R.string.email_not_valid)
                }
                !isPasswordValid(binding.passwordEditText.text.toString()) -> {
                    binding.passwordTextInputLayout.error = getString(R.string.invalid_password)
                }
                else -> {
                    showLoading(true)
                    auth.signInWithEmailAndPassword(
                        binding.emailEditText.text.toString(),
                        binding.passwordEditText.text.toString(),
                    ).addOnCompleteListener { task ->
                        if (task.isComplete && task.isSuccessful) {
                            startActivity(intentFor<ChannelsActivity>())
                            longToast("Welcome ${auth.currentUser?.displayName}")
                        } else {
                            showLoading(false)
                            binding.container.longSnackbar(task.exception?.localizedMessage.toString())
                        }
                    }
                }
            }

        }
    }

    private fun startSignUpActivity() {
        startActivity(intentFor<SignUpActivity>())
    }

    private fun forgotPassword() {
        if (!isEmailValid(binding.emailEditText.text.toString())) {
            binding.emailTextInputLayout.error = getString(R.string.email_not_valid)
        } else {
            showLoading(true)
            auth.sendPasswordResetEmail(binding.emailEditText.text.toString())
                .addOnCompleteListener { task ->
                    if (task.isComplete && task.isSuccessful) {
                        showLoading(false)
                        longToast(getString(R.string.recovery_email_message))
                    } else {
                        showLoading(false)
                        binding.container.longSnackbar(task.exception?.localizedMessage.toString())
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
        binding.passwordEditText.afterTextChanged {
            if (it.isNotEmpty()) {
                binding.emailTextInputLayout.error = null
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.loading.visibility = if (show) View.VISIBLE else View.GONE
    }

    private fun isEmailValid(email: String): Boolean {
        return email.isNotBlank() && email.contains("@")
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 7
    }
}