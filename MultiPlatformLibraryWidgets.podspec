Pod::Spec.new do |spec|
    spec.name                     = 'MultiPlatformLibraryUnits'
    spec.version                  = '0.1.0'
    spec.homepage                 = 'https://github.com/icerockdev/moko-units'
    spec.source                   = { :git => "https://github.com/icerockdev/moko-units.git", :tag => "release/#{spec.version}" }
    spec.authors                  = 'IceRock Development'
    spec.license                  = { :type => 'Apache 2', :file => 'LICENSE.md' }
    spec.summary                  = 'Swift additions to moko-units Kotlin/Native library'
    spec.module_name              = "#{spec.name}"
    
    spec.dependency 'MultiPlatformLibrary'

    spec.ios.deployment_target  = '9.0'
    spec.swift_version = '4.2'

    spec.default_subspec = 'Core'

    spec.subspec 'Core' do |sp|
      sp.source_files = "units/src/iosMain/swift/Core/**/*.{h,m,swift}"
    end

    spec.subspec 'R.swift' do |sp|
      sp.source_files = "units/src/iosMain/swift/R.swift/**/*.{h,m,swift}"
      sp.dependency 'R.swift'
    end

    spec.pod_target_xcconfig = {
        'VALID_ARCHS' => '$(ARCHS_STANDARD_64_BIT)'
    }
end
