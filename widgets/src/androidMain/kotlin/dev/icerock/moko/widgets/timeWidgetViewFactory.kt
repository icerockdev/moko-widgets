package com.icerockdev.mpp.widgets.forms

import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TimePicker
import androidx.databinding.DataBindingUtil
import com.icerockdev.mpp.widgets.VFC
import com.icerockdev.mpp.widgets.ViewFactoryContext
import com.icerockdev.mpp.widgets.forms.databinding.WidgetTimeBinding
import java.util.Calendar.*

actual var timeWidgetViewFactory: VFC<TimeWidget> = { context: ViewFactoryContext,
                                                      widget: TimeWidget ->

    val ctx = context.context
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(ctx)
    val binding: WidgetTimeBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_time, parent, false)
    binding.widget = widget

    binding.bindPicker(ctx, widget)

    binding.setLifecycleOwner(context.lifecycleOwner)
    binding.root
}

private fun WidgetTimeBinding.bindPicker(context: Context, timeWidget: TimeWidget) {
    widget = timeWidget

    textInputLayout.hint = timeWidget.label.liveData().value.toString(context)
    timeWidget.field.data.value?.let { time ->
        editText.setText(time.toString())
    }

    val calendar = getInstance()
    val initialHours = timeWidget.field.data.value?.hours ?: calendar.get(HOUR_OF_DAY)
    val initialMinutes = timeWidget.field.data.value?.minutes ?: calendar.get(MINUTE)

    val timePickerDialog =
        TimePickerDialog(context, { _: TimePicker?, hourOfDay: Int, minute: Int ->
            val newTime = Time(hourOfDay, minute, 0)
            timeWidget.field.data.postValue(newTime)
            editText.setText(newTime.toString())
        }, initialHours, initialMinutes - 1, true)

    editText.setOnClickListener { timePickerDialog.show() }
}