/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

typealias AnyWidget = Widget<*>

abstract class Widget<T : Widget<T>> {
    protected abstract val factory: VFC<T>

    fun buildView(viewFactoryContext: ViewFactoryContext): View {
        return factory(viewFactoryContext, this as T)
    }

    interface Style
}
