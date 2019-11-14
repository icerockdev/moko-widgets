/*
* Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
*/

#import <UIKit/UITableViewCell.h>

@interface WidgetTableViewCell : UITableViewCell

+ (void)setViewFactory:(UIView *(^)(void))factory toReuseIdentifier:(NSString *)reuseIdentifer;

@end
