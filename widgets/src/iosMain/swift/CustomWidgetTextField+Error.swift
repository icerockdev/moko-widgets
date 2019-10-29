/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import MultiPlatformLibrary
import MultiPlatformLibraryCore

extension CustomWidgetTextField {
    public func bindError(liveData: LiveData<NSString>) {
        setError(text: liveData.value)
        liveData.addObserver { [weak self] text in
            self?.setError(text: text)
        }
    }
    
    public func bindError(liveData: LiveData<StringDesc>) {
        setError(text: liveData.value)
        liveData.addObserver { [weak self] text in
            self?.setError(text: text)
        }
    }
    
    private func setError(text: StringDesc?) {
        setError(text: text?.localized() as? NSString)
    }
    
    private func setError(text: NSString?) {
        guard let stringValue = text as? String else {
            errorText = nil
            return
        }
        
        if(errorText?.compare(stringValue) == ComparisonResult.orderedSame) { return }
        
        errorText = stringValue
    }
}
