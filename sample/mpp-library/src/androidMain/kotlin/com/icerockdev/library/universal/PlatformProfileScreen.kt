/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library.universal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.icerockdev.library.databinding.FragmentProfileBinding
import dev.icerock.moko.widgets.screen.navigation.Route

// here we can work with ProfileScreen as any other Fragment
actual class PlatformProfileScreen actual constructor(
    override val backRoute: Route<Unit>
) : ProfileScreen() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: android.os.Bundle?
    ): View? {
        val binding = FragmentProfileBinding.inflate(inflater, container, false)
        binding.viewModel = this.profileViewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}
