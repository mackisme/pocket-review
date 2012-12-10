package com.wadpam.rnr.web;

import com.wadpam.docrest.domain.RestCode;
import com.wadpam.docrest.domain.RestReturn;
import com.wadpam.open.json.JCursorPage;
import com.wadpam.rnr.domain.DComment;
import com.wadpam.rnr.json.JComment;
import com.wadpam.rnr.service.RnrService;
import com.wadpam.server.exceptions.NotFoundException;
import com.wadpam.server.web.AbstractRestController;
import net.sf.mardao.core.CursorPage;
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
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * The comment controller implements all REST methods related to commenting.
 * @author mattiaslevin
 */
@Controller
@RequestMapping(value="{domain}/comment")
public class CommentController extends AbstractRestController {

    static final Logger LOG = LoggerFactory.getLogger(CommentController.class);
    static final Converter CONVERTER = new Converter();

    private RnrService rnrService;


    /**
     * Add a comment to a product.
     *
     * If you like to comment on a ratings, you can use the rating id as the product id.
     * If you need the ability to comment on both ratings and products you can either use
     * two different domains or prefix the productId depending on the type of comment,
     * e.g. PROD-3384, RATING-34
     * @param productId domain-unique id for the product to comment
     * @param username optional. A unique user name or id.
     *                 Needed in order to perform user related operations later on.
     * @param latitude optional. -90..90
     * @param longitude optional -180..180
     * @param comment the comment
     * @return the new comment
     */
    @RestReturn(value=JComment.class, entity=JComment.class, code={
            @RestCode(code=302, message="OK", description="Redirect to newly created comment")
    })
    @RequestMapping(value="", method= RequestMethod.POST)
    public RedirectView addComment(HttpServletRequest request,
                                   HttpServletResponse response,
                                   UriComponentsBuilder uriBuilder,
                                   @PathVariable String domain,
                                   @RequestParam(required=true) String productId,
                                   @RequestParam(required=false) String productName,
                                   @RequestParam(required=false) String username,
                                   @RequestParam(required=false) Float latitude,
                                   @RequestParam(required=false) Float longitude,
                                   @RequestParam(required=true) String comment) {

        final DComment body = rnrService.addComment(productId, username, latitude, longitude, comment);

        return new RedirectView(uriBuilder.path("/{domain}/comment/{id}").
                buildAndExpand(domain, body.getId()).toUriString());
    }

    /**
     * Delete a comment with a specific id.
     * @param id The unique comment id
     * @return a http response code indicating the outcome of the operation
     */
    @RestReturn(value=JComment.class, entity=JComment.class, code={
            @RestCode(code=200, message="OK", description="Comment deleted"),
            @RestCode(code=404, message="NOK", description="Comment not found and can not be deleted")
    })
    @RequestMapping(value="{id}", method= RequestMethod.DELETE)
    public ResponseEntity<JComment> deleteComment(HttpServletRequest request,
                                                  HttpServletResponse response,
                                                  @PathVariable long id) {

        final DComment body = rnrService.deleteComment(id);
        if (null == body)
            throw new NotFoundException(404, String.format("Comment with id:%s not found", id));

        return new ResponseEntity<JComment>(HttpStatus.OK);
    }

    /**
     * Get comment details for a specific id.
     * @param id The unique comment id
     * @return the comment details
     */
    @RestReturn(value=JComment.class, entity=JComment.class, code={
            @RestCode(code=200, message="OK", description="Comment found"),
            @RestCode(code=404, message="NOK", description="Comment not found")
    })
    @RequestMapping(value="{id}", method= RequestMethod.GET)
    public ResponseEntity<JComment> getComment(HttpServletRequest request,
                                               HttpServletResponse response,
                                               @PathVariable long id) {

        final DComment body = rnrService.getComment(id);
        if (null == body)
            throw new NotFoundException(404, String.format("Comment with id:%s not found", id));

        return new ResponseEntity<JComment>(CONVERTER.convert(body), HttpStatus.OK);
    }

    /**
     * Returns all comments done by a specific user.
     * @param username a unique user name or id
     * @return a list of comments
     */
    @RestReturn(value=JComment.class, entity=JComment.class, code={
            @RestCode(code=200, message="OK", description="All comments for user")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="username")
    public ResponseEntity<Collection<JComment>> getMyComments(HttpServletRequest request,
                                                              HttpServletResponse response,
                                                              @RequestParam(required=true) String username) {

        final Iterable<DComment> dCommentIterable = rnrService.getMyComments(username);

        return new ResponseEntity<Collection<JComment>>((Collection<JComment>)CONVERTER.convert(dCommentIterable), HttpStatus.OK);
    }

    /**
     * Returns all comments for a specific product.
     * @param productId the product to look for
     * @param pagesize Optional. The number of products to return in this page. Default value is 10.
     * @param cursor Optional. The current cursor position during pagination.
     *               The next page will be return from this position.
     *               If asking for the first page, not cursor should be provided.
     * @return a list of comments
     */
    @RestReturn(value=JComment.class, entity=JComment.class, code={
            @RestCode(code=200, message="OK", description="All comments for product")
    })
    @RequestMapping(value="", method= RequestMethod.GET, params="productId")
    public ResponseEntity<JCursorPage<JComment>> getAllCommentsForProduct(HttpServletRequest request,
                                                                          HttpServletResponse response,
                                                                          @RequestParam(required=true) String productId,
                                                                          @RequestParam(defaultValue="10") int pagesize,
                                                                          @RequestParam(required=false) String cursor) {

        final CursorPage<DComment, Long> dPage = rnrService.getAllCommentsForProduct(productId, pagesize, cursor);

        return new ResponseEntity<JCursorPage<JComment>>((JCursorPage<JComment>)CONVERTER.convert(dPage), HttpStatus.OK);
    }



    // Setters and Getters
    public void setRnrService(RnrService rnrService) {
        this.rnrService = rnrService;
    }

}
