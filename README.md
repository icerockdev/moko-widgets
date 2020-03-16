![moko-widgets](https://user-images.githubusercontent.com/5010169/70204294-93a45900-1752-11ea-9bb6-820d514ceef9.png)  
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) [![Download](https://api.bintray.com/packages/icerockdev/moko/moko-widgets/images/download.svg) ](https://bintray.com/icerockdev/moko/moko-widgets/_latestVersion) ![kotlin-version](https://img.shields.io/badge/kotlin-1.3.70-orange)

# Mobile Kotlin widgets
This is a Kotlin MultiPlatform library that provides declarative UI and application screens management
 in common code. You can implement full application for Android and iOS only from common code with it.  

## Current status
Current version - `0.1.0-dev-16`. Dev version is not tested in production tasks yet, API can be changed and
 bugs may be found. But dev version is chance to test limits of API and concepts to feedback and improve lib.
 We open for any feedback and ideas (go to issues or #moko at [kotlinlang.slack.com](https://kotlinlang.slack.com))!

## Roadmap
- December-January: Test library in real project;
- February: production usage at IceRock;
- March: 0.1.0 release with flexible API;
- First half of 2020: more widgets, more factories; figma template and generation of screens.

## Sample Screen
|Android|iOS|
|---|---|
|![Sample Android](https://user-images.githubusercontent.com/5010169/70204616-d0bd1b00-1753-11ea-95d1-749341631ba7.png)|![Sample iOS](https://user-images.githubusercontent.com/5010169/70204576-aff4c580-1753-11ea-95b9-14e488edb689.png)|
Code of screen structure:
```kotlin
class LoginScreen(
    private val theme: Theme,
    private val loginViewModelFactory: () -> LoginViewModel
) : WidgetScreen<Args.Empty>() {

    override fun createContentWidget() = with(theme) {
        val viewModel = getViewModel(loginViewModelFactory)

        constraint(size = WidgetSize.AsParent) {
            val logoImage = +image(
                size = WidgetSize.Const(SizeSpec.WrapContent, SizeSpec.WrapContent),
                image = const(Image.resource(MR.images.logo)),
                scaleType = ImageWidget.ScaleType.FIT
            )

            val emailInput = +input(
                size = WidgetSize.WidthAsParentHeightWrapContent,
                id = Id.EmailInputId,
                label = const("Email".desc() as StringDesc),
                field = viewModel.emailField,
                inputType = InputType.PHONE
            )
            val passwordInput = +input(
                size = WidgetSize.WidthAsParentHeightWrapContent,
                id = Id.PasswordInputId,
                label = const("Password".desc() as StringDesc),
                field = viewModel.passwordField
            )
            val loginButton = +button(
                size = WidgetSize.Const(SizeSpec.AsParent, SizeSpec.Exact(50f)),
                content = ButtonWidget.Content.Text(Value.data("Login".desc())),
                onTap = viewModel::onLoginPressed
            )

            val registerButton = +button(
                id = Id.RegistrationButtonId,
                size = WidgetSize.Const(SizeSpec.WrapContent, SizeSpec.Exact(40f)),
                content = ButtonWidget.Content.Text(Value.data("Registration".desc())),
                onTap = viewModel::onRegistrationPressed
            )

            val copyrightText = +text(
                size = WidgetSize.WrapContent,
                text = const("IceRock Development")
            )

            constraints {
                passwordInput centerYToCenterY root
                passwordInput leftRightToLeftRight root offset 16

                emailInput bottomToTop passwordInput offset 8
                emailInput leftRightToLeftRight root offset 16

                loginButton topToBottom passwordInput
                loginButton leftRightToLeftRight root

                registerButton topToBottom loginButton
                registerButton rightToRight root

                // logo image height must be automatic ?
                logoImage centerXToCenterX root
                logoImage.verticalCenterBetween(
                    top = root.top,
                    bottom = emailInput.top
                )

                copyrightText centerXToCenterX root
                copyrightText bottomToBottom root.safeArea offset 8
            }
        }
    }

    object Id {
        object EmailInputId : InputWidget.Id
        object PasswordInputId : InputWidget.Id
        object RegistrationButtonId : ButtonWidget.Id
    }
}
```

Code of theme:
```kotlin
val loginScreen = Theme(baseTheme) {
    factory[ConstraintWidget.DefaultCategory] = ConstraintViewFactory(
        padding = PaddingValues(16f),
        background = Background(
            fill = Fill.Solid(Colors.white)
        )
    )

    factory[InputWidget.DefaultCategory] = SystemInputViewFactory(
        margins = MarginValues(bottom = 8f),
        underLineColor = Color(0x000000DD),
        underLineFocusedColor = Color(0x3949ABFF),
        labelTextStyle = TextStyle(
            size = 12,
            color = Color(0x3949ABFF),
            fontStyle = FontStyle.BOLD
        ),
        errorTextStyle = TextStyle(
            size = 12,
            color = Color(0xB00020FF),
            fontStyle = FontStyle.BOLD
        ),
        textStyle = TextStyle(
            size = 16,
            color = Color(0x000000FF),
            fontStyle = FontStyle.MEDIUM
        )
    )

    val corners = platformSpecific(android = 8f, ios = 25f)

    factory[ButtonWidget.DefaultCategory] = SystemButtonViewFactory(
        margins = MarginValues(top = 32f),
        background = {
            val bg: (Color) -> Background = {
                Background(
                    fill = Fill.Solid(it),
                    shape = Shape.Rectangle(
                        cornerRadius = corners
                    )
                )
            }
            StateBackground(
                normal = bg(Color(0x6770e0FF)),
                pressed = bg(Color(0x6770e0EE)),
                disabled = bg(Color(0x6770e0BB))
            )
        }.invoke(),
        textStyle = TextStyle(
            color = Colors.white
        )
    )

    factory[LoginScreen.Id.RegistrationButtonId] = SystemButtonViewFactory(
        background = {
            val bg: (Color) -> Background = {
                Background(
                    fill = Fill.Solid(it),
                    shape = Shape.Rectangle(
                        cornerRadius = corners
                    )
                )
            }
            StateBackground(
                normal = bg(Color(0xFFFFFF00)),
                pressed = bg(Color(0xE7E7EEEE)),
                disabled = bg(Color(0x000000BB))
            )
        }.invoke(),
        margins = MarginValues(top = 16f),
        textStyle = TextStyle(
            color = Color(0x777889FF)
        ),
        androidElevationEnabled = false
    )
}
```

## Table of Contents
- [Features](#features)
- [Requirements](#requirements)
- [Versions](#versions)
- [Installation](#installation)
- [Usage](#usage)
- [Samples](#samples)
- [Set Up Locally](#setup-locally)
- [Contributing](#contributing)
- [License](#license)

## Features
- **compliance with platform rules**; 
- **declare structure, not rendering**;
- **compile-time safety**;
- **reactive data handling**.

## Requirements
- Gradle version 5.6.4+
- Android API 16+
- iOS version 9.0+

## Versions
- kotlin 1.3.50
  - 0.1.0-dev-1
- kotlin 1.3.60
  - 0.1.0-dev-2
  - 0.1.0-dev-3
  - 0.1.0-dev-4
  - 0.1.0-dev-5
- kotlin 1.3.61
  - 0.1.0-dev-6
  - 0.1.0-dev-7
  - 0.1.0-dev-8
  - 0.1.0-dev-9
  - 0.1.0-dev-10
  - 0.1.0-dev-11
  - 0.1.0-dev-12
  - 0.1.0-dev-13
  - 0.1.0-dev-14
  - 0.1.0-dev-15
- kotlin 1.3.70
  - 0.1.0-dev-16

## Installation
root build.gradle  
```groovy
allprojects {
    repositories {
        maven { url = "https://dl.bintray.com/icerockdev/moko" }
    }
}
```

project build.gradle
```groovy
dependencies {
    commonMainApi("dev.icerock.moko:widgets:0.1.0-dev-16")
}
```

### Codegen for new Widgets with @WidgetDef
root build.gradle  
```groovy
buildscript {
    repositories {
        maven { url = "https://dl.bintray.com/icerockdev/plugins" } // gradle plugin
    }

    dependencies {
        classpath "dev.icerock.moko.widgets:gradle-plugin:0.1.0-dev-16"
    }
}

allprojects {
    repositories {
        maven { url = uri("https://dl.bintray.com/icerockdev/plugins") } // compiler plugins
    }
}
```

project build.gradle
```groovy
apply plugin: "dev.icerock.mobile.multiplatform-widgets-generator"
```

## Usage
### Hello world
Multiplatform application definition at `mpp-library/src/commonMain/kotlin/App.kt`:
```kotlin
class App : BaseApplication() {
    override fun setup(): ScreenDesc<Args.Empty> {
        val theme = Theme()

        return registerScreen(HelloWorldScreen::class) { HelloWorldScreen(theme) }
    }
}
```

Screen definition `mpp-library/src/commonMain/kotlin/HelloWorldScreen.kt`: 
```kotlin
class HelloWorldScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>() {

    override fun createContentWidget() = with(theme) {
        container(size = WidgetSize.AsParent) {
            center {
                text(
                    size = WidgetSize.WrapContent,
                    text = const("Hello World!")
                )
            }
        }
    }
}
```

Result:

|Android|iOS|
|---|---|
|![HelloWorld Android](https://user-images.githubusercontent.com/5010169/69857402-84408e00-12c2-11ea-945a-5f287a754e67.png)|![HelloWorld iOS](https://user-images.githubusercontent.com/5010169/69857202-febcde00-12c1-11ea-8679-5b68b5b11c42.png)|

### Configure styles
Setup theme config:
```kotlin
val theme = Theme {
    factory[TextWidget.DefaultCategory] = SystemTextViewFactory(
        textStyle = TextStyle(
            size = 24,
            color = Colors.black
        ),
        padding = PaddingValues(padding = 16f)
    )
}
```
Result:

|Android|iOS|
|---|---|
|![Custom style Android](https://user-images.githubusercontent.com/5010169/69857575-d1bcfb00-12c2-11ea-9cbb-ee6b17357db2.png)|![Custom style iOS](https://user-images.githubusercontent.com/5010169/69857701-09c43e00-12c3-11ea-9e9d-181a298a7edf.png)|

### Bind data to UI
```kotlin
class TimerScreen(
    private val theme: Theme
) : WidgetScreen<Args.Empty>() {
    override fun createContentWidget(): Widget<WidgetSize.Const<SizeSpec.AsParent, SizeSpec.AsParent>> {
        val viewModel = getViewModel { TimerViewModel() }

        return with(theme) {
            container(size = WidgetSize.AsParent) {
                center {
                    text(
                        size = WidgetSize.WrapContent,
                        text = viewModel.text
                    )
                }
            }
        }
    }
}

class TimerViewModel : ViewModel() {
    private val iteration = MutableLiveData<Int>(0)
    val text: LiveData<StringDesc> = iteration.map { it.toString().desc() }

    init {
        viewModelScope.launch {
            while (isActive) {
                delay(1000)
                iteration.value = iteration.value + 1
            }
        }
    }
}
```

## Samples
Please see more examples in the [sample directory](sample).

## Set Up Locally 
- The [widgets directory](widgets) contains the `widgets` library;
- The [widgets-bottomsheet directory](widgets-bottomsheet) contains the `widgets-bottomsheet` library;
- The [widgets-sms directory](widgets-sms) contains the `widgets-sms` library;
- The [widgets-datetime-picker directory](widgets-datetime-picker) contains the `datetime-picker` library;
- The [widgets-collection directory](widgets-collection) contains the `collection` library;
- The [gradle-plugin directory](gradle-plugin) contains the gradle-plugin which apply compiler plugins for Native and JVM;
- The [kotlin-plugin directory](kotlin-plugin) contains the JVM compiler plugin with code-generation from @WidgetDef annotation;
- The [kotlin-native-plugin directory](kotlin-native-plugin) contains the Native compiler plugin with code-generation from @WidgetDef annotation;
- The [kotlin-common-plugin directory](kotlin-common-plugin) contains the common code of JVM and Native compiler plugins;
- The [sample directory](sample) contains sample apps for Android and iOS; plus the mpp-library connected to the apps;
- For local testing a library use:
  - `./publishToMavenLocal.sh`
  - `cd sample/ios-app`
  - `pod install`
  - sample apps priority use the locally published version
  - run android from `Android Studio` - module `android-app`, run iOS from xcode workspace `sample/ios-app/ios-app.xcworkspace`

## Contributing
All development (both new features and bug fixes) is performed in the `develop` branch. This way `master` always contains the sources of the most recently released version. Please send PRs with bug fixes to the `develop` branch. Documentation fixes in the markdown files are an exception to this rule. They are updated directly in `master`.

The `develop` branch is pushed to `master` on release.

For more details on contributing please see the [contributing guide](CONTRIBUTING.md).

## License
        
    Copyright 2019 IceRock MAG Inc.
    
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at
    
       http://www.apache.org/licenses/LICENSE-2.0
    
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
