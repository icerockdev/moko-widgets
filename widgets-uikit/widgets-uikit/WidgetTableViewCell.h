/*
* Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
*/

#import <UIKit/UITableViewCell.h>

@interface WidgetFactoryResult: NSObject

@property (nonnull) UIView *view;
@property (nonnull) id cellTag;

@end

@interface WidgetTableViewCell : UITableViewCell

@property (nonnull) id cellTag;

+ (void)setViewFactory:(nonnull WidgetFactoryResult *(^)(UIViewController *))factory
     toReuseIdentifier:(nonnull NSString *)reuseIdentifer;

@end
