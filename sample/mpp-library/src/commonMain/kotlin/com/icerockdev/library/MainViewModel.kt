/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

package com.icerockdev.library

import dev.icerock.moko.fields.FormField
import dev.icerock.moko.fields.liveBlock
import dev.icerock.moko.fields.validate
import dev.icerock.moko.mvvm.State
import dev.icerock.moko.mvvm.livedata.LiveData
import dev.icerock.moko.mvvm.livedata.MutableLiveData
import dev.icerock.moko.mvvm.livedata.map
import dev.icerock.moko.mvvm.livedata.mergeWith
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import dev.icerock.moko.resources.desc.StringDesc
import dev.icerock.moko.resources.desc.desc

interface MainViewModelContract {
    val state: LiveData<State<String, String>>

    fun onChangeStatePressed()
}

class MainViewModel(val title: String) : ViewModel(), MainViewModelContract {
    private val _state: MutableLiveData<State<String, String>> = MutableLiveData(initialValue = State.Empty())
    override val state: LiveData<State<String, String>> = _state

    val nameField: FormField<String, StringDesc> = FormField("Aleksey", liveBlock { null })
    val nicknameField: FormField<String, StringDesc> = FormField("Alex009", liveBlock { null })
    val aboutField: FormField<String, StringDesc> =
        FormField("My name is Aleksey Mikhailov, i am CTO of IceRock Development from Novosibirsk", liveBlock { null })
    val emailField: FormField<String, StringDesc> = FormField("am@icerock.dev", liveBlock { email ->
        if (email.contains("@")) null
        else "Should contain @".desc()
    })
    val phoneField: FormField<String, StringDesc> = FormField("+79999999999", liveBlock { null })
    val birthdayField: FormField<String, StringDesc> = FormField("31.05.1993", liveBlock { null })
    val genders: LiveData<List<StringDesc>> = MutableLiveData(
        initialValue = listOf(
            "Мужчина".desc(),
            "Женщина".desc()
        )
    )
    val genderField = FormField<Int?, StringDesc>(null) { indexLiveData ->
        genders.mergeWith(indexLiveData) { genders, index ->
            if (index == null) null
            else genders[index]
        }.map {
            if (it == null) "invalid!".desc()
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

    fun onSavePressed() {
        fields.validate()
    }
}