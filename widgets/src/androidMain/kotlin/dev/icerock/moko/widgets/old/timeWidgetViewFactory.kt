/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

actual var timeWidgetViewFactory: VFC<TimeWidget> = { context: ViewFactoryContext,
                                                      widget: TimeWidget ->
    TODO()
//    val ctx = context.context
//    val parent = context.parent
//    val layoutInflater = LayoutInflater.from(ctx)
//    val binding: WidgetTimeBinding =
//        DataBindingUtil.inflate(layoutInflater, R.layout.widget_time, parent, false)
//    binding.widget = widget
//
//    binding.bindPicker(ctx, widget)
//
//    binding.setLifecycleOwner(context.lifecycleOwner)
//    binding.root
}
//
//private fun WidgetTimeBinding.bindPicker(context: Context, timeWidget: TimeWidget) {
//    widget = timeWidget
//
//    textInputLayout.hint = timeWidget.label.liveData().value.toString(context)
//    timeWidget.field.data.value?.let { time ->
//        editText.setText(time.toString())
//    }
//
//    val calendar = getInstance()
//    val initialHours = timeWidget.field.data.value?.hours ?: calendar.get(HOUR_OF_DAY)
//    val initialMinutes = timeWidget.field.data.value?.minutes ?: calendar.get(MINUTE)
//
//    val timePickerDialog =
//        TimePickerDialog(context, { _: TimePicker?, hourOfDay: Int, minute: Int ->
//            val newTime = Time(hourOfDay, minute, 0)
//            timeWidget.field.data.postValue(newTime)
//            editText.setText(newTime.toString())
//        }, initialHours, initialMinutes - 1, true)
//
//    editText.setOnClickListener { timePickerDialog.show() }
//}