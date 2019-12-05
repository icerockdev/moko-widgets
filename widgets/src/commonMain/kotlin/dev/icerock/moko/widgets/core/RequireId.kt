/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.core

interface RequireId<T : Theme.Id> : OptionalId<T> {
    override val id: T
}
