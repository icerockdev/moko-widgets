![moko-widgets](https://user-images.githubusercontent.com/5010169/70204294-93a45900-1752-11ea-9bb6-820d514ceef9.png)  
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) [![Download](https://api.bintray.com/packages/icerockdev/moko/moko-widgets/images/download.svg) ](https://bintray.com/icerockdev/moko/moko-units/_latestVersion) ![kotlin-version](https://img.shields.io/badge/kotlin-1.3.60-orange)

# Mobile Kotlin widgets
This is a Kotlin MultiPlatform library that provides declarative UI and application screens management
 in common code. You can implement full application for Android and iOS only from common code with it.  

## Current status
Current version - `0.1.0-dev-5`. Dev version is not tested in production tasks yet, API can be changed and
 bugs may be found. But dev version is chance to test limits of API and concepts to feedback and improve lib.
 We open for any feedback and ideas (go to issues or #moko at [kotlinlang.slack.com](https://kotlinlang.slack.com))!

## Roadmap
- December-January: Test library in real project;
- February: 0.1.0 release with flexible API; production usage at IceRock;
- First half of 2019: more widgets, more factories; figma template and generation of screens.

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
                image = const(Image.resource(MR.images.logo))
            )

            val emailInput = +input(
                size = WidgetSize.WidthAsParentHeightWrapContent,
                id = Id.EmailInputId,
                label = const("Email".desc() as StringDesc),
                field = viewModel.emailField
            )
            val passwordInput = +input(
                size = WidgetSize.WidthAsParentHeightWrapContent,
                id = Id.PasswordInputId,
                label = const("Password".desc() as StringDesc),
                field = viewModel.passwordField
            )
            val loginButton = +button(
                size = WidgetSize.Const(SizeSpec.AsParent, SizeSpec.Exact(50f)),
                text = const("Login".desc() as StringDesc),
                onTap = viewModel::onLoginPressed
            )

            val registerButton = +button(
                id = Id.RegistrationButtonId,
                size = WidgetSize.Const(SizeSpec.WrapContent, SizeSpec.Exact(40f)),
                text = const("Registration".desc() as StringDesc),
                onTap = viewModel::onRegistrationPressed
            )

            constraints {
                passwordInput centerYToCenterY root
                passwordInput leftRightToLeftRight root

                emailInput bottomToTop passwordInput
                emailInput leftRightToLeftRight root

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
    constraintFactory = DefaultConstraintWidgetViewFactory(
        DefaultConstraintWidgetViewFactoryBase.Style(
            padding = PaddingValues(16f),
            background = Background(
                fill = Fill.Solid(Colors.white)
            )
        )
    )

    imageFactory = DefaultImageWidgetViewFactory(
        DefaultImageWidgetViewFactoryBase.Style(
            scaleType = DefaultImageWidgetViewFactoryBase.ScaleType.FIT
        )
    )

    inputFactory = DefaultInputWidgetViewFactory(
        DefaultInputWidgetViewFactoryBase.Style(
            margins = MarginValues(bottom = 8f),
            underLineColor = Color(0xe5e6eeFF),
            labelTextStyle = TextStyle(
                color = Color(0x777889FF)
            )
        )
    )

    val corners = platformSpecific(android = 8f, ios = 25f)

    buttonFactory = DefaultButtonWidgetViewFactory(
        DefaultButtonWidgetViewFactoryBase.Style(
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
    )

    setButtonFactory(
        DefaultButtonWidgetViewFactory(
            DefaultButtonWidgetViewFactoryBase.Style(
                margins = MarginValues(top = 16f),
                padding = platformSpecific(
                    ios = PaddingValues(start = 16f, end = 16f),
                    android = null
                ),
                background = {
                    val bg: (Color) -> Background = {
                        Background(
                            fill = Fill.Solid(it),
                            border = Border(
                                color = Color(0xF2F2F8FF),
                                width = 2f
                            ),
                            shape = Shape.Rectangle(cornerRadius = corners)
                        )
                    }
                    StateBackground(
                        normal = bg(Colors.white),
                        pressed = bg(Color(0xEEEEEEFF)),
                        disabled = bg(Color(0xBBBBBBFF))
                    )
                }.invoke(),
                textStyle = TextStyle(
                    color = Color(0x777889FF)
                )
            )
        ),
        LoginScreen.Id.RegistrationButtonId
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
- Gradle version 5.4.1+
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
    commonMainApi("dev.icerock.moko:widgets:0.1.0-dev-5")
}
```

settings.gradle  
```groovy
enableFeaturePreview("GRADLE_METADATA")
```

### Codegen for new Widgets with @WidgetDef
root build.gradle  
```groovy
buildscript {
    repositories {
        maven { url = "https://dl.bintray.com/icerockdev/plugins" } // gradle plugin
    }

    dependencies {
        classpath "dev.icerock.moko.widgets:gradle-plugin:0.1.0-dev-5"
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
    override fun setup() {
        val theme = Theme()

        registerScreenFactory(HelloWorldScreen::class) { HelloWorldScreen(theme) }
    }

    override fun getRootScreen(): KClass<out Screen<Args.Empty>> {
        return HelloWorldScreen::class
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
    textFactory = DefaultTextWidgetViewFactory(
        DefaultTextWidgetViewFactoryBase.Style(
            textStyle = TextStyle(
                size = 24,
                color = Colors.black
            ),
            padding = PaddingValues(padding = 16f)
        )
    )
}
```
Result:

|Android|iOS|
|---|---|
|![Custom style Android](https://user-images.githubusercontent.com/5010169/69857575-d1bcfb00-12c2-11ea-9cbb-ee6b17357db2.png)|![Custom style iOS](https://user-images.githubusercontent.com/5010169/69857701-09c43e00-12c3-11ea-9e9d-181a298a7edf.png)|

## Samples
Please see more examples in the [sample directory](sample).

## Set Up Locally 
- The [widgets directory](widgets) contains the `widgets` library;
- The [gradle-plugin directory](gradle-plugin) contains the gradle-plugin which apply compiler plugins for Native and JVM;
- The [kotlin-plugin directory](kotlin-plugin) contains the JVM compiler plugin with code-generation from @WidgetDef annotation;
- The [kotlin-native-plugin directory](kotlin-native-plugin) contains the Native compiler plugin with code-generation from @WidgetDef annotation;
- The [kotlin-common-plugin directory](kotlin-common-plugin) contains the common code of JVM and Native compiler plugins;
- The [sample directory](sample) contains sample apps for Android and iOS; plus the mpp-library connected to the apps;
- For local testing a library use:
  - `./gradlew -PpluginPublish publishPluginPublicationToMavenLocal`
  - `./gradlew -PlibraryPublish publishToMavenLocal` 
  - sample apps priority use the locally published version.

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
