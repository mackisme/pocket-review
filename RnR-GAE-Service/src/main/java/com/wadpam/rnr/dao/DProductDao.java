package com.wadpam.rnr.dao;

import com.google.appengine.api.datastore.Key;
import com.wadpam.rnr.domain.DProduct;

import java.util.Collection;

/**
 * Business Methods interface for entity DProduct.
 * This interface is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2012-08-05T20:54:54.772+0700.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public interface DProductDao extends GeneratedDProductDao<Key, Key> {

    /**
     * Find most liked product
     * @param limit the number of products to max return
     * @return A list of products sorted according to the number of likes
     */
    public Collection<DProduct> findMostLiked(int limit);

    /**
     * Find most commented product
     * @param limit the number of products to max return
     * @return A list of products sorted according to the number of comments
     */
    public Collection<DProduct> findMostCommented(int limit);

    /**
     * Find most rated product
     * @param limit the number of product to max return
     * @return A list of products sorted according to the number of ratings
     */
    public Collection<DProduct> findMostRated(int limit);

    /**
     * Find product with highest average rating
     * @param limit the number of product to max return
     * @return A list of products sorted according to average rating
     */
    public Collection<DProduct> findTopRated(int limit);
}
