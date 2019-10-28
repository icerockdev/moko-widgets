package com.icerockdev.mpp.widgets.forms

import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Spinner
import androidx.databinding.BindingAdapter
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.URL


/**
 * Invokes handler when layout's [android.widget.EditText] focus is lost.
 */
@BindingAdapter("app:onFocusLost")
fun bindOnFocusLost(view: TextInputLayout, handler: FocusLostHandler) {

    val focusListener = object : View.OnFocusChangeListener {
        var hadFocus = false

        override fun onFocusChange(v: View?, hasFocus: Boolean) {
            if (hadFocus && !hasFocus) {
                handler.onFocusLost()
            }

            hadFocus = hasFocus
        }

    }

    view.editText?.onFocusChangeListener = focusListener
    return
}

/**
 * Calls onFocusLost when view loses focus.
 */
interface FocusLostHandler {
    fun onFocusLost()
}

@BindingAdapter(value = ["image", "imageUrl", "placeholder", "roundImage"], requireAll = false)
fun bindGlideLoadDrawable(
    view: ImageView,
    image: Drawable?,
    imageUrl: String?,
    placeholder: Drawable? = null,
    roundImage: Boolean? = false
) {
    if (image == null && imageUrl.isNullOrEmpty()) {
        placeholder?.let {
            view.setImageDrawable(placeholder)
        }

        return
    }

    val requestOptions = if (roundImage == true) {
        RequestOptions.circleCropTransform()
    } else {
        RequestOptions.centerCropTransform()
    }

    placeholder?.let {
        requestOptions.placeholder(placeholder)
    }

    if (image != null) {
        Glide.with(view)
            .load(image)
            .apply(requestOptions)
            .into(view)
    } else {
        Glide.with(view)
            .load(imageUrl)
            .apply(requestOptions)
            .into(view)
    }
}

@BindingAdapter("selectedItem")
fun bindSelectedItem(view: Spinner, item: Int) {
    view.setSelection(item, true)
}

// Fast decision for project
@BindingAdapter(
    value = ["mediaImageUrl", "mediaPlaceholder", "mediaRoundImage"],
    requireAll = false
)
fun bindMediaImageLoad(
    view: ImageView,
    mediaImageUrl: String?,
    mediaPlaceholder: Drawable? = null,
    loadingCallback: MediaLoadingListener?
) {
    val circularProgressDrawable = CircularProgressDrawable(view.context)
    circularProgressDrawable.strokeWidth = 5f
    circularProgressDrawable.centerRadius = 30f
    circularProgressDrawable.start()
    view.setImageDrawable(circularProgressDrawable)

    mediaImageUrl?.let { imageUrl ->
        // TODO FIXME MEDIUM убрать глобал скоуп
        GlobalScope.launch(Dispatchers.Main) {
            var inputStream: InputStream? = null
            try {
                val bmp = withContext(Dispatchers.IO) {
                    val url = URL(imageUrl)
                    inputStream = url.openConnection().getInputStream()
                    BitmapFactory.decodeStream(inputStream)
                }
                view.layoutParams = FrameLayout.LayoutParams(
                    view.layoutParams.width,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
                )
                view.setImageBitmap(bmp)
                loadingCallback?.onSuccess()

            } catch (e: Throwable) {
                loadingCallback?.onError(e)
            } finally {
                inputStream?.close()
            }
        }
    }
}

interface MediaLoadingListener {

    fun onSuccess()

    fun onError(exception: Throwable?)

}