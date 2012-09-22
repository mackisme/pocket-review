package com.wadpam.rnr.dao;

import com.google.appengine.api.datastore.Key;
import com.wadpam.rnr.domain.DRating;
import com.wadpam.rnr.domain.DThumbs;

/**
 * Business Methods interface for entity DThumbs.
 * This interface is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2012-09-22T12:45:10.581+0700.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public interface DThumbsDao extends GeneratedDThumbsDao<Key, Key> {

    /**
     * Find thumbs done by a specific user and a specific product.
     * @param productId the product
     * @param username the user
     * @return a thumbs
     */
    public DThumbs findByProductIdUsername(String productId, String username);
}