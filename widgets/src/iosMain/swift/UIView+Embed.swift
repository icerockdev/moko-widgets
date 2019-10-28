//
//  UIView+Embed.swift
//  GetChallenge
//
//  Created by Stanislav Rachenko on 26/06/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

import Foundation
import UIKit

public enum Direction {
    case vertical
    case horizontal
    case none
    case both
}

public extension UIScrollView {
    func fillScrollWith(view: UIView, scrollDirection: Direction, insets: UIEdgeInsets = UIEdgeInsets.zero) {
        self.fillWith(view: view, insets: insets)
        self.addScrollConstraints(view: view, scrollDirection: scrollDirection, insets: insets)
    }
    
    private func addScrollConstraints(view: UIView, scrollDirection: Direction, insets: UIEdgeInsets) {
        switch scrollDirection {
        case .vertical:
            view.fixSize(width: self.frame.width - insets.left - insets.right)
            allignWith(view: view, direction: .horizontal)
            break
        case .horizontal:
            view.fixSize(height: self.frame.height - insets.top - insets.bottom)
            allignWith(view: view, direction: .vertical)
            break
        case .none:
            view.fixSize(width: self.frame.width - insets.left - insets.right, height: self.frame.height - insets.top - insets.bottom)
            allignWith(view: view, direction: .horizontal)
            allignWith(view: view, direction: .vertical)
            break
        case .both:
            break
        }
    }
}

public extension UIView {
    
    public func fillWith(view: UIView) {
        self.fillWith(view: view, insets: UIEdgeInsets.zero)
    }
    
    public func fillWith(view: UIView, pinning: [UIRectEdge]) {
        view.translatesAutoresizingMaskIntoConstraints = false
        
        self.addSubview(view)
        
        if (pinning.contains(.top)) {
            let pinTop = NSLayoutConstraint(item: view, attribute: .top, relatedBy: .equal,
                                            toItem: self, attribute: .top, multiplier: 1.0, constant: 0)
            self.addConstraint(pinTop)
        }
        
        if (pinning.contains(.bottom)) {
            let pinBottom = NSLayoutConstraint(item: view, attribute: .bottom, relatedBy: .equal,
                                               toItem: self, attribute: .bottom, multiplier: 1.0, constant: 0)
            self.addConstraint(pinBottom)
        }
        
        if (pinning.contains(.left)) {
            let pinLeading = NSLayoutConstraint(item: view, attribute: .leading, relatedBy: .equal,
                                                toItem: self, attribute: .leading, multiplier: 1.0, constant: 0)
            self.addConstraint(pinLeading)
        }
        
        if (pinning.contains(.right)) {
            let pinTrailing = NSLayoutConstraint(item: view, attribute: .trailing, relatedBy: .equal,
                                                 toItem: self, attribute: .trailing, multiplier: 1.0, constant: 0)
            self.addConstraint(pinTrailing)
        }
    }
    
   public func fillWith(view: UIView, insets: UIEdgeInsets, pinning: [UIRectEdge]) {
        view.translatesAutoresizingMaskIntoConstraints = false
        
        self.addSubview(view)
        
        if (pinning.contains(.top)) {
            let pinTop = NSLayoutConstraint(item: view, attribute: .top, relatedBy: .equal,
                                            toItem: self, attribute: .top, multiplier: 1.0, constant: insets.top)
            self.addConstraint(pinTop)
        }
        
        if (pinning.contains(.bottom)) {
            let pinBottom = NSLayoutConstraint(item: view, attribute: .bottom, relatedBy: .equal,
                                               toItem: self, attribute: .bottom, multiplier: 1.0, constant: insets.bottom)
            self.addConstraint(pinBottom)
        }
        
        if (pinning.contains(.left)) {
            let pinLeading = NSLayoutConstraint(item: view, attribute: .leading, relatedBy: .equal,
                                                toItem: self, attribute: .leading, multiplier: 1.0, constant: insets.left)
            self.addConstraint(pinLeading)
        }
        
        if (pinning.contains(.right)) {
            let pinTrailing = NSLayoutConstraint(item: view, attribute: .trailing, relatedBy: .equal,
                                                 toItem: self, attribute: .trailing, multiplier: 1.0, constant: insets.right)
            self.addConstraint(pinTrailing)
        }
    }
    
    public func fillWith(view: UIView, insets: UIEdgeInsets) {
        view.translatesAutoresizingMaskIntoConstraints = false
        
        self.addSubview(view)
        
        let pinTop = NSLayoutConstraint(item: view, attribute: .top, relatedBy: .equal,
                                        toItem: self, attribute: .top, multiplier: 1.0, constant: insets.top)
        
        let pinBottom = NSLayoutConstraint(item: self, attribute: .bottom, relatedBy: .equal,
                                           toItem: view, attribute: .bottom, multiplier: 1.0, constant: insets.bottom)
        
        let pinLeading = NSLayoutConstraint(item: view, attribute: .leading, relatedBy: .equal,
                                            toItem: self, attribute: .leading, multiplier: 1.0, constant: insets.left)
        
        let pinTrailing = NSLayoutConstraint(item: self, attribute: .trailing, relatedBy: .equal,
                                             toItem: view, attribute: .trailing, multiplier: 1.0, constant: insets.right)
        
        self.addConstraints([pinTop, pinBottom, pinLeading, pinTrailing])
    }
    
    func fixSize(width: CGFloat? = nil, height: CGFloat? = nil) {
        if let width = width {
            let fixWidth = NSLayoutConstraint(item: self, attribute: .width, relatedBy: .equal,
                                              toItem: nil, attribute: .notAnAttribute, multiplier: 1.0, constant: width)
            self.addConstraint(fixWidth)
        }
        
        if let height = height {
            let fixHeight = NSLayoutConstraint(item: self, attribute: .height, relatedBy: .equal,
                                               toItem: nil, attribute: .notAnAttribute, multiplier: 1.0, constant: height)
            self.addConstraint(fixHeight)
        }
    }
    
    public func allignWith(view: UIView, direction: Direction) {
        switch direction {
            case .vertical:
                self.centerYAnchor.constraint(equalTo: view.centerYAnchor).isActive = true
                break
            case .horizontal:
                self.centerXAnchor.constraint(equalTo: view.centerXAnchor).isActive = true
                break
            case .none:
                break
            case .both:
                self.allignWith(view: view, direction: .horizontal)
                self.allignWith(view: view, direction: .vertical)
                break
        }
    }
    
    func pin(view1: UIView, view2: UIView, inset: CGFloat, direction: Direction) {
        switch direction {
        case .vertical:
            let pinTop = NSLayoutConstraint(item: view1, attribute: .bottom, relatedBy: .equal,
                                                toItem: view2, attribute: .top, multiplier: 1.0, constant: -inset)
            self.addConstraint(pinTop)
            break
        case .horizontal:
            let pinLeading = NSLayoutConstraint(item: view1, attribute: .trailing, relatedBy: .equal,
                                                toItem: view2, attribute: .leading, multiplier: 1.0, constant: -inset)
            self.addConstraint(pinLeading)
            break
        case .none:
            break
        case .both:
            pin(view1: view1, view2: view2, inset: inset, direction: .vertical)
            pin(view1: view1, view2: view2, inset: inset, direction: .horizontal)
            break
        }
    }
    
    public func pinAllign(view1: UIView, view2: UIView, inset: CGFloat, direction: Direction) {
        self.pin(view1: view1, view2: view2, inset: inset, direction: direction)
        switch direction {
        case .vertical:
            view1.allignWith(view: view2, direction: .horizontal)
            break
        case .horizontal:
            view1.allignWith(view: view2, direction: .vertical)
            break
        case .none:
            break
        case .both:
            view1.allignWith(view: view2, direction: .vertical)
            view1.allignWith(view: view2, direction: .horizontal)
            break
        }
    }
}
