package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.rnr.domain.DRating;
import com.wadpam.rnr.json.JHistogram;
import com.wadpam.rnr.json.JRating;
import com.wadpam.rnr.service.RnrService;
import com.wadpam.server.web.AbstractRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;


/**
 * The rating controller implements all REST methods related to ratings and reviews.
 * @author os
 */
@Controller
@RequestMapping(value="{domain}/rating")
public class RatingController extends AbstractRestController {

    static final Logger LOG = LoggerFactory.getLogger(RatingController.class);
    
    private RnrService rnrService;

    /**
     * Add a rating to a product.
     * @param productId domain-unique id for the product to rate
     * @param username optional. A unique user name or id.
     *                 Needed in order to perform user related operations later on.
     * @param latitude optional, -90..90
     * @param longitude optional, -180..180
     * @param rating mandatory, the rating 0..100
     * @param comment optional. review comment
     * @return the new rating
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
        @RestCode(code=302, message="OK", description="Redirect to newly created rating")
    })
    @RequestMapping(value="", method= RequestMethod.POST)
    public RedirectView addRating(HttpServletRequest request,
                                  Principal principal,
                                  @PathVariable String domain,
                                  @RequestParam(required=true) String productId,
                                  @RequestParam(required=false) String username,
                                  @RequestParam(required=false) Float latitude,
                                  @RequestParam(required=false) Float longitude,
                                  @RequestParam int rating,
                                  @RequestParam(required=false) String comment) {

        final DRating body = rnrService.addRating(domain, productId, username, latitude, longitude, rating, comment);

        return new RedirectView(request.getRequestURI() + "/" + body.getId().toString());
    }

    /**
     * Delete a rating with a specific id.
     * @param id The unique rating id
     * @return a http response code indicating the outcome of the operation
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
            @RestCode(code=200, message="OK", description="Rating deleted"),
            @RestCode(code=404, message="NOK", description="Rating not found and can not be deleted")
    })
    @RequestMapping(value="{id}", method= RequestMethod.DELETE)
    public ResponseEntity<JRating> deleteLike(HttpServletRequest request,
                                              Principal principal,
                                              @PathVariable long id) {

        final DRating body = rnrService.deleteRating(id);

        return new ResponseEntity<JRating>(HttpStatus.OK);
    }

    /**
     * Get rating details for a specific id.
     * @param id The unique like id
     * @return the rating details
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
            @RestCode(code=200, message="OK", description="Rating found"),
            @RestCode(code=404, message="NOK", description="Rating not found")
    })
    @RequestMapping(value="{id}", method= RequestMethod.GET)
    public ResponseEntity<JRating> getLike(HttpServletRequest request,
                                           Principal principal,
                                           @PathVariable long id) {

        final DRating body = rnrService.getRating(id);

        return new ResponseEntity<JRating>(Converter.convert(body), HttpStatus.OK);
    }

    /**
     * Return all ratings done by a specific user.
     * @param username a unique user name or id
     * @return a list of ratings
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
        @RestCode(code=200, message="OK", description="Ratings found for user")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="username")
    public ResponseEntity<Collection<JRating>> getMyRatings(HttpServletRequest request,
                                                            Principal principal,
                                                            @RequestParam(required=true) String username) {

        final Collection<DRating> body = rnrService.getMyRatings(username);

        return new ResponseEntity<Collection<JRating>>((Collection<JRating>)Converter.convert(body), HttpStatus.OK);
    }

    /**
     * Returns all ratings for a specific product.
     * @param productId the product to looks for
     * @return a list of ratings
     */
    @RestReturn(value=JRating.class, entity=JRating.class, code={
            @RestCode(code=200, message="OK", description="All ratings for product")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="productId")
    public ResponseEntity<Collection<JRating>> getAllRatingsForProduct(HttpServletRequest request,
                                                                       Principal principal,
                                                                       @RequestParam(required=true) String productId) {

        final Collection<DRating> body = rnrService.getAllRatingsForProduct(productId);

        return new ResponseEntity<Collection<JRating>>((Collection<JRating>)Converter.convert(body), HttpStatus.OK);
    }

    /**
     * Returns a histogram over all ratings for a specific product.
     * @param productId the product to looks for
     * @param interval Optional. the interval in the returned histogram. Default 10.
     * @return a histogram
     */
    @RestReturn(value=JHistogram.class, entity=JHistogram.class, code={
            @RestCode(code=200, message="OK", description="Histogram for product")
    })
    @RequestMapping(value="histogram", method= RequestMethod.GET, params="productId")
    public ResponseEntity<JHistogram> getHistogramForProduct(HttpServletRequest request,
                                                             Principal principal,
                                                             @RequestParam(required=true) String productId,
                                                             @RequestParam(required=false, defaultValue="10") int interval) {

        final Map<Long, Long> values = rnrService.getHistogramForProduct(productId, interval);

        JHistogram histogram = new JHistogram();
        histogram.setInterval(interval);
        histogram.setHistogram(values);

        return new ResponseEntity<JHistogram>(histogram, HttpStatus.OK);
    }


    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }
}
