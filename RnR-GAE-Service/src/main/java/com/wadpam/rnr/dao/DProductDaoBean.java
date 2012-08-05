package com.wadpam.rnr.dao;

import com.wadpam.rnr.domain.DProduct;

import java.util.Collection;

/**
 * Implementation of Business Methods related to entity DProduct.
 * This (empty) class is generated by mardao, but edited by developers.
 * It is not overwritten by the generator once it exists.
 *
 * Generated on 2012-08-05T20:54:54.772+0700.
 * @author mardao DAO generator (net.sf.mardao.plugin.ProcessDomainMojo)
 */
public class DProductDaoBean 
	extends GeneratedDProductDaoImpl
		implements DProductDao 
{

    // Find most liked products
    @Override
    public Collection<DProduct> findMostLiked(int limit) {
        return findBy(COLUMN_NAME_LIKECOUNT, false, limit, 0);
    }

    // Find most commented products
    @Override
    public Collection<DProduct> findMostCommented(int limit) {
        return findBy(COLUMN_NAME_COMMENTCOUNT, false, limit, 0);
    }

    // Find most rated products
    @Override
    public Collection<DProduct> findMostRated(int limit) {
        return findBy(COLUMN_NAME_RATINGCOUNT, false, limit, 0);
    }

    // Find products with highest average rating
    @Override
    public Collection<DProduct> findTopRated(int limit) {
        return findBy(COLUMN_NAME_RATINGAVERAGE, false, limit, 0);
    }
}
