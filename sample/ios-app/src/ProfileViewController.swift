/*
* Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
*/

import UIKit
import MultiPlatformLibrary
import MultiPlatformLibraryMvvm

class ProfileViewController: UIViewController {
    @IBOutlet var textLabel: UILabel!
    
    var profileScreen: ProfileScreen!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        textLabel.bindText(liveData: profileScreen.profileViewModel.text)
    }
}
