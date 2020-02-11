/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.style

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.widget.EditText
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import com.redmadrobot.inputmask.MaskedTextChangedListener
import dev.icerock.moko.widgets.style.input.InputType

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

    if (type == InputType.SMS_CODE) {
        val client = SmsRetriever.getClient(context)
        val task = client.startSmsRetriever()
        task.addOnSuccessListener {
            val smsReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context?, intent: Intent?) {
                    if (!SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent?.action)) return
                    val extras = intent?.extras
                    val status = extras?.get(SmsRetriever.EXTRA_SMS_MESSAGE) as Status
                    when (status.statusCode) {
                        CommonStatusCodes.SUCCESS -> {
                            val message =
                                (extras.get(SmsRetriever.EXTRA_SMS_MESSAGE) as String).split(" ")
                            val code = message.findLast { it.toIntOrNull() != null }
                            setText(code)
                        }
                    }
                }
            }
            val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
            context.registerReceiver(smsReceiver, intentFilter)
        }
    }
}

private fun InputType.toPlatformInputType(): Int {
    return when (this) {
        InputType.EMAIL -> android.text.InputType.TYPE_CLASS_TEXT + android.text.InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        InputType.PLAIN_TEXT -> android.text.InputType.TYPE_CLASS_TEXT + android.text.InputType.TYPE_TEXT_VARIATION_NORMAL
        InputType.PASSWORD -> android.text.InputType.TYPE_CLASS_TEXT + android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD
        InputType.DATE -> android.text.InputType.TYPE_CLASS_DATETIME + android.text.InputType.TYPE_DATETIME_VARIATION_DATE
        InputType.PHONE -> android.text.InputType.TYPE_CLASS_PHONE
        InputType.DIGITS -> android.text.InputType.TYPE_CLASS_NUMBER
        InputType.SMS_CODE -> android.text.InputType.TYPE_CLASS_NUMBER
    }
}
