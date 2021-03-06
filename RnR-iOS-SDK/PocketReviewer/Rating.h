//
//  Rating.h
//
//  Created by Mattias Levin on 5/31/12.
//  Copyright (c) 2012 Wadpam. All rights reserved.
//

#import <Foundation/Foundation.h>


/**
 The rating of an item.
 */
@interface Rating : NSObject


/**
 Create a new Rating object.
 @return A Rating object
 */
+ (Rating*)rating;

/**
 The unique item id.
 */
@property (nonatomic, retain) NSString *itemId;


/**
 The items latitue.
 */
@property (nonatomic) float latitude;


/**
 The items longitude.
 */
@property (nonatomic) float longitude;


/**
 The average rating.
 */
@property (nonatomic) float averageRating;


/**
 The total number of ratings.
 */
@property (nonatomic) NSInteger numberOfRatings;



@end
