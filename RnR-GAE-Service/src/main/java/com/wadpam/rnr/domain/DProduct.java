/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wadpam.rnr.domain;


import com.google.appengine.api.datastore.GeoPt;
import net.sf.mardao.api.domain.AEDStringEntity;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.util.Collection;

/**
 *
 * @author os
 */
@Entity
public class DProduct extends AEDStringEntity implements Serializable {   // TODO: Implement GeoModel

    private static final long serialVersionUID = -7313484361958303203L;

    /** The Many-To-One productId (unconstrained) */
    @Id
    private String      productId;

    /** The total sum or all ratings */
    @Basic
    private Long        ratingSum = 0L;

    /** The total number of ratings */
    @Basic
    private Long        ratingCount = 0L;

    /** The total number of likes */
    @Basic
    private Long        likeCount = 0L;

    /** The total number of comments */
    @Basic
    private Long        commentCount = 0L;

    /** The location of the product */
    @Basic
    private GeoPt       location;

    @Basic
    /** used by GeoDao to index boxes */
    private Collection<Long> geoboxes;


    @Override
    public String getSimpleKey() {
        return productId;
    }

    @Override
    public String toString() {
        return String.format("{productId:%s, ratings:%d, average:%d, likes:%d, location:%s}",
                productId, ratingCount, getRatingAverage(), likeCount, location);
    }

    // Needed for geolocation  // TODO: Uncomment overrides and implement getId()
    public String getId() {
        return this.productId;
    }

//    @Override
    public float getLatitude() {
        return null != location ? location.getLatitude() : 0;
    }

//    @Override
    public float getLongitude() {
        return null != location ? location.getLongitude() : 0;
    }

//    @Override
    public void setGeoboxes(Collection<Long> geoboxes) {
        this.geoboxes = geoboxes;
    }

    public Collection<Long> getGeoboxes() {
        return geoboxes;
    }

    // Getters and setters
    public int getRatingAverage() {
        return 0 < ratingCount ? (int) (ratingSum / ratingCount) : -1;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Long getRatingSum() {
        return ratingSum;
    }

    public void setRatingSum(Long ratingSum) {
        this.ratingSum = ratingSum;
    }

    public Long getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
    }

    public Long getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Long likeCount) {
        this.likeCount = likeCount;
    }

    public Long getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(Long commentCount) {
        this.commentCount = commentCount;
    }

    public GeoPt getLocation() {
        return location;
    }

    public void setLocation(GeoPt location) {
        this.location = location;
    }
}