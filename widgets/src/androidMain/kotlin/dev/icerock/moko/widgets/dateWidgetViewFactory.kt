package com.icerockdev.mpp.widgets.forms

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.databinding.DataBindingUtil
import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.fields.FormField
import com.icerockdev.mpp.widgets.VFC
import com.icerockdev.mpp.widgets.ViewFactoryContext
import com.icerockdev.mpp.widgets.forms.databinding.WidgetDateBinding
import com.icerockdev.mpp.widgets.style.ext.setDpMargins
import com.icerockdev.mpp.widgets.style.ext.toPlatformSize
import com.icerockdev.mpp.widgets.style.input.InputType

// TODO MEDIUM надо вернуть в вариант с диалогами - инпуты теперь через InputWidget с типом Date можно
actual var dateWidgetViewFactory: VFC<DateWidget> = { context: ViewFactoryContext,
                                                      widget: DateWidget ->

    val ctx = context.context
    val dm = ctx.resources.displayMetrics
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(ctx)
    val binding: WidgetDateBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_date, parent, false)

    binding.widget = widget

    binding.setupListeners(widget.field)

    val style = widget.style

    binding.root.layoutParams = Constraints.LayoutParams(
        widget.style.size.width.toPlatformSize(dm),
        widget.style.size.height.toPlatformSize(dm)
    ).apply {
        setDpMargins(
            context.context.resources,
            marginStart = style.margins.start,
            marginEnd = style.margins.end,
            marginTop = style.margins.top,
            marginBottom = style.margins.bottom
        )
    }

    binding.error.applyStyle(style.errorTextStyle)
    binding.hint.applyStyle(style.labelTextStyle)
    binding.editText.apply {
        backgroundTintList = ColorStateList.valueOf(style.underLineColor)
        applyStyle(style.textStyle)
        applyInputType(InputType.DATE) { input, completed ->
            processDateInput(
                widget,
                input,
                completed
            )
        }
    }

    binding.lifecycleOwner = context.lifecycleOwner
    binding.root
}

private fun processDateInput(widget: DateWidget, input: String, completed: Boolean) {
    widget.field.data.value = if (input.isBlank()) {
        Date.Empty
    } else if (!completed) {
        Date.Incomplete
    } else {
        val splitted = input.split(".").map { it.toInt() }

        Date.Filled(
            splitted[2],
            splitted[1],
            splitted[0]
        )
    }

    widget.field.validate()
}

fun WidgetDateBinding.setupListeners(
    field: FormField<Date?, StringDesc>
) {
    val motionLayout = root as MotionLayout

    editText.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {
        }

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
            text?.apply {
                if (this.isEmpty() && editText.isFocused.not()) {
                    hint.setTypeface(hint.typeface, Typeface.BOLD)
                    motionLayout.transitionToStart()
                } else {
                    hint.typeface = Typeface.DEFAULT
                    motionLayout.transitionToEnd()
                }
            }
        }

    })

    editText.onFocusChangeListener = object : View.OnFocusChangeListener {
        var hadFocus = false

        override fun onFocusChange(p0: View?, hasFocus: Boolean) {
            if (hadFocus && hasFocus.not()) {
                field.validate()
            }
            hadFocus = hasFocus

            if (editText.text?.isEmpty() == true && hasFocus.not()) {
                hint.setTypeface(hint.typeface, Typeface.BOLD)
                motionLayout.transitionToStart()
            } else {
                hint.typeface = Typeface.DEFAULT
                motionLayout.transitionToEnd()
            }
        }
    }

    motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
        override fun onTransitionTrigger(
            p0: MotionLayout?,
            p1: Int,
            p2: Boolean,
            p3: Float
        ) {
        }

        override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
        }

        override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
            hint.layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.WRAP_CONTENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
        }

        override fun onTransitionCompleted(p0: MotionLayout?, currentId: Int) {
        }
    })
}