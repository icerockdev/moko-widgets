/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style

import android.widget.EditText
import dev.icerock.moko.widgets.style.input.InputType
import com.redmadrobot.inputmask.MaskedTextChangedListener

fun EditText.applyInputType(
    type: InputType,
    maskFilledListener: ((String, Boolean) -> Unit) = { _, _ -> }
) {
    inputType = type.toPlatformInputType()

    type.mask?.let { mask ->
        val maskedInputListener =
            MaskedTextChangedListener(mask, this, object : MaskedTextChangedListener.ValueListener {
                override fun onTextChanged(
                    maskFilled: Boolean,
                    extractedValue: String,
                    formattedValue: String
                ) {
                    maskFilledListener(formattedValue, maskFilled)
                }
            })

        addTextChangedListener(maskedInputListener)
    }
}

private fun InputType.toPlatformInputType(): Int {
    return when (this) {
        is InputType.Email -> android.text.InputType.TYPE_CLASS_TEXT + android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        is InputType.Plain -> android.text.InputType.TYPE_CLASS_TEXT + android.text.InputType.TYPE_TEXT_VARIATION_NORMAL
        is InputType.Password -> android.text.InputType.TYPE_CLASS_TEXT + android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        is InputType.Date -> android.text.InputType.TYPE_CLASS_DATETIME + android.text.InputType.TYPE_DATETIME_VARIATION_DATE
        is InputType.Phone -> android.text.InputType.TYPE_CLASS_PHONE
        is InputType.Digits -> android.text.InputType.TYPE_CLASS_NUMBER
    }
}
