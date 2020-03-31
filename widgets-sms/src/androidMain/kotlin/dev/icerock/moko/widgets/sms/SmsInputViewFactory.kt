/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.sms

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import dev.icerock.moko.widgets.core.widget.InputWidget
import dev.icerock.moko.widgets.core.ViewBundle
import dev.icerock.moko.widgets.core.ViewFactory
import dev.icerock.moko.widgets.core.ViewFactoryContext
import dev.icerock.moko.widgets.core.style.view.WidgetSize

actual class SmsInputViewFactory actual constructor(private val wrapped: ViewFactory<InputWidget<out WidgetSize>>) :
    ViewFactory<InputWidget<out WidgetSize>> {

    override fun <WS : WidgetSize> build(
        widget: InputWidget<out WidgetSize>,
        size: WS,
        viewFactoryContext: ViewFactoryContext
    ): ViewBundle<WS> {
        val bundle =
            wrapped.build(widget = widget, size = size, viewFactoryContext = viewFactoryContext)
        val view = bundle.view

        val textField = findTextField(view)
        if (textField != null) {
            val client = SmsRetriever.getClient(textField.context)
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
                                textField.setText(code)
                            }
                        }
                    }
                }
                val intentFilter = IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION)
                textField.context.registerReceiver(smsReceiver, intentFilter)
            }
        }
        return bundle
    }

    private fun findTextField(view: View): TextView? {
        return when (view) {
            is TextView -> view
            is ViewGroup -> {
                for (i in 0 until view.childCount) {
                    val childView = view.getChildAt(i)

                    val rv = findTextField(childView)
                    if (rv != null) return rv
                }

                null
            }
            else -> null
        }
    }
}