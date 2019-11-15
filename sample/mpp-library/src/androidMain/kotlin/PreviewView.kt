/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import android.content.Context
import android.util.AttributeSet
import com.icerockdev.library.universal.WidgetsScreen
import dev.icerock.moko.widgets.core.BasePreviewView
import dev.icerock.moko.widgets.screen.Screen

class PreviewView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePreviewView(context, attrs, defStyleAttr) {

    override fun createScreen(): Screen<*> {
        return WidgetsScreen()
    }
}
