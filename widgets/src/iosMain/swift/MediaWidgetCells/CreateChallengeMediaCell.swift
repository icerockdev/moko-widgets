//
//  CreateChallengeMediaCell.swift
//  GetChallenge
//
//  Created by Stanislav Rachenko on 19/07/2019.
//  Copyright © 2019 IceRock Development. All rights reserved.
//

import UIKit
import MultiPlatformLibraryBinderAdapter

class CreateChallengeMediaCell: UICollectionViewCell, Fillable {
    @IBOutlet weak var allContentView: UIView!
    let cellCornerRadius: CGFloat = 8
    let cellShadowOffset = CGSize(width: 0, height: 2)
    let cellShadowOpacity: CGFloat = 0.5
    let cellShadowColor = UIColor.black.cgColor
    let cellShadowRadius: CGFloat = 4
    static let reuseId = "CreateChallengeMediaCell"
    typealias DataType = CellModel
    var onTap: (() -> Void)?
    
    struct CellModel {
        let onTap: (() -> Void)?
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
    }
    
    @IBAction func tapCellAction(_ sender: Any) {
        onTap?()
    }
    
    func fill(_ data: CreateChallengeMediaCell.CellModel) {
        self.onTap = data.onTap
    }
    
    func update(_ data: CreateChallengeMediaCell.CellModel) {
    }
    
    override func prepareForReuse() {
    }
    
    override func layoutSubviews() {
        super.layoutSubviews()
        layoutShadow()
        layer.cornerRadius = cellCornerRadius
        allContentView.cornerRadius = cellCornerRadius
        allContentView.layer.masksToBounds = true
    }
    
    func layoutShadow() {
        for item in self.layer.sublayers ?? [] {
            if item.name == "shadowLayer" {
                item.removeFromSuperlayer()
            }
        }
        
        let newShadowLayer = CAShapeLayer()
        newShadowLayer.name = "shadowLayer"
        newShadowLayer.path = UIBezierPath(roundedRect: self.bounds, cornerRadius: cellCornerRadius).cgPath
        newShadowLayer.fillColor = UIColor.clear.cgColor
        newShadowLayer.shadowPath = newShadowLayer.path
        newShadowLayer.shadowOffset = cellShadowOffset
        newShadowLayer.shadowColor = cellShadowColor
        newShadowLayer.shadowOpacity = Float(cellShadowOpacity)
        newShadowLayer.shadowRadius = cellShadowRadius
        layer.insertSublayer(newShadowLayer, at: 0)
    }
}
