/*
 * Copyright 2020 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit
import SDWebImage

@objc public class ImageNetwork: NSObject {
  
  @objc public static func loadImage(
    view: UIView,
    url: String,
    placeholder: UIImage?,
    setImageBlock: @escaping (UIImage?) -> Void
  ) {
    let tag = url.hashValue
    view.tag = tag

    guard let nsUrl = URL(string: url) else {
      print("can't parse url \"\(url)\"")
      setImageBlock(placeholder)
      return
    }
    
    view.sd_internalSetImage(
      with: nsUrl,
      placeholderImage: placeholder,
      options: SDWebImageOptions(),
      context: nil,
      setImageBlock: { (image, _, _, _) in
        if view.tag != tag { return } // view reused

        setImageBlock(image)
    },
      progress: nil,
      completed: nil
    )
  }
}
