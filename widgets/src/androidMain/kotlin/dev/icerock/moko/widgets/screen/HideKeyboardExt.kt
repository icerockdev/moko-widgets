package dev.icerock.moko.widgets.screen


import android.app.Activity
import android.view.inputmethod.InputMethodManager

actual fun Screen<*>.hideKeyboard() {
    requireActivity().currentFocus.run {
        val imm =
            requireActivity().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }
}