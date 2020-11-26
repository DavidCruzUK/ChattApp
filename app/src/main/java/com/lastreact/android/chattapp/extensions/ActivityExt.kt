package com.lastreact.android.chattapp.extensions

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity


fun AppCompatActivity.hideKeyboard() {
    this.currentFocus?.let {
        val im: InputMethodManager =
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
        im.hideSoftInputFromWindow(it.windowToken, 0)
    }
}