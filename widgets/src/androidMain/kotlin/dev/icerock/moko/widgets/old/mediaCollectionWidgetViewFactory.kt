/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.old

import dev.icerock.moko.widgets.core.VFC
import dev.icerock.moko.widgets.core.ViewFactoryContext

actual var mediaCollectionWidgetViewFactory: VFC<MediaCollectionWidget> =
    { context: ViewFactoryContext,
      widget: MediaCollectionWidget ->

        TODO()
//        val ctx = context.context
//        val parent = context.parent
//        val layoutInflater = LayoutInflater.from(ctx)
//        val binding: WidgetMediaCollectionBinding =
//            DataBindingUtil.inflate(layoutInflater, R.layout.widget_media_collection, parent, false)
//
//        binding.bindList(widget)
//        binding.recyclerView.addItemDecoration(MediaDecoration())
//        binding.lifecycleOwner = context.lifecycleOwner
//        binding.root
    }

//
//private class MediaDecoration : RecyclerView.ItemDecoration() {
//
//    override fun getItemOffsets(
//        outRect: Rect,
//        view: View,
//        parent: RecyclerView,
//        state: RecyclerView.State
//    ) {
//        super.getItemOffsets(outRect, view, parent, state)
//
//        parent.adapter?.run {
//
//            when (parent.getChildAdapterPosition(view)) {
//                0 -> {
//                    val leftToCenter = parent.measuredWidth / 2
//                    val itemToCenter = view.layoutParams.width / 2
//
//                    val paddingLeft = leftToCenter - itemToCenter
//                    outRect.left = paddingLeft
//                }
//                itemCount - 1 -> {
//                    outRect.right =
//                        (view.context.resources.displayMetrics.density * RIGHT_PADDING).toInt()
//                }
//            }
//        }
//    }
//
//    private companion object {
//        const val RIGHT_PADDING = 16
//    }
//}
//
//private fun WidgetMediaCollectionBinding.bindList(mediaWidget: MediaCollectionWidget) {
//    widget = mediaWidget
//    photos = mediaWidget.field.data.map { mediaList ->
//        mediaList.map { media ->
//            ItemPhoto()
//                .setIsPlayVisible(media.type == MediaType.VIDEO)
//                .setImage(media.preview.platformBitmap.getResizedBitmap())
//                .setOnItemClick({
//                    mediaWidget.itemClickListener(media)
//                } as? kotlin.jvm.functions.Function0<Unit>)
//                .setOnDeleteClick({
//                    mediaWidget.deleteListener(media)
//                } as? kotlin.jvm.functions.Function0<Unit>)
//        }.plus(
//            ItemPhotoAdd()
//                .setOnClick({
//                    mediaWidget.addListener()
//                } as? kotlin.jvm.functions.Function0<Unit>)
//        )
//    }.ld()
//}