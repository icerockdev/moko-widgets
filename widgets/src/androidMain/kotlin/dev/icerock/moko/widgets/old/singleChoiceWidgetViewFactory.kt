/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

actual var singleChoiceWidgetViewFactory: VFC<SingleChoiceWidget> = { context: ViewFactoryContext,
                                                                      widget: SingleChoiceWidget ->
    TODO()
//    val ctx = context.context
//    val dm = ctx.resources.displayMetrics
//    val style = widget.style
//    val parent = context.parent
//    val layoutInflater = LayoutInflater.from(ctx)
//    val binding: WidgetSingleChoiceBinding =
//        DataBindingUtil.inflate(layoutInflater, R.layout.widget_single_choice, parent, false)
//
//    binding.widget = widget
//
//    // todo: refactor code, update widget xml items, add popup
//    binding.textInputLayout.hint = widget.label.liveData().value.toString(ctx)
//
//    binding.bindItems(context, widget.values.ld())
//
//    binding.textInputLayout.apply {
//        layoutParams = FrameLayout.LayoutParams(
//            style.size.width.toPlatformSize(dm),
//            style.size.height.toPlatformSize(dm)
//        ).also {
//            it.setDpMargins(
//                ctx.resources,
//                style.margins.start,
//                style.margins.top,
//                style.margins.end,
//                style.margins.bottom
//            )
//        }
//    }
//
//    binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//
//        override fun onNothingSelected(parent: AdapterView<*>?) {}
//
//        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//            widget.field.data.postValue(position)
//            binding.editText.setText(widget.values.value[position].toString(ctx))
//        }
//    }
//
//    binding.editText.apply {
//        setOnClickListener { binding.spinner.performClick() }
//        backgroundTintList = ColorStateList.valueOf(style.underlineColor)
//        applyStyle(style.textStyle)
//    }
//
//    style.dropDownBackground?.let {
//        binding.spinner.setPopupBackgroundDrawable(it.buildBackground(ctx))
//    }
//
//    binding.setLifecycleOwner(context.lifecycleOwner)
//    binding.root
}
//
//private fun WidgetSingleChoiceBinding.bindItems(
//    context: ViewFactoryContext,
//    list: LiveData<List<StringDesc>>
//) {
//    list.observe(context.lifecycleOwner, Observer { items ->
//        val adapter = ArrayAdapter(context.context, R.layout.item_spinner,
//            items.map { it.toString(context.context) })
//        spinner.adapter = adapter
//    })
//}