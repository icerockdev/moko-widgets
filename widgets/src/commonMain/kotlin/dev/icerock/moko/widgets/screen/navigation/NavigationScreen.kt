/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package dev.icerock.moko.widgets.screen.navigation

import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.Screen
import dev.icerock.moko.widgets.screen.ScreenFactory
import dev.icerock.moko.widgets.screen.TypedScreenDesc

expect abstract class NavigationScreen<S>(
    initialScreen: TypedScreenDesc<Args.Empty, S>,
    router: Router
) : Screen<Args.Empty> where S : Screen<Args.Empty>, S : NavigationItem {

    class Router {
        internal fun <T, Arg : Args, S> createPushRouteInternal(
            destination: TypedScreenDesc<Arg, S>,
            inputMapper: (T) -> Arg
        ): Route<T> where S : Screen<Arg>, S : NavigationItem

        internal fun <IT, Arg : Args, OT, R : Parcelable, S> createPushResultRouteInternal(
            destination: TypedScreenDesc<Arg, S>,
            inputMapper: (IT) -> Arg,
            outputMapper: (R) -> OT
        ): RouteWithResult<IT, OT> where S : Screen<Arg>, S : Resultable<R>, S : NavigationItem

        internal fun <T, Arg : Args, S> createReplaceRouteInternal(
            destination: TypedScreenDesc<Arg, S>,
            inputMapper: (T) -> Arg
        ): Route<T> where S : Screen<Arg>, S : NavigationItem
    }
}

// createPushRouteInternal

fun <S> NavigationScreen.Router.createPushRoute(
    destination: TypedScreenDesc<Args.Empty, S>
): Route<Unit> where S : Screen<Args.Empty>, S : NavigationItem = createPushRouteInternal(
    destination
) { Args.Empty }

fun <T, P : Parcelable, S> NavigationScreen.Router.createPushRoute(
    destination: TypedScreenDesc<Args.Parcel<P>, S>,
    inputMapper: (T) -> P
): Route<T> where S : Screen<Args.Parcel<P>>, S : NavigationItem = createPushRouteInternal(
    destination
) { Args.Parcel(inputMapper(it)) }

// createReplaceRouteInternal

fun <S> NavigationScreen.Router.createReplaceRoute(
    destination: TypedScreenDesc<Args.Empty, S>
): Route<Unit> where S : Screen<Args.Empty>, S : NavigationItem = createReplaceRouteInternal(
    destination
) { Args.Empty }

fun <T, P : Parcelable, S> NavigationScreen.Router.createReplaceRoute(
    destination: TypedScreenDesc<Args.Parcel<P>, S>,
    inputMapper: (T) -> P
): Route<T> where S : Screen<Args.Parcel<P>>, S : NavigationItem = createReplaceRouteInternal(
    destination
) { Args.Parcel(inputMapper(it)) }

// createPushResultRouteInternal

fun <OT, R : Parcelable, S> NavigationScreen.Router.createPushResultRoute(
    destination: TypedScreenDesc<Args.Empty, S>,
    outputMapper: (R) -> OT
): RouteWithResult<Unit, OT> where S : Screen<Args.Empty>, S : Resultable<R>, S : NavigationItem =
    createPushResultRouteInternal(
        destination,
        { Args.Empty },
        outputMapper
    )

fun <IT, P : Parcelable, OT, R : Parcelable, S> NavigationScreen.Router.createPushResultRoute(
    destination: TypedScreenDesc<Args.Parcel<P>, S>,
    inputMapper: (IT) -> P,
    outputMapper: (R) -> OT
): RouteWithResult<IT, OT> where S : Screen<Args.Parcel<P>>, S : Resultable<R>, S : NavigationItem =
    createPushResultRouteInternal(
        destination,
        { Args.Parcel(inputMapper(it)) },
        outputMapper
    )

// NavigationItem

interface NavigationItem {
    val navigationBar: NavigationBar
}

sealed class NavigationBar {
    object None : NavigationBar()
    data class Normal(val title: StringDesc) : NavigationBar()
}
