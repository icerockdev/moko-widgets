package dev.icerock.moko.widgets.core

abstract class Screen<Contract, Args> {
    abstract fun createViewModel(arguments: Args): Contract
    abstract fun createWidget(viewModel: Contract): Widget
}
