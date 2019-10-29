/*
 * Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
 */

import UIKit

import MultiPlatformLibrary
import MultiPlatformLibraryBinderAdapter

class ChallengeMediaCell: UICollectionViewCell, Fillable {
    static let reuseId = "ChallengeMediaCell"
    typealias DataType = CellModel
    let cellCornerRadius: CGFloat = 8
    let cellShadowOffset = CGSize(width: 0, height: 0)
    let cellShadowOpacity: CGFloat = 0.4
    let cellShadowColor = UIColor.black.cgColor
    let cellShadowRadius: CGFloat = 6
    @IBOutlet weak var allContentView: UIView!
    @IBOutlet var mediaPreview: UIImageView!
    @IBOutlet var deleteButton: UIButton!
    @IBOutlet weak var playButton: UIImageView!
    var onTap: (() -> Void)?
    var onDelete: (() -> Void)?
    
    struct CellModel {
        let image: UIImage?
        let isPhoto: Bool
        let isDeleteHidden: Bool
        let onTap: (() -> Void)?
        let onDelete: (() -> Void)?
    }
    
    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    @IBAction func deleteButtonAction(_ sender: Any) {
       onDelete?()
    }
    
    @IBAction func tapCellAction(_ sender: Any) {
        onTap?()
    }
    
    func fill(_ data: ChallengeMediaCell.CellModel) {
        mediaPreview.image = data.image
        self.onTap = data.onTap
        self.onDelete = data.onDelete
        self.playButton.isHidden = data.isPhoto
        deleteButton.isHidden = data.isDeleteHidden
    }
    
    func update(_ data: ChallengeMediaCell.CellModel) {
        fill(data)
    }
    
    override func prepareForReuse() {
        mediaPreview.image = nil
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
