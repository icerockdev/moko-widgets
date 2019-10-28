package dev.icerock.moko.widgets.core

import dev.icerock.moko.core.Parcelable
import dev.icerock.moko.mvvm.viewmodel.ViewModel

abstract class Screen<T : ViewModel, Args : Parcelable> {
    abstract fun createViewModel(arguments: Args): T
    abstract fun createWidget(viewModel: T): Widget
}
