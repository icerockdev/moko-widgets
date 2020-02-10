Pod::Spec.new do |spec|
    spec.name                     = 'moko-widgets-bottom-sheet'
    spec.version                  = '0.1.0'
    spec.homepage                 = 'https://github.com/icerockdev/moko-widgets'
    spec.source                   = { :git => "https://github.com/icerockdev/moko-widgets.git", :tag => "release/#{spec.version}" }
    spec.authors                  = 'IceRock Development'
    spec.license                  = { :type => 'Apache 2', :file => 'LICENSE.md' }
    spec.summary                  = 'Swift additions to moko-widgets Kotlin/Native library'
    spec.module_name              = "mokoWidgetsBottomSheet"

    spec.source_files             = "widgets-bottomSheet/src/iosMain/swift/**/*.{h,m,swift}"
    spec.resources                = "widgets-bottomSheet/src/iosMain/resources/**/*"

    spec.dependency 'FloatingPanel'

    spec.ios.deployment_target  = '11.0'
    spec.swift_version          = '5.0'

    spec.pod_target_xcconfig = {
        'VALID_ARCHS' => '$(ARCHS_STANDARD_64_BIT)'
    }
end
