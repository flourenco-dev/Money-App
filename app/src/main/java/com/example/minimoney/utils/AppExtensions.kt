package com.example.minimoney.utils

import android.content.Context
import android.text.Editable
import android.util.Patterns
import android.view.HapticFeedbackConstants
import android.view.View
import androidx.core.content.ContextCompat

fun Boolean?.get() = this ?: false

fun Int?.get() = this ?: 0

fun Double?.get() = this ?: 0.0

fun String?.get() = this ?: ""

fun View.performHaptic() {
    performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)
}

fun View.setOnClickListenerWithHaptic(block: (v: View) -> Unit) {
    if (!isHapticFeedbackEnabled) {
        isHapticFeedbackEnabled = true
    }
    setOnClickListener {
        performHaptic()
        block.invoke(it)
    }
}

fun Editable?.isValidEmail(): Boolean = this?.let {
    Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()
} ?: false

fun Editable?.getStringText(): String = this?.toString().get()

fun String.isColorHex(): Boolean = startsWith("#") && (length == 7 || length == 9)

fun Context.getCompatColor(resourceId: Int) = ContextCompat.getColor(this, resourceId)