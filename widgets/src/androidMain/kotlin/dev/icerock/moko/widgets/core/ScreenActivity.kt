package dev.icerock.moko.widgets.core

import android.os.Bundle
import android.os.Parcel
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dev.icerock.moko.core.Parcelable
import dev.icerock.moko.mvvm.ViewModelFactory
import dev.icerock.moko.mvvm.viewmodel.ViewModel

abstract class ScreenActivity<VM : ViewModel, Args : Parcelable, S : Screen<VM, Args>> : AppCompatActivity() {
    abstract fun createScreen(): S
    abstract fun getArgs(): Args
    abstract val viewModelClass: Class<VM>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val screen = createScreen()

        val viewModel: VM = ViewModelProvider(this, ViewModelFactory {
            screen.createViewModel(getArgs())
        }).get(viewModelClass)

        val widget = screen.createWidget(viewModel)
        val view = widget.buildView(
            ViewFactoryContext(
                context = this,
                lifecycleOwner = this,
                parent = null
            )
        )

        setContentView(view)
    }

//    class NoArgs private constructor() : Parcelable {
//        override fun describeContents(): Int = 0
//
//        override fun writeToParcel(dest: Parcel?, flags: Int) {}
//
//        companion object {
//            @JvmField
//            val CREATOR = object : android.os.Parcelable.Creator<NoArgs> {
//                override fun createFromParcel(parcel: Parcel) = NoArgs()
//                override fun newArray(size: Int) = arrayOfNulls<NoArgs>(size)
//            }
//        }
//    }
}
