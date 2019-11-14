/*
* Copyright 2019 IceRock MAG Inc. Use of this source code is governed by the Apache 2.0 license.
*/

#import "WidgetTableViewCell.h"
#import <UIKit/UIKit.h>

static NSMutableDictionary *viewFactoryHolder = NULL;

__attribute__((constructor))
static void initialize_viewFactoryHolder() {
    viewFactoryHolder = [[NSMutableDictionary alloc] init];
}

@implementation WidgetTableViewCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(nullable NSString *)reuseIdentifier {
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        if(reuseIdentifier == nil) {
            return self;
        }
      
        UIView *(^viewFactory)(void) = viewFactoryHolder[reuseIdentifier];
        UIView *widgetView = viewFactory();

        [self.contentView addSubview:widgetView];

        [widgetView.leadingAnchor constraintEqualToAnchor:self.contentView.leadingAnchor].active = true;
        [widgetView.trailingAnchor constraintEqualToAnchor:self.contentView.trailingAnchor].active = true;
        [widgetView.topAnchor constraintEqualToAnchor:self.contentView.topAnchor].active = true;
        [widgetView.bottomAnchor constraintEqualToAnchor:self.contentView.bottomAnchor].active = true;
    }

    return self;
}

+ (void)setViewFactory:(UIView *(^)(void))factory toReuseIdentifier:(nonnull NSString *)reuseIdentifer {
    [viewFactoryHolder setObject:factory forKey:reuseIdentifer];
}

@end


