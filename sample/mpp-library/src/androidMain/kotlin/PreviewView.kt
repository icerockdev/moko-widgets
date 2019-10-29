/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import android.content.Context
import android.util.AttributeSet
import com.icerockdev.library.MainScreen
import com.icerockdev.library.MainViewModel
import com.icerockdev.library.Theme
import dev.icerock.moko.widgets.core.BasePreviewView
import dev.icerock.moko.widgets.core.Screen
import dev.icerock.moko.widgets.core.WidgetScope

class PreviewView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePreviewView<MainViewModel, MainScreen.Args>(context, attrs, defStyleAttr) {
    override fun createScreen(): Screen<MainViewModel, MainScreen.Args> =
        MainScreen(
            widgetScope = WidgetScope(),
            theme = Theme
        )

    override fun createContract(): MainViewModel = TODO()
//        object : MainViewModel {
//        override val state: LiveData<State<String, String>> =
//            MutableLiveData(initialValue = State.Loading())
//
//        override fun onChangeStatePressed() {
//        }
//    }
}
