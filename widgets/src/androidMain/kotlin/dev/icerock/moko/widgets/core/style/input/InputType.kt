/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core.style.input

import android.widget.EditText
// TODO move to separated additions module to reduce external dependencies count in core
import com.redmadrobot.inputmask.MaskedTextChangedListener

actual interface InputType {

    fun applyTo(editText: EditText)

    actual companion object
}

actual fun InputType.Companion.plain(mask: String?): InputType {
    return object : InputType {
        override fun applyTo(editText: EditText) {
            editText.applyMask(
                platformInputType = android.text.InputType.TYPE_CLASS_TEXT +
                        android.text.InputType.TYPE_TEXT_VARIATION_NORMAL,
                mask = mask
            )
        }
    }
}

actual fun InputType.Companion.digits(mask: String?): InputType {
    return object : InputType {
        override fun applyTo(editText: EditText) {
            editText.applyMask(
                platformInputType = android.text.InputType.TYPE_CLASS_NUMBER,
                mask = mask
            )
        }
    }
}

actual fun InputType.Companion.date(mask: String?): InputType {
    return object : InputType {
        override fun applyTo(editText: EditText) {
            editText.applyMask(
                platformInputType = android.text.InputType.TYPE_CLASS_DATETIME +
                        android.text.InputType.TYPE_DATETIME_VARIATION_DATE,
                mask = mask
            )
        }
    }
}

actual fun InputType.Companion.password(mask: String?): InputType {
    return object : InputType {
        override fun applyTo(editText: EditText) {
            editText.applyMask(
                platformInputType = android.text.InputType.TYPE_CLASS_TEXT +
                        android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD,
                mask = mask
            )
        }
    }
}

actual fun InputType.Companion.phone(mask: String?): InputType {
    return object : InputType {
        override fun applyTo(editText: EditText) {
            editText.applyMask(
                platformInputType = android.text.InputType.TYPE_CLASS_PHONE,
                mask = mask
            )
        }
    }
}

actual fun InputType.Companion.email(mask: String?): InputType {
    return object : InputType {
        override fun applyTo(editText: EditText) {
            editText.applyMask(
                platformInputType = android.text.InputType.TYPE_CLASS_TEXT +
                        android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS,
                mask = mask
            )
        }
    }
}

private fun EditText.applyMask(
    platformInputType: Int,
    mask: String?,
    maskFilledListener: ((String, Boolean) -> Unit) = { _, _ -> }
) {
    inputType = platformInputType
    if (mask != null) {
        val maskedInputListener = MaskedTextChangedListener(
            format = mask,
            field = this,
            valueListener = object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {
                    maskFilledListener(formattedValue, maskFilled)
                }
            }
        )

        addTextChangedListener(maskedInputListener)
    }
}
