/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

import dev.icerock.moko.mvvm.viewmodel.ViewModel

abstract class Screen<VM : ViewModel, Args> : ViewModelProvider<VM, Args> {
    abstract fun createWidget(viewModel: VM): Widget
}

interface ViewModelProvider<VM : ViewModel, Args> {
    fun createViewModel(arguments: Args): VM
}
