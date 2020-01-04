/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev

import App
import ScreensPlatformDeps
import android.app.Application
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.icerockdev.databinding.FragmentProfileBinding
import com.icerockdev.library.universal.ProfileScreen
import dev.icerock.moko.widgets.screen.Args
import dev.icerock.moko.widgets.screen.BaseApplication
import dev.icerock.moko.widgets.screen.PlatformScreen

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        mppApp = App(
            widgetsPlatformDeps = object : App.WidgetsPlatformDeps {},
            screensPlatformDeps = ScreensPlatformDeps(
                profileScreenDeps = object : PlatformScreen.Deps<Args.Parcel<ProfileScreen.Arg>, ProfileScreen.Bundle> {
                    override fun createView(
                        screen: PlatformScreen<Args.Parcel<ProfileScreen.Arg>, ProfileScreen.Bundle>,
                        platformBundle: ProfileScreen.Bundle,
                        inflater: LayoutInflater,
                        container: ViewGroup?,
                        savedInstanceState: Bundle?
                    ): View {
                        val binding = FragmentProfileBinding.inflate(inflater, container, false)
                        binding.viewModel = platformBundle.viewModel
                        binding.lifecycleOwner = screen
                        return binding.root
                    }
                }
            )
        ).apply {
            setup()
        }
    }

    companion object {
        lateinit var mppApp: BaseApplication
    }
}
