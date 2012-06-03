package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.json.JRating;
import com.wadpam.rnr.json.JResult;
import com.wadpam.rnr.json.JResultPage;
import com.wadpam.rnr.service.RnrService;
import java.util.Collection;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * The rating controller implements all REST methods related to ratings and reviews
 * @author os
 */
@Controller
@RequestMapping(value="{domain}/rating")
public class RatingController {
    static final Logger LOG = LoggerFactory.getLogger(RatingController.class);
    
    private RnrService rnrService;

    /**
     * Adds the rating for specified productId and username
     * @param productId domain-unique id for the product to rate
     * @param username optional
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @param review optional review text
     * @param rating 0..100
     * @return the new average for this product
     */
    @RestReturn(value=JResult.class, entity=JResult.class, code={
        @RestCode(code=200, message="OK", description="Rating added")
    })
    @RequestMapping(value="{productId}", method= RequestMethod.POST)
    public ResponseEntity<JResult> addRating(HttpServletRequest request,
            @PathVariable String productId,
            @RequestParam(required=false) String username,
            @RequestParam(required=false) Float latitude,
            @RequestParam(required=false) Float longitude,
            @RequestParam(required=false) String review,
            @RequestParam int rating) {
        final JResult body = rnrService.addRating(productId, username, 
                latitude, longitude, rating);
        return new ResponseEntity<JResult>(body, HttpStatus.OK);
    }
    
    /**
     * Returns a Collection of JRatings, where rating is 0..100
     * @param latitude optional, 
     * @param longitude optional
     * @param radius optional, should be 2, 3, or 4. Default is 3.
     * @param minRating optional, should be 1..100, default is 50.
     * @return a Collection of JRatings, where rating is minRating..100
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
        @RestCode(code=200, message="OK", description="Ratings found")
    })
    @RequestMapping(value="nearby", method= RequestMethod.GET)
    public ResponseEntity<Collection<JRating>> findNearbyRatings(HttpServletRequest request,
            @RequestParam(required=false) Float latitude,
            @RequestParam(required=false) Float longitude,
            @RequestParam(defaultValue="3") int radius,
            @RequestParam(defaultValue="50") int minRating) {
        if (null == latitude) {
            final String cityLatLong = request.getHeader("X-AppEngine-CityLatLong");
            if (null != cityLatLong) {
                final int index = cityLatLong.indexOf(',');
                latitude = Float.parseFloat(cityLatLong.substring(0, index));
                longitude = Float.parseFloat(cityLatLong.substring(index+1));
            }
        }
        
        final Collection<JRating> body = rnrService.findNearbyRatings(
                latitude, longitude, radius, minRating);
        return new ResponseEntity<Collection<JRating>>(body, HttpStatus.OK);
    }

    /**
     * Returns the average rating for specified productId
     * @param productId domain-unique id for the product to rate
     * @return the average rating for specified productId
     */
    @RestReturn(value=JResult.class, entity=JResult.class, code={
        @RestCode(code=200, message="OK", description="Rating added"),
        @RestCode(code=404, message="Not Found", description="Product not found")
    })
    @RequestMapping(value="{productId}", method= RequestMethod.GET)
    public ResponseEntity<JResult> getAverageRating(
            @PathVariable String productId) {
        final JResult body = rnrService.getAverage(productId);
        if (null == body) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<JResult>(body, HttpStatus.OK);
    }

    /**
     * Returns a list of average rating for all products
     * @return a list of average rating for all products
     */
    @RestReturn(value=JResultPage.class, entity=JResult.class, code={
        @RestCode(code=200, message="OK", description="Rating added"),
        @RestCode(code=404, message="Not Found", description="Product not found")
    })
    @RequestMapping(value="", method= RequestMethod.GET)
    public ResponseEntity<JResultPage> getAverageRatings(
            @RequestParam(required=false) String cursor,
            @RequestParam(defaultValue="0") long offset,
            @RequestParam(defaultValue="10") long limit
            ) {
        final JResultPage body = rnrService.getAveragePage(cursor, offset, limit);
        return new ResponseEntity<JResultPage>(body, HttpStatus.OK);
    }

    /**
     * Returns a list of average rating for specified products
     * @return a list of average rating for specified products
     */
    @RestReturn(value=JResultPage.class, entity=JResult.class, code={
        @RestCode(code=200, message="OK", description="Rating added")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="ids")
    public ResponseEntity<Collection<JResult>> getAverageRatings(
            @RequestParam(value="ids") String ids[]) {
        final Collection<JResult> body = rnrService.getAverageRatings(ids);
        return new ResponseEntity<Collection<JResult>>(body, HttpStatus.OK);
    }

    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }
}