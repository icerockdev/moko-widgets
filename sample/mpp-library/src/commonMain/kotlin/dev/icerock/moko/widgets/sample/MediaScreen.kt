/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.sample

import com.icerockdev.library.MR
import dev.icerock.moko.media.Bitmap
import dev.icerock.moko.media.picker.MediaPickerController
import dev.icerock.moko.media.picker.MediaSource
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcher
import dev.icerock.moko.mvvm.dispatcher.EventsDispatcherOwner
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.widgets.ButtonWidget
import dev.icerock.moko.widgets.button
import dev.icerock.moko.widgets.constraint
import dev.icerock.moko.widgets.core.Image
import dev.icerock.moko.widgets.core.Theme
import dev.icerock.moko.widgets.core.Value
import dev.icerock.moko.widgets.core.Widget
import dev.icerock.moko.widgets.core.resource
import dev.icerock.moko.widgets.image
import dev.icerock.moko.widgets.media.bind
import dev.icerock.moko.widgets.media.bitmap
import dev.icerock.moko.widgets.media.createMediaPickerController
import dev.icerock.moko.widgets.permissions.createPermissionsController
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.WidgetScreen
import dev.icerock.moko.widgets.screen.getViewModel
import dev.icerock.moko.widgets.screen.listen
import dev.icerock.moko.widgets.screen.navigation.NavigationBar
import dev.icerock.moko.widgets.screen.navigation.NavigationItem
import dev.icerock.moko.widgets.screen.showToast
import dev.icerock.moko.widgets.style.view.SizeSpec
import dev.icerock.moko.widgets.style.view.WidgetSize
import kotlinx.coroutines.launch

class MediaScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>(), NavigationItem, MediaViewModel.EventsListener {

    override val navigationBar: NavigationBar = NavigationBar.Normal(
        title = "Media sample".desc()
    )

    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        val viewModel = getViewModel {
            MediaViewModel(
                mediaPickerController = createMediaPickerController(createPermissionsController()),
                eventsDispatcher = createEventsDispatcher()
            )
        }
        viewModel.mediaPickerController.bind(this)
        viewModel.eventsDispatcher.listen(screen = this, listener = this)

        return with(theme) {
            constraint(size = WidgetSize.AsParent) {
                val selectedImage = +image(
                    size = WidgetSize.Const(
                        width = SizeSpec.Exact(200f),
                        height = SizeSpec.Exact(200f)
                    ),
                    image = viewModel.selectedImage.map { bitmap ->
                        bitmap?.let { Image.bitmap(it) } ?: Image.resource(MR.images.home_black_18)
                    }
                )

                val selectBtn = +button(
                    size = WidgetSize.WrapContent,
                    content = ButtonWidget.Content.Text(Value.data("Select".desc())),
                    onTap = viewModel::onSelectImagePressed
                )

                constraints {
                    selectedImage centerXToCenterX root
                    selectedImage centerYToCenterY root

                    selectBtn centerXToCenterX root
                    selectBtn topToBottom selectedImage
                }
            }
        }
    }

    override fun showError(error: String) {
        showToast(error.desc())
    }
}

class MediaViewModel(
    val mediaPickerController: MediaPickerController,
    override val eventsDispatcher: EventsDispatcher<EventsListener>
) : ViewModel(), EventsDispatcherOwner<MediaViewModel.EventsListener> {
    private val _selectedImage = MutableLiveData<Bitmap?>(initialValue = null)
    val selectedImage: LiveData<Bitmap?> = _selectedImage

    fun onSelectImagePressed() {
        viewModelScope.launch {
            try {
                val bitmap = mediaPickerController.pickImage(MediaSource.CAMERA)
                _selectedImage.value = bitmap
            } catch (exception: Exception) {
                eventsDispatcher.dispatchEvent { showError(exception.toString()) }
            }
        }
    }

    interface EventsListener {
        fun showError(error: String)
    }
}
