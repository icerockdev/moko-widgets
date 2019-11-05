/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library

import com.icerockdev.library.screen.CryptoProfileScreen
import com.icerockdev.library.screen.DemoScreen
import com.icerockdev.library.screen.McommerceProfileScreen
import com.icerockdev.library.screen.SocialProfileScreen
import com.icerockdev.library.screen.UsersScreen
import dev.icerock.moko.fields.FormField
import dev.icerock.moko.fields.liveBlock
import dev.icerock.moko.fields.validate
import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.livedata.mergeWith
import dev.icerock.moko.mvvm.livedata.readOnly
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc
import dev.icerock.moko.units.UnitItem

class MainViewModel(
    val title: String,
    val unitsFactory: UnitsFactory
) : ViewModel(), CryptoProfileScreen.ViewModelContract, DemoScreen.ViewModelContract,
    McommerceProfileScreen.ViewModelContract, SocialProfileScreen.ViewModelContract, UsersScreen.ViewModelContract {
    private val _items = MutableLiveData<List<UnitItem>>(initialValue = with(unitsFactory) {
        listOf(
            "Aleksey Mikhailov",
            "Alexandr Pogrebnyak",
            "Andrey Breslav",
            "Nikolay Igotti"
        ).mapIndexed { index, name ->
            createUserUnit(
                itemId = index.toLong(),
                name = name,
                avatarUrl = "https://avatars0.githubusercontent.com/u/5010169"
            ) {
                println("clicked $index user")
            }
        }
    })
    override val items: LiveData<List<UnitItem>> = _items.readOnly()

    private val _state: MutableLiveData<State<String, String>> = MutableLiveData(initialValue = State.Empty())
    override val state: LiveData<State<String, String>> = _state

    override val nameField: FormField<String, StringDesc> = FormField("Aleksey", liveBlock { null })
    override val nicknameField: FormField<String, StringDesc> = FormField("Alex009", liveBlock { null })
    override val aboutField: FormField<String, StringDesc> =
        FormField("My name is Aleksey Mikhailov, i am CTO of IceRock Development from Novosibirsk", liveBlock { null })
    override val emailField: FormField<String, StringDesc> = FormField("am@icerock.dev", liveBlock { email ->
        if (email.contains("@")) null
        else "Should contain @".desc()
    })
    override val phoneField: FormField<String, StringDesc> = FormField("+79999999999", liveBlock { null })
    override val birthdayField: FormField<String, StringDesc> = FormField("31.05.1993", liveBlock { null })
    override val genders: LiveData<List<StringDesc>> = MutableLiveData(
        initialValue = listOf(
            "Мужчина".desc(),
            "Женщина".desc()
        )
    )
    override val genderField = FormField<Int?, StringDesc>(null) { indexLiveData ->
        genders.mergeWith(indexLiveData) { genders, index ->
            if (index == null) null
            else genders[index]
        }.map {
            if (it == null) "invalid!".desc()
            else null
        }
    }
    override val passwordField: FormField<String, StringDesc> = FormField("123456", liveBlock { null })
    override val repeatPasswordField: FormField<String, StringDesc> = FormField("1234567") { repeatPasswordData ->
        repeatPasswordData.mergeWith(passwordField.data) { repeatPassword, password ->
            if (repeatPassword != password) "should be same".desc() as StringDesc
            else null
        }
    }

    private val fields = listOf(
        nameField,
        nicknameField,
        aboutField,
        emailField,
        phoneField,
        birthdayField,
        genderField
    )

    override fun onChangeStatePressed() {
        when (state.value) {
            is State.Empty -> _state.value = State.Loading()
            is State.Loading -> {
                _state.value = State.Data(data = "hello $title!")
            }
            is State.Data -> _state.value = State.Error(error = "this is error")
            is State.Error -> _state.value = State.Empty()
        }
    }

    override fun onSavePressed() {
        fields.validate()
    }

    interface UnitsFactory {
        fun createUserUnit(
            itemId: Long,
            name: String,
            avatarUrl: String,
            onClick: () -> Unit
        ): UnitItem
    }
}
