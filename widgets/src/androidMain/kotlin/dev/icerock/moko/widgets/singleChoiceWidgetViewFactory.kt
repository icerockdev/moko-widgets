package com.icerockdev.mpp.widgets.forms

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.icerockdev.mpp.core.resources.StringDesc
import com.icerockdev.mpp.widgets.VFC
import com.icerockdev.mpp.widgets.ViewFactoryContext
import com.icerockdev.mpp.widgets.forms.databinding.WidgetSingleChoiceBinding
import com.icerockdev.mpp.widgets.style.background.Background
import com.icerockdev.mpp.widgets.style.background.buildBackground
import com.icerockdev.mpp.widgets.style.ext.setDpMargins
import com.icerockdev.mpp.widgets.style.ext.toPlatformSize

actual var singleChoiceWidgetViewFactory: VFC<SingleChoiceWidget> = { context: ViewFactoryContext,
                                                                      widget: SingleChoiceWidget ->

    val ctx = context.context
    val dm = ctx.resources.displayMetrics
    val style = widget.style
    val parent = context.parent
    val layoutInflater = LayoutInflater.from(ctx)
    val binding: WidgetSingleChoiceBinding =
        DataBindingUtil.inflate(layoutInflater, R.layout.widget_single_choice, parent, false)

    binding.widget = widget

    // todo: refactor code, update widget xml items, add popup
    binding.textInputLayout.hint = widget.label.liveData().value.toString(ctx)

    binding.bindItems(context, widget.values.ld())

    binding.textInputLayout.apply {
        layoutParams = FrameLayout.LayoutParams(
            style.size.width.toPlatformSize(dm),
            style.size.height.toPlatformSize(dm)
        ).also {
            it.setDpMargins(
                ctx.resources,
                style.margins.start,
                style.margins.top,
                style.margins.end,
                style.margins.bottom
            )
        }
    }

    binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

        override fun onNothingSelected(parent: AdapterView<*>?) {}

        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            widget.field.data.postValue(position)
            binding.editText.setText(widget.values.value[position].toString(ctx))
        }
    }

    binding.editText.apply {
        setOnClickListener { binding.spinner.performClick() }
        backgroundTintList = ColorStateList.valueOf(style.underlineColor)
        applyStyle(style.textStyle)
    }

    style.dropDownBackground?.let {
        binding.spinner.setPopupBackgroundDrawable(it.buildBackground(ctx))
    }

    binding.setLifecycleOwner(context.lifecycleOwner)
    binding.root
}

private fun WidgetSingleChoiceBinding.bindItems(
    context: ViewFactoryContext,
    list: LiveData<List<StringDesc>>
) {
    list.observe(context.lifecycleOwner, Observer { items ->
        val adapter = ArrayAdapter(context.context, R.layout.item_spinner,
            items.map { it.toString(context.context) })
        spinner.adapter = adapter
    })
}