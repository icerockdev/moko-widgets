![moko-units](img/logo.png)  
[![GitHub license](https://img.shields.io/badge/license-Apache%20License%202.0-blue.svg?style=flat)](http://www.apache.org/licenses/LICENSE-2.0) [![Download](https://api.bintray.com/packages/icerockdev/moko/moko-units/images/download.svg) ](https://bintray.com/icerockdev/moko/moko-units/_latestVersion) ![kotlin-version](https://img.shields.io/badge/kotlin-1.3.50-orange)

# Mobile Kotlin units
This is a Kotlin MultiPlatform library that provides RecyclerView/UITableView/UICollectionView filling from common code.

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
- **** .
TODO

## Requirements
- Gradle version 5.4.1+
- Android API 21+
- iOS version 9.0+

## Versions
- kotlin 1.3.50
  - 0.1.0
  - 0.1.1

## Installation
root build.gradle  
```groovy
buildscript {
    repositories {
        maven { url = "https://dl.bintray.com/icerockdev/plugins" }
    }

    dependencies {
        classpath "dev.icerock.moko:units-generator:0.1.1"
    }
}


allprojects {
    repositories {
        maven { url = "https://dl.bintray.com/icerockdev/moko" }
    }
}
```

project build.gradle
```groovy
apply plugin: "dev.icerock.mobile.multiplatform-units"

dependencies {
    commonMainApi("dev.icerock.moko:units:0.1.1")
}

multiplatformUnits {
    classesPackage = "org.example.library.units"
    dataBindingPackage = "org.example.library"
    layoutsSourceSet = "androidMain"
}
```

settings.gradle  
```groovy
enableFeaturePreview("GRADLE_METADATA")
```

On iOS, in addition to the Kotlin library add Pod in the Podfile.
```ruby
pod 'MultiPlatformLibraryUnits', :git => 'https://github.com/icerockdev/moko-units.git', :tag => 'release/0.1.1'
```
**`MultiPlatformLibraryUnits` CocoaPod requires that the framework compiled from Kotlin be named 
`MultiPlatformLibrary` and be connected as a CocoaPod `MultiPlatformLibrary`. 
[Here](sample/ios-app/Podfile)'s an example.
To simplify integration with MultiPlatformFramework you can use [mobile-multiplatform-plugin](https://github.com/icerockdev/mobile-multiplatform-gradle-plugin)**.  
`MultiPlatformLibraryUnits` CocoaPod contains an `DataSource`s for `UITableView`/`UICollectionView`.

## Usage
common:
```kotlin
interface UnitFactory {
    fun createHeader(text: String): UnitItem
    fun createProfileTile(profileId: Long, avatarUrl: String, username: String): UnitItem
}

class ViewModel(unitFactory: UnitFactory) {
    val items = listOf(
        unitFactory.createHeader("Programmers"),
        unitFactory.createProfileTile(1, "url", "Mikhailov"),
        unitFactory.createProfileTile(2, "url", "Babenko"),
        unitFactory.createProfileTile(3, "url", "Tchernov"),
        unitFactory.createHeader("Designers"),
        unitFactory.createProfileTile(4, "url", "Eugeny")
    )
}
```

android:
```xml
<androidx.recyclerview.widget.RecyclerView
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:bindValue="@{viewModel.items}"
            app:adapter="@{`dev.icerock.moko.units.adapter.UnitsRecyclerViewAdapter`}"
            app:layoutManager="androidx.recyclerview.widget.LinearLayoutManager"/>
```
```kotlin
object UnitFactoryImpl: UnitFactory {
    fun createHeader(text: String): UnitItem {
        return LayoutHeader()
            .setText(text)
            .setItemId(text.hashCode())
    }
    
    fun createProfileTile(profileId: Long, avatarUrl: String, username: String): UnitItem {
        return LayoutProfileTile()
            .setAvatarUrl(avatarUrl)
            .setUserName(username)
            .setItemId(profileId)
    }
}
```
```kotlin
mBinding.viewModel = ViewModel(UnitFactoryImpl)
```

iOS:
```swift
class UnitFactoryImpl: NSObject, UnitFactory {

  func createHeader(text: String) -> UnitItem {
    let data = HeaderTableViewCell.CellModel(text: text)
    return UIBindingTableViewCellUnit<HeaderTableViewCell>(
      data: data,
      reusable: R.nib.headerTableViewCell,
      configurator: nil,
      height: 48)
  }
  
  func createProfileTile(profileId: Long, avatarUrl: String, username: String) -> UnitItem {
    let data = ProfileTableViewCell.CellModel(avatarUrl: avatarUrl, username: username)
    return UIBindingTableViewCellUnit<ProfileTableViewCell>(
      data: data,
      reusable: R.nib.profileTableViewCell,
      configurator: nil,
      height: 56)
  }
}
```
```swift
let viewModel = ViewModel(unitFactory: UnitFactoryImpl())
let tableDataSource = FlatUnitTableViewDataSource()
unitTableViewDelegate = tableDataSource.setup(for: tableView)

tableDataSource.units = viewModel.items
tableView.reloadTable()
```

## Samples
Please see more examples in the [sample directory](sample).

## Set Up Locally 
- The [units directory](units) contains the `units` library;
- The [gradle-plugin directory](gradle-plugin) contains a gradle plugin with a `MR` class generator;
- The [sample directory](sample) contains sample apps for Android and iOS; plus the mpp-library connected to the apps;
- For local testing a library use the `:units:publishToMavenLocal` gradle task - so that sample apps use the locally published version.
- For local testing a plugin use the `:gradle-plugin:publishToMavenLocal` gradle task so that sample apps will use the locally published version.

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
