Pod::Spec.new do |spec|
    spec.name                     = 'mppLibraryIos'
    spec.version                  = '0.1.0'
    spec.homepage                 = 'Link'
    spec.source                   = { :git => "Not Published", :tag => "Cocoapods/#{spec.name}/#{spec.version}" }
    spec.authors                  = 'IceRock Development'
    spec.license                  = ''
    spec.summary                  = 'Shared code between iOS and Android'
    spec.module_name              = "#{spec.name}"

    spec.source_files             = "src/iosMain/swift/**/*.{h,m,swift}"
    spec.resources                = "src/iosMain/resources/**/*"

    spec.ios.deployment_target  = '11.0'
    spec.swift_version          = '5.0'

    spec.pod_target_xcconfig = {
        'VALID_ARCHS' => '$(ARCHS_STANDARD_64_BIT)'
    }
end
