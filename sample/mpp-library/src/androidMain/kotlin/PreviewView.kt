import android.content.Context
import android.util.AttributeSet
import com.icerockdev.library.IMainViewModel
import com.icerockdev.library.MainScreen
import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.widgets.core.BasePreviewView
import dev.icerock.moko.widgets.core.Screen
import dev.icerock.moko.widgets.core.WidgetScope

class PreviewView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : BasePreviewView<IMainViewModel, MainScreen.Args>(context, attrs, defStyleAttr) {
    override fun createScreen(): Screen<IMainViewModel, MainScreen.Args> = MainScreen(widgetScope = WidgetScope())

    override fun createContract(): IMainViewModel = object : IMainViewModel {
        override val state: LiveData<State<String, String>> =
            MutableLiveData(initialValue = State.Loading())

        override fun onChangeStatePressed() {
        }
    }
}
